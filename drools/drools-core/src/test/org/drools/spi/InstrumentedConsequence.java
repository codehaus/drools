package org.drools.spi;

import java.util.ArrayList;
import java.util.List;

public class InstrumentedConsequence
    implements
    Consequence
{
    private List invokedTuples;

    public InstrumentedConsequence()
    {
        super( );
        this.invokedTuples = new ArrayList( );
    }

    public void invoke(Tuple tuple)
    {
        this.invokedTuples.add( tuple );
    }

    public List getInvokedTuples()
    {
        return this.invokedTuples;
    }
}