package org.drools.spring.factory;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.drools.DroolsException;
import org.drools.rule.Rule;
import org.drools.spring.metadata.ArgumentMetadata;
import org.drools.spring.metadata.ArgumentMetadataSource;
import org.drools.spring.metadata.MethodMetadata;
import org.drools.spring.metadata.MethodMetadataSource;
import org.drools.spring.pojorule.Argument;
import org.drools.spring.pojorule.PojoCondition;
import org.drools.spring.pojorule.PojoConsequence;
import org.drools.spring.pojorule.RuleReflectMethod;

public class RuleBuilder {

    public static final class InvalidReturnTypeException extends DroolsException {
        InvalidReturnTypeException(String message) {
            super(message);
        }
    }

    public static final class InvalidParameterException extends DroolsException {
        InvalidParameterException(String message) {
            super(message);
        }
    }

    // ---- ---- ----

    private MethodMetadataSource methodMetadataSource;
    private ArgumentMetadataSource argumentMetadataSource;

    public void setMethodMetadataSource(MethodMetadataSource methodMetadataSource) {
        this.methodMetadataSource = methodMetadataSource;
    }

    public void setArgumentMetadataSource(ArgumentMetadataSource argumentMetadataSource) {
        this.argumentMetadataSource = argumentMetadataSource;
    }

    // ---- ---- ----

    public Rule buildRule(Rule rule, Object pojo) throws DroolsException {
        List conditionRuleReflectMethods = new ArrayList();

        Method[] pojoMethods = pojo.getClass().getMethods();
        for (int i = 0; i < pojoMethods.length; i++) {
            Method pojoMethod = pojoMethods[i];
            MethodMetadata methodMedata = methodMetadataSource.getMethodMetadata(pojoMethod);
            if (methodMedata == null) {
                continue;
            }

            ArgumentMetadata[] argumentsMetadata = getArgumentMetadata(pojoMethod);
            Argument[] arguments = getArguments(rule, argumentsMetadata);

            if (methodMedata.getMethodType() == MethodMetadata.CONDITION) {
                assertReturnType(pojoMethod, boolean.class);
                rule.addCondition(
                        new PojoCondition(new RuleReflectMethod(rule, pojo, pojoMethod, arguments)));

            } else if (methodMedata.getMethodType() == MethodMetadata.CONSEQUENCE) {
                conditionRuleReflectMethods.add(
                        new RuleReflectMethod(rule, pojo, pojoMethod, arguments));

            }
        }

        if (!conditionRuleReflectMethods.isEmpty()) {
            addConsequence(rule, conditionRuleReflectMethods);
        }

        rule.checkValidity();
        return rule;
    }

    private ArgumentMetadata[] getArgumentMetadata(Method pojoMethod) throws InvalidParameterException {
        Class[] parameterTypes = pojoMethod.getParameterTypes();
        ArgumentMetadata[] metadata = new ArgumentMetadata[parameterTypes.length];
        for (int i = 0; i < parameterTypes.length; i++) {
            metadata[i] = argumentMetadataSource.getArgumentMetadata(pojoMethod, parameterTypes[i], i);
            if (metadata[i] == null) {
                throw new InvalidParameterException("Cannot determine parameter metdata"
                        + ": method=" + pojoMethod.getName()
                        + ", parameterType[" + i + "]=" + parameterTypes[i]);
            }
        }
        return metadata;
    }

    private Argument[] getArguments(Rule rule, ArgumentMetadata[] argumentsMetadata) throws DroolsException {
        Argument[] arguments = new Argument[argumentsMetadata.length];
        for (int i = 0; i < argumentsMetadata.length; i++) {
            arguments[i] = argumentsMetadata[i].createArgument(rule);
        }
        return arguments;
    }

    private static void assertReturnType(Method method, Class returnClass)
            throws InvalidReturnTypeException {
        if (method.getReturnType() != returnClass) {
            throw new InvalidReturnTypeException("Rule method returns the wrong class"
                    + ": method = " + method + ", expected return class = " + returnClass
                    + ", actual return class = " + method.getReturnType());
        }
    }

    private void addConsequence(Rule rule, List conditionRuleReflectMethodsCollector) {
        RuleReflectMethod[] conditionRuleReflectMethodArray = (RuleReflectMethod[]) conditionRuleReflectMethodsCollector.toArray(
                new RuleReflectMethod[conditionRuleReflectMethodsCollector.size()]);
        PojoConsequence consequence = new PojoConsequence(conditionRuleReflectMethodArray);
        rule.setConsequence(consequence);
    }
}
