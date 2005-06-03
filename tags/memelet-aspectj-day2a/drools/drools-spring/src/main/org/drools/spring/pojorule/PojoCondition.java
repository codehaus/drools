package org.drools.spring.pojorule;

import java.util.ArrayList;
import java.util.List;

import org.drools.rule.Declaration;
import org.drools.spi.Condition;
import org.drools.spi.ConditionException;
import org.drools.spi.Tuple;

public class PojoCondition implements Condition {

    private final RuleReflectMethod ruleMethod;
    private final Declaration[] requiredDeclarations;

    public PojoCondition(RuleReflectMethod ruleMethod) {
        this.ruleMethod = ruleMethod;
        this.requiredDeclarations = extractDeclarations(ruleMethod.getArguments());
    }

    public String getMethodName() {
        return ruleMethod.getMethodName();
    }

    private static Declaration[] extractDeclarations(Argument[] arguments) {
        List declarations = new ArrayList(arguments.length);
        for (int i = 0; i < arguments.length; i++) {
            Argument argument = arguments[i];
            if (argument instanceof FactArgument) {
                declarations.add(((FactArgument) argument).getDeclaration());
            }
        }
        return (Declaration[])declarations.toArray(new Declaration[declarations.size()]);
    }

    public Declaration[] getRequiredTupleMembers() {
        return requiredDeclarations;
    }

    public boolean isAllowed(Tuple tuple) throws ConditionException {
        try {
            return ((Boolean)ruleMethod.invokeMethod(tuple)).booleanValue();
        } catch (Exception e) {
            throw new ConditionException(e);
        }
    }

    public String toString() {
        return ruleMethod.toString();
    }
}