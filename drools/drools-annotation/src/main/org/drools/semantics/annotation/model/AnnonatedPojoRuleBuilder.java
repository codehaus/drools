package org.drools.semantics.annotation.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.drools.DroolsException;
import org.drools.rule.Declaration;
import org.drools.rule.InvalidRuleException;
import org.drools.rule.Rule;
import org.drools.semantics.annotation.DroolsParameter;
import org.drools.semantics.annotation.DroolsApplicationData;
import org.drools.semantics.annotation.DroolsCondition;
import org.drools.semantics.annotation.DroolsConsequence;
import org.drools.semantics.base.ClassObjectType;
import org.drools.spi.Consequence;
import org.drools.spi.KnowledgeHelper;

public class AnnonatedPojoRuleBuilder
{
    // TODO Extract the parameter factory registy to its own abstraction
    private static final Set<ParameterValueFactory> parameterValueFatories
            = new HashSet<ParameterValueFactory>();

    static {
        registerParameterValueFactory(new KnowledgeHelperParameterValueFactory());
        registerParameterValueFactory(new DroolsTupleParameterValueFactory());
        registerParameterValueFactory(new DroolsApplicationDataParameterValueFactory());
    }

    public static void registerParameterValueFactory( ParameterValueFactory factory)
    {
        for (ParameterValueFactory registeredFactory : parameterValueFatories) {
            if (factory.getParameterValueType() == registeredFactory.getParameterValueType()) {
                throw new IllegalArgumentException("ParameterValueFactory already registered"
                                                   + ": type=" + factory.getParameterValueType()
                                                   + ", factory =" + factory
                                                   + ", registered factory=" + registeredFactory);
            }
        }
        parameterValueFatories.add(factory);
    }

    private static interface ParameterValidator
    {
        void assertParameter( ParameterValue newParameterValue, List<ParameterValue> parameterValues )
                throws DroolsException;
    }

    public Rule buildRule( Rule rule, Object pojo ) throws DroolsException
    {
        Class< ? > ruleClass = pojo.getClass( );
        buildConditions( rule, ruleClass, pojo );
        buildConsequence( rule, ruleClass, pojo );
        return rule;
    }

    private static void buildConditions( Rule rule, Class< ? > ruleClass, Object pojo )
            throws DroolsException
    {
        for (Method method : ruleClass.getMethods( ))
        {
            DroolsCondition conditionAnnotation = method.getAnnotation( DroolsCondition.class );
            if (conditionAnnotation != null)
            {
                PojoCondition condition = newPojoCondition( rule, pojo, method );
                rule.addCondition( condition );
            }
        }
    }

    private static void buildConsequence( Rule rule, Class< ? > ruleClass, Object pojo )
            throws DroolsException
    {
        Consequence consequence = null;
        for (Method method : ruleClass.getMethods( ))
        {
            DroolsConsequence consequenceAnnotation = method
                    .getAnnotation( DroolsConsequence.class );
            if (consequenceAnnotation != null)
            {
                if (consequence != null)
                {
                    throw new DroolsException( "Rule must only contain one consequence method"
                            + ": class = " + ruleClass + ", method = " + method );
                }
                consequence = newPojoConsequence( rule, pojo, method );
                rule.setConsequence( consequence );
            }
        }
        if (consequence == null)
        {
            throw new DroolsException( "Rule must define a consequence method" + ": class = "
                    + ruleClass );
        }
    }

    private static PojoCondition newPojoCondition( Rule rule, Object pojo, Method pojoMethod )
            throws DroolsException
    {
        assertReturnType( pojoMethod, boolean.class );
        ParameterValidator parameterValidator = new ParameterValidator( ) {
            public void assertParameter( ParameterValue newParameterValue,
                                        List<ParameterValue> parameterValues )
                    throws DroolsException
            {
                if (newParameterValue instanceof KnowledgeHelperParameterValue)
                {
                    throw new DroolsException(
                            "Condition methods cannot declare a parameter of type KnowledgeHelper" );
                }
            }
        };
        return new PojoCondition( new RuleReflectMethod( rule, pojo, pojoMethod,
                getParameterValues( rule, pojoMethod, parameterValidator ) ) );
    }

    private static PojoConsequence newPojoConsequence( Rule rule, Object pojo, Method pojoMethod )
            throws DroolsException
    {
        assertReturnType( pojoMethod, void.class );
        ParameterValidator parameterValidator = new ParameterValidator( ) {
            private boolean hasDroolsParameterValue;

            public void assertParameter( ParameterValue newParameterValue,
                                        List<ParameterValue> parameterValues )
                    throws DroolsException
            {
                if (newParameterValue instanceof KnowledgeHelperParameterValue)
                {
                    if (hasDroolsParameterValue)
                    {
                        throw new DroolsException(
                                "Consequence methods can only declare on parameter of type Drools" );
                    }
                    hasDroolsParameterValue = true;
                }
            }
        };
        return new PojoConsequence( new RuleReflectMethod( rule, pojo, pojoMethod,
                getParameterValues( rule, pojoMethod, parameterValidator ) ) );
    }

    private static void assertReturnType( Method method, Class returnClass ) throws DroolsException
    {
        if (method.getReturnType( ) != returnClass)
        {
            throw new DroolsException( "Rule method returns the wrong class" + ": method = "
                    + method + ", expected return class = " + returnClass
                    + ", actual return class = " + method.getReturnType( ) );
        }
    }

    private static ParameterValue[] getParameterValues( Rule rule, Method method,
                                                       ParameterValidator validator )
            throws DroolsException
    {
        Class< ? >[] parameterClasses = method.getParameterTypes( );
        Annotation[][] parameterAnnotations = method.getParameterAnnotations( );
        List<ParameterValue> parameterValues = new ArrayList<ParameterValue>( );

        for (int i = 0; i < parameterClasses.length; i++)
        {
            Class< ? > parameterClass = parameterClasses[i];
            ParameterValue parameterValue = null;
            try
            {
                parameterValue = getParameterValue( rule, parameterClass, parameterAnnotations[i] );
                validator.assertParameter( parameterValue, parameterValues );
            }
            catch (DroolsException e)
            {
                throwContextDroolsException( method, i, parameterClass, e );
            }
            parameterValues.add( parameterValue );
        }
        return parameterValues.toArray( new ParameterValue[parameterValues.size( )] );
    }

    // TODO Extract all these classes to file-level.
    public interface ParameterValueFactory
    {
        Class<? extends ParameterValue> getParameterValueType();

        ParameterValue create ( Rule rule, Class< ? > parameterClass,
                                Annotation[] parameterAnnotations) throws DroolsException;
    }

    public static abstract class AnnotationParameterValueFactory implements ParameterValueFactory
    {
        private final Class<? extends Annotation> annotationClass;

        protected AnnotationParameterValueFactory(Class<? extends Annotation> annotationClass) {
            this.annotationClass = annotationClass;
        }

        protected abstract ParameterValue doCreate( Rule rule,
                                                    Class< ? > parameterClass,
                                                    Annotation annotation) throws InvalidRuleException;

        public ParameterValue create ( Rule rule, Class< ? > parameterClass,
                                       Annotation[] parameterAnnotations) throws DroolsException {
            Annotation annotation = getAnnotation(annotationClass, parameterAnnotations);
            if (annotation == null) {
                return null;
            }
            return doCreate(rule, parameterClass, annotation);
        }

        private Annotation getAnnotation(Class<? extends Annotation> annotationClass, Annotation[] parameterAnnotations) {
            for (Annotation annotation : parameterAnnotations) {
                if (annotationClass.isAssignableFrom(annotation.getClass())) {
                    return annotation;
                }
            }
            return null;
        }
    }

    public static class KnowledgeHelperParameterValueFactory implements ParameterValueFactory
    {
        public Class<? extends ParameterValue> getParameterValueType() {
            return KnowledgeHelperParameterValue.class;
        }

        public ParameterValue create ( Rule rule, Class< ? > parameterClass,
                                       Annotation[] parameterAnnotations) {
            if (parameterClass != KnowledgeHelper.class) {
                return null;
            }
            return new KnowledgeHelperParameterValue( rule );
        }
    }

    public static class DroolsTupleParameterValueFactory extends AnnotationParameterValueFactory
    {
        public DroolsTupleParameterValueFactory() {
            super(DroolsParameter.class);
        }

        public Class<? extends ParameterValue> getParameterValueType() {
            return TupleParameterValue.class;
        }

        @Override
        public ParameterValue doCreate ( Rule rule, Class< ? > parameterClass,
                                         Annotation annotation) throws InvalidRuleException {
            String parameterId = ((DroolsParameter) annotation).value( );
            Declaration declaration = rule.getParameterDeclaration( parameterId );
            if (declaration == null)
            {
                ClassObjectType classObjectType = new ClassObjectType( parameterClass );
                declaration = rule.addParameterDeclaration( parameterId, classObjectType );
            }
            return new TupleParameterValue( declaration );
        }
    }

    public static class DroolsApplicationDataParameterValueFactory extends AnnotationParameterValueFactory
    {
        public DroolsApplicationDataParameterValueFactory() {
            super(DroolsApplicationData.class);
        }

        public Class<? extends ParameterValue> getParameterValueType() {
            return ApplicationDataParameterValue.class;
        }

        @Override
        public ParameterValue doCreate ( Rule rule, Class< ? > parameterClass,
                                         Annotation annotation) {
            String parameterId = ((DroolsApplicationData) annotation).value( );
            return new ApplicationDataParameterValue( parameterId, parameterClass );
        }
    }

    private static ParameterValue getParameterValue( Rule rule, Class< ? > parameterClass,
                                                     Annotation[] parameterAnnotations )
                                                     throws DroolsException
    {
        ParameterValue parameterValue;
        for (ParameterValueFactory factory : parameterValueFatories) {
            parameterValue = factory.create(rule, parameterClass, parameterAnnotations);
            if (parameterValue != null) {
                return parameterValue;
            }
        }
        throw new DroolsException(
                "Method parameter type not recognized or not annotated" );
    }

    private static void throwContextDroolsException( Method method, int i,
                                                     Class< ? > parameterClass, Exception e )
             throws DroolsException
     {
         throw new DroolsException( e.getMessage( ) + ": method = " + method + ", parameter[" + i
                 + "] = " + parameterClass, e );
     }

    private static void assertNonConflictingParameterAnnotation( ParameterValue parameterValue )
            throws DroolsException
    {
        if (parameterValue != null)
        {
            throw new DroolsException( "Method parameter contains conflicting @Drools annotations" );
        }
    }
}
