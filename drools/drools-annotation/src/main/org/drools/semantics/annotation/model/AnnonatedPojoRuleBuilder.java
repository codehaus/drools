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
    public static final class InvalidReturnTypeException extends DroolsException {
        InvalidReturnTypeException(String message) {
            super(message);
        }
    }

    public static final class InvalidParameterException extends DroolsException {
        InvalidParameterException(String message) {
            super(message);
        }
        InvalidParameterException(String message, Throwable t) {
            super(message, t);
        }
    }

    public static final class MissingConsequenceMethodException extends DroolsException {
        MissingConsequenceMethodException(String message) {
            super(message);
        }
    }

    //---- ---- ----

    // TODO Extract the parameter factory registy to its own abstraction.
    // Or maybe just take extensions in the builder instance :)
    private static final Set<ParameterValueFactory> parameterValueFatories
            = new HashSet<ParameterValueFactory>();

    static {
        registerParameterValueFactory(new KnowledgeHelperParameterValueFactory());
        registerParameterValueFactory(new TupleParameterValueFactory());
        registerParameterValueFactory(new ApplicationDataParameterValueFactory());
    }

    public static void registerParameterValueFactory( ParameterValueFactory factory) {
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

    private static interface ParameterValidator {
        void assertParameter(ParameterValue newParameterValue, List<ParameterValue> parameterValues)
                throws DroolsException;
    }

    public Rule buildRule(Rule rule, Object pojo) throws DroolsException {
        Class< ? > ruleClass = pojo.getClass();
        buildConditions(rule, ruleClass, pojo);
        buildConsequence(rule, ruleClass, pojo);
        return rule;
    }

    private static void buildConditions(Rule rule, Class<?> ruleClass, Object pojo) throws DroolsException {
        for (Method method : ruleClass.getMethods()) {
            DroolsCondition conditionAnnotation = method.getAnnotation(DroolsCondition.class);
            if (conditionAnnotation != null) {
                PojoCondition condition = newPojoCondition(rule, pojo, method);
                rule.addCondition(condition);
            }
        }
    }

    private static void buildConsequence(Rule rule, Class< ? > ruleClass, Object pojo) throws DroolsException {
        Consequence consequence = null;
        List<RuleReflectMethod> ruleReflectMethods = new ArrayList<RuleReflectMethod>();
        for (Method method : ruleClass.getMethods()) {
            DroolsConsequence consequenceAnnotation = method.getAnnotation(DroolsConsequence.class);
            if (consequenceAnnotation != null) {
                ruleReflectMethods.add(newConsequenceRuleReflectMethod(rule, pojo, method));
            }
        }
        if (ruleReflectMethods.isEmpty()) {
            throw new MissingConsequenceMethodException( "Rule must define at least one consequence method" + ": class = " + ruleClass );
        }
        consequence = new PojoConsequence(
                ruleReflectMethods.toArray(new RuleReflectMethod[ruleReflectMethods.size()]));
        rule.setConsequence( consequence );
    }

    private static RuleReflectMethod newConsequenceRuleReflectMethod(Rule rule, Object pojo,
            Method pojoMethod) throws DroolsException {
        assertReturnType(pojoMethod, void.class);
        return new RuleReflectMethod(rule, pojo, pojoMethod, getParameterValues(rule, pojoMethod,
                new ConsequenceParameterValidator()));
    }

    private static final class ConditionParameterValidator implements ParameterValidator {
        public void assertParameter(ParameterValue newParameterValue,
                                    List<ParameterValue> parameterValues) throws InvalidReturnTypeException {
            if (newParameterValue instanceof KnowledgeHelperParameterValue) {
                throw new InvalidReturnTypeException(
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
                                    List<ParameterValue> parameterValues) throws InvalidReturnTypeException {
            if (newParameterValue instanceof KnowledgeHelperParameterValue) {
                if (hasDroolsParameterValue) {
                    throw new InvalidReturnTypeException(
                            "Consequence methods can only declare on parameter of type Drools" );
                }
                hasDroolsParameterValue = true;
            }
        }
    };

    private static void assertReturnType(Method method, Class returnClass) throws InvalidReturnTypeException {
        if (method.getReturnType( ) != returnClass) {
            throw new InvalidReturnTypeException(
                    "Rule method returns the wrong class" + ": method = "
                    + method + ", expected return class = " + returnClass
                    + ", actual return class = " + method.getReturnType( ) );
        }
    }

    private static ParameterValue[] getParameterValues(Rule rule, Method method, ParameterValidator validator)
            throws DroolsException {
        Class<?>[] parameterClasses = method.getParameterTypes();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        List<ParameterValue> parameterValues = new ArrayList<ParameterValue>();

        for (int i = 0; i < parameterClasses.length; i++) {
            Class<?> parameterClass = parameterClasses[i];
            ParameterValue parameterValue = null;
            try {
                parameterValue = getParameterValue(rule, parameterClass, parameterAnnotations[i]);
                validator.assertParameter(parameterValue, parameterValues);
            }
            catch (DroolsException e) {
              throw new InvalidParameterException(
                      e.getMessage() +
                      ": method = " + method + ", parameter[" + i +
                      "] = " + parameterClass, e );
            }
            parameterValues.add(parameterValue);
        }
        return parameterValues.toArray(new ParameterValue[parameterValues.size()]);
    }

    private static ParameterValue getParameterValue(Rule rule, Class<?> parameterClass,
                                                    Annotation[] parameterAnnotations)
                                                    throws DroolsException {
        ParameterValue parameterValue;
        for (ParameterValueFactory factory : parameterValueFatories) {
            parameterValue = factory.create(rule, parameterClass, parameterAnnotations);
            if (parameterValue != null) {
                return parameterValue;
            }
        }
        throw new InvalidParameterException(
                "Method parameter type not recognized or not annotated" );
    }

    private static void assertNonConflictingParameterAnnotation( ParameterValue parameterValue )
            throws InvalidParameterException {
        if (parameterValue != null) {
            throw new InvalidParameterException("Method parameter contains conflicting @Drools annotations");
        }
    }
}
