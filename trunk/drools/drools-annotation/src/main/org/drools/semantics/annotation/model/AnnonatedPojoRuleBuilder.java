package org.drools.semantics.annotation.model;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.drools.DroolsException;
import org.drools.rule.Rule;
import org.drools.semantics.annotation.Condition;
import org.drools.semantics.annotation.Consequence;

public class AnnonatedPojoRuleBuilder {

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

    public static final class MissingConsequenceMethodException extends DroolsException {
        MissingConsequenceMethodException(String message) {
            super(message);
        }
    }

    // ---- ---- ----

    // TODO Extract the factory registy to its own abstraction.
    // Or maybe just take extensions in the builder instance.
    private static final List<ArgumentFactory> argumentSourceFactories = new ArrayList<ArgumentFactory>();

    static {
        registerArgumentSourceFactory(new TupleArgumentFactory());
        registerArgumentSourceFactory(new KnowledgeHelperArgumentFactory());
        registerArgumentSourceFactory(new ApplicationDataArgumentFactory());
    }
    private static final ArgumentFactory nullArgumentFactory = new ArgumentFactory() {
        public Class<? extends Argument> getArgumentType() {
            return null;
        }
        public Argument create(Rule rule, Class<?> parameterClass, Annotation[] parameterAnnotations) throws DroolsException {
            throw new InvalidParameterException("Method parameter type not recognized or not annotated");
        }
    };
    private static final ArgumentFactory tupleArgumentFactory = new ArgumentFactory() {
        public Class<? extends Argument> getArgumentType() {
            return TupleArgument.class;
        }
        public Argument create(Rule rule, Class<?> parameterClass, Annotation[] parameterAnnotations) throws DroolsException {
            return TupleArgumentFactory.createArgument(rule, parameterClass, null);
        }
    };

    public static void registerArgumentSourceFactory(ArgumentFactory factory) {
        for (ArgumentFactory registeredFactory : argumentSourceFactories) {
            if (factory.getArgumentType() == registeredFactory.getArgumentType()) {
                throw new IllegalArgumentException("ParameterValueFactory already registered"
                        + ": type=" + factory.getArgumentType() + ", factory =" + factory
                        + ", registered factory=" + registeredFactory);
            }
        }
        argumentSourceFactories.add(factory);
    }

    // ---- ---- ----

    private static class BuildContext {
        public Rule rule;
        public ArgumentFactory defaultArgumentFactory;
        public Object pojo;
    }

    public Rule buildRule(Rule rule, Object pojo) throws DroolsException {
        org.drools.semantics.annotation.Rule ruleAnnotation = pojo.getClass().getAnnotation(org.drools.semantics.annotation.Rule.class);

        BuildContext context = new BuildContext();
        context.rule = rule;
        context.pojo = pojo;
        context.defaultArgumentFactory = (ruleAnnotation.defaultParameterAnnotation())
                ? tupleArgumentFactory : nullArgumentFactory;

        buildConditions(context);
        buildConsequence(context);

        return context.rule;
    }

    private static void buildConditions(BuildContext context) throws DroolsException {
        for (Method method : context.pojo.getClass().getMethods()) {
            Condition conditionAnnotation = method.getAnnotation(Condition.class);
            if (conditionAnnotation != null) {
                PojoCondition condition = newPojoCondition(context, method);
                context.rule.addCondition(condition);
            }
        }
    }

    private static void buildConsequence(BuildContext context) throws DroolsException {
        PojoConsequence consequence = null;
        List<RuleReflectMethod> ruleReflectMethods = new ArrayList<RuleReflectMethod>();
        for (Method method : context.pojo.getClass().getMethods()) {
            Consequence consequenceAnnotation = method.getAnnotation(Consequence.class);
            if (consequenceAnnotation != null) {
                ruleReflectMethods.add(newConsequenceRuleReflectMethod(context, method));
            }
        }
        if (ruleReflectMethods.isEmpty()) {
            throw new MissingConsequenceMethodException(
                    "Rule must define at least one consequence method" +
                    ": class = " + context.pojo.getClass());
        }
        consequence = new PojoConsequence(ruleReflectMethods
                .toArray(new RuleReflectMethod[ruleReflectMethods.size()]));
        context.rule.setConsequence(consequence);
    }

    private static RuleReflectMethod newConsequenceRuleReflectMethod(BuildContext context, Method pojoMethod) throws DroolsException {
        assertReturnType(pojoMethod, void.class);
        Argument[] arguments = getArguments(context, pojoMethod);
        return new RuleReflectMethod(context.rule, context.pojo, pojoMethod, arguments);
   }

    private static PojoCondition newPojoCondition(BuildContext context, Method pojoMethod) throws DroolsException {
        assertReturnType(pojoMethod, boolean.class);
        Argument[] arguments = getArguments(context, pojoMethod);
        for (Argument argument : arguments) {
            if (argument instanceof KnowledgeHelperArgument) {
                throw new InvalidParameterException(
                        "Condition methods cannot declare a parameter of type KnowledgeHelper" +
                        ": method = " + pojoMethod);
            }
        }
        return new PojoCondition(new RuleReflectMethod(
                context.rule, context.pojo, pojoMethod, arguments));
    }

    private static void assertReturnType(Method method, Class returnClass)
            throws InvalidReturnTypeException {
        if (method.getReturnType() != returnClass) {
            throw new InvalidReturnTypeException("Rule method returns the wrong class"
                    + ": method = " + method + ", expected return class = " + returnClass
                    + ", actual return class = " + method.getReturnType());
        }
    }

    private static Argument[] getArguments(BuildContext context, Method method) throws DroolsException {
        Class[] parameterClasses = method.getParameterTypes();
        Annotation[][] parameterAnnotations = method.getParameterAnnotations();
        List<Argument> arguments = new ArrayList<Argument>();
        for (int i = 0; i < parameterClasses.length; i++) {
            Class parameterClass = parameterClasses[i];
            Argument argument = getArgument(context, parameterClass, parameterAnnotations[i]);
            arguments.add(argument);
        }
        return arguments.toArray(new Argument[arguments.size()]);
    }

    private static Argument getArgument(BuildContext context, Class parameterClass,
            Annotation[] parameterAnnotations) throws DroolsException {
        Argument parameterValue;
        for (ArgumentFactory factory : argumentSourceFactories) {
            parameterValue = factory.create(context.rule, parameterClass, parameterAnnotations);
            if (parameterValue != null) {
                return parameterValue;
            }
        }
        return context.defaultArgumentFactory.create(context.rule, parameterClass, parameterAnnotations);
    }
}
