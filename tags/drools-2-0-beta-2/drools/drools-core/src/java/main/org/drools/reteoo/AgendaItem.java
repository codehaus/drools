
package org.drools.reteoo;

import org.drools.WorkingMemory;

import org.drools.spi.Rule;
import org.drools.spi.Action;
import org.drools.spi.ActionInvokationException;

/** Item entry in the <code>Agenda</code>.
 */
class AgendaItem
{
    private ReteTuple tuple;
    private Rule      rule;
    
    AgendaItem(ReteTuple tuple,
               Rule rule)
    {
        this.tuple    = tuple;
        this.rule     = rule;
    }

    Rule getRule()
    {
        return this.rule;
    }

    boolean dependsOn(Object object)
    {
        return getTuple().dependsOn( object );
    }

    void setTuple(ReteTuple tuple)
    {
        this.tuple = tuple;
    }

    ReteTuple getTuple()
    {
        return this.tuple;
    }
    
    void fire(WorkingMemory workingMemory) throws ActionInvokationException
    {
        getRule().getAction().invoke( getTuple(),
                                      workingMemory );
    }

    public String toString()
    {
        return "[" + getTuple() + getRule().getName() + "]";
    }
}
