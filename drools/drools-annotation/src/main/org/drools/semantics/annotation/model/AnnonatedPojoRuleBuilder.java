package org.drools.semantics.annotation.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.drools.DroolsException;
import org.drools.rule.Rule;
import org.drools.semantics.annotation.DroolsCondition;
import org.drools.semantics.annotation.DroolsConsequence;
import org.drools.spi.Consequence;
import org.drools.semantics.annotation.model.ParameterValue;

public class AnnonatedPojoRuleBuilder
{
    // TODO Extract the parameter factory registy to its own abstraction.
    // Or maybe just take extensions in the builder instance :)
    private static final Set<ParameterValueFactory> parameterValueFatories
            = new HashSet<ParameterValueFactory>();

    static {
        registerParameterValueFactory(new KnowledgeHelperParameterValueFactory());
        registerParameterValueFactory(new TupleParameterValueFactory());
        registerParameterValueFactory(new ApplicationDataParameterValueFactory());
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

    //---- ---- ----

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
            DroolsConsequence consequenceAnnotation = method.getAnnotation( DroolsConsequence.class );
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
            throw new DroolsException( "Rule must define a consequence method" + ": class = " + ruleClass );
        }
    }


    private static final class ConditionParameterValidator implements ParameterValidator {
        public void assertParameter(ParameterValue newParameterValue,
                List<ParameterValue> parameterValues) throws DroolsException {
            if (newParameterValue instanceof KnowledgeHelperParameterValue) {
                throw new DroolsException(
                        "Condition methods cannot declare a parameter of type KnowledgeHelper");
            }
        }
    };

    private static PojoCondition newPojoCondition( Rule rule, Object pojo, Method pojoMethod )
            throws DroolsException {
        assertReturnType( pojoMethod, boolean.class );
        return new PojoCondition( new RuleReflectMethod( rule, pojo, pojoMethod,
                getParameterValues( rule, pojoMethod, new ConditionParameterValidator()) ) );
    }


    private static final class ConsequenceParameterValidator implements ParameterValidator {
        private boolean hasDroolsParameterValue;

        public void assertParameter(ParameterValue newParameterValue,
                                    List<ParameterValue> parameterValues) throws DroolsException {
            if (newParameterValue instanceof KnowledgeHelperParameterValue) {
                if (hasDroolsParameterValue) {
                    throw new DroolsException(
                            "Consequence methods can only declare on parameter of type Drools" );
                }
                hasDroolsParameterValue = true;
            }
        }
    };

    private static PojoConsequence newPojoConsequence( Rule rule, Object pojo, Method pojoMethod )
            throws DroolsException
    {
        assertReturnType( pojoMethod, void.class );
        return new PojoConsequence( new RuleReflectMethod( rule, pojo, pojoMethod,
                getParameterValues( rule, pojoMethod, new ConsequenceParameterValidator() ) ) );
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
