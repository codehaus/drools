
package org.drools.spi;

import org.drools.WorkingMemory;

import java.util.List;
import java.util.ArrayList;

public class InstrumentedConsequence
    implements Consequence
{
    private List invokedTuples;

    public InstrumentedConsequence()
    {
        super();
        this.invokedTuples = new ArrayList();
    }

    public void invoke(Tuple tuple,
                       WorkingMemory workingMemory)
    {
        this.invokedTuples.add( tuple );
    }

    public List getInvokedTuples()
    {
        return this.invokedTuples;
    }
}
