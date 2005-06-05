package org.drools.spring.pojorule;

import org.drools.spi.Consequence;
import org.drools.spi.ConsequenceException;
import org.drools.spi.Tuple;

public class PojoConsequence implements Consequence
{
    private final RuleReflectMethod[] ruleMethods;

    public PojoConsequence(RuleReflectMethod[] ruleMethods) {
        this.ruleMethods = ruleMethods;
    }

    public String[] getMethodNames() {
        String[] names = new String[ruleMethods.length];
        for (int i = 0; i < ruleMethods.length; i++) {
            names[i] = ruleMethods[i].getMethodName();
        }
        return names;
    }

    public void invoke(Tuple tuple) throws ConsequenceException {
        try {
            for (int i = 0; i < ruleMethods.length; i++) {
                ruleMethods[i].invokeMethod(tuple);
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