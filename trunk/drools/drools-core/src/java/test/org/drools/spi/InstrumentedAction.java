
package org.drools.spi;

import org.drools.WorkingMemory;

import java.util.List;
import java.util.ArrayList;

public class InstrumentedAction implements Action
{
    private List invokedTuples;

    public InstrumentedAction()
    {
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
