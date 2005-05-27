package org.drools.reteoo;

import java.util.ArrayList;
import java.util.List;

import org.drools.rule.Rule;
import org.drools.spi.ConflictResolver;

public class InstrumentedAgenda extends Agenda
{
    private List added;

    private List removed;

    private List modified;

    public InstrumentedAgenda(WorkingMemoryImpl workingMemory,
                              ConflictResolver conflictResolver)
    {
        super( workingMemory, conflictResolver );

        this.added = new ArrayList( );
        this.removed = new ArrayList( );
        this.modified = new ArrayList( );
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

    public void addToAgenda(ReteTuple tuple, Rule rule)
    {
        this.added.add( tuple );
        super.addToAgenda( tuple, rule );
    }

    public void removeFromAgenda(TupleKey key, Rule rule)
    {
        this.removed.add( key );
        super.removeFromAgenda( key, rule );
    }

}

