
package org.drools.reteoo;

import org.drools.WorkingMemory;

import org.drools.spi.Action;
import org.drools.spi.ActionInvokationException;

/** Item entry in the <code>Agenda</code>.
 */
class AgendaItem
{
    private ReteTuple tuple;
    private Action    action;
    private long      duration;
    
    AgendaItem(ReteTuple tuple,
               Action action,
               long duration)
    {
        this.tuple    = tuple;
        this.action   = action;
        this.duration = duration;
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
    
    Action getAction()
    {
        return this.action;
    }

    long getDuration()
    {
        return this.duration;
    }

    void fire(WorkingMemory workingMemory) throws ActionInvokationException
    {
        getAction().invoke( getTuple(),
                            workingMemory );
    }

    public String toString()
    {
        return "[" + getTuple() + getAction() + "]";
    }
}
