package org.drools.semantics.annotation.model;

import org.drools.spi.Consequence;
import org.drools.spi.ConsequenceException;
import org.drools.spi.Tuple;

class PojoConsequence implements Consequence
{
    private final RuleReflectMethod[] ruleMethods;

    public PojoConsequence(RuleReflectMethod[] ruleMethods) {
        this.ruleMethods = ruleMethods;
    }

    public void invoke(Tuple tuple) throws ConsequenceException {
        try {
            for (RuleReflectMethod ruleMethod : ruleMethods) {
                ruleMethod.invokeMethod( tuple );
            }
        } catch (Exception e) {
            throw new ConsequenceException( e );
        }
    }

    public String toString() {
        // TODO Ensure this is clear
        return ruleMethods.toString();
    }
}