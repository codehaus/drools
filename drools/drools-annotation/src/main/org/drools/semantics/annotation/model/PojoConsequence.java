package org.drools.semantics.annotation.model;

import org.drools.WorkingMemory;
import org.drools.spi.Consequence;
import org.drools.spi.ConsequenceException;
import org.drools.spi.Tuple;

class PojoConsequence implements Consequence
{
    private final RuleReflectMethod ruleMethod;

    public PojoConsequence( RuleReflectMethod ruleMethod )
    {
        this.ruleMethod = ruleMethod;
    }

    public void invoke( Tuple tuple, WorkingMemory workingMemory ) throws ConsequenceException
    {
        try
        {
            ruleMethod.invokeMethod( tuple );
        }
        catch (Exception e)
        {
            throw new ConsequenceException( e );
        }
    }
    
    public String toString() {
        return ruleMethod.toString();
    }
}