package org.drools.reteoo;

import org.drools.FactHandle;
import org.drools.rule.Rule;
import org.drools.spi.ConflictResolutionStrategy;

import java.util.List;
import java.util.ArrayList;

public class InstrumentedAgenda
    extends Agenda
{
    private List added;
    private List removed;
    private List modified;

    public InstrumentedAgenda(WorkingMemoryImpl workingMemory,
                              ConflictResolutionStrategy conflictResolution)
    {
        super( workingMemory,
               conflictResolution );

        this.added = new ArrayList();
        this.removed = new ArrayList();
        this.modified = new ArrayList();
    }

    public List getAdded()
    {
        return this.added;
    }

    public List getRemoved()
    {
        return this.removed;
    }

    public List getModified()
    {
        return this.modified;
    }

    public void addToAgenda(ReteTuple tuple,
                            Rule rule)
    {
        this.added.add( tuple );
        super.addToAgenda( tuple,
                           rule );
    }

    public void removeFromAgenda(TupleKey key,
                                 Rule rule)
    {
        this.removed.add( key );
        super.removeFromAgenda( key,
                                rule );
    }

    public void modifyAgenda(FactHandle handle,
                             TupleSet newTuples,
                             Rule rule)
    {
        this.modified.add( handle );
        super.modifyAgenda( handle,
                            newTuples,
                            rule );
    }
}

