package org.drools.semantics.annotation.model;

import java.util.ArrayList;
import java.util.List;

import org.drools.rule.Declaration;
import org.drools.spi.Condition;
import org.drools.spi.ConditionException;
import org.drools.spi.Tuple;

class PojoCondition implements Condition {
    
    private final RuleReflectMethod ruleMethod;
    private final Declaration[] requiredDeclarations;

    public PojoCondition(RuleReflectMethod ruleMethod) {
        this.ruleMethod = ruleMethod;
        this.requiredDeclarations = extractDeclarations(ruleMethod.getArguments());
    }

    private static Declaration[] extractDeclarations(Argument[] arguments) {
        List<Declaration> declarations = new ArrayList<Declaration>(arguments.length);
        for (Argument arg : arguments) {
            if (arg instanceof TupleArgument) {
                declarations.add(((TupleArgument) arg).getDeclaration());
            }
        }
        return declarations.toArray(new Declaration[declarations.size()]);
    }

    public Declaration[] getRequiredTupleMembers() {
        return requiredDeclarations;
    }

    public boolean isAllowed(Tuple tuple) throws ConditionException {
        try {
            return (Boolean) ruleMethod.invokeMethod(tuple);
        } catch (Exception e) {
            throw new ConditionException(e);
        }
    }

    public String toString() {
        return ruleMethod.toString();
    }
}