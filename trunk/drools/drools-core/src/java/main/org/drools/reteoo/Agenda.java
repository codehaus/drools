
package org.drools.reteoo;

import org.drools.WorkingMemory;

import org.drools.spi.Action;
import org.drools.spi.Declaration;
import org.drools.spi.ActionInvokationException;

import java.util.LinkedList;
import java.util.Iterator;

public class Agenda
{
    private WorkingMemory workingMemory;
    private LinkedList items;

    public Agenda(WorkingMemory workingMemory)
    {
        this.workingMemory = workingMemory;

        this.items = new LinkedList();
    }

    void addToAgenda(ReteTuple tuple,
                     Action action)
    {
        if ( action == null )
        {
            return;
        }

        this.items.add( new AgendaItem( tuple,
                                        action )  );
    }
    
    public void modifyObject(Object object)
    {
        retractObject( object );
    }
    
    public void retractObject(Object object)
    {
        Iterator   itemIter = this.items.iterator();
        AgendaItem eachItem = null;

        while ( itemIter.hasNext() )
        {
            eachItem = (AgendaItem) itemIter.next();

            if ( eachItem.containsRootFactObject( object ) )
            {
                itemIter.remove();
            }
        }
    }

    public boolean isEmpty()
    {
        return this.items.isEmpty();
    }

    public void fireNextItem() throws ActionInvokationException
    {
        if ( isEmpty() )
        {
            return;
        }

        AgendaItem item = (AgendaItem) this.items.removeFirst();

        item.fire( this.workingMemory );
    }
}

class AgendaItem
{
    private ReteTuple tuple;
    private Action    action;
    
    AgendaItem(ReteTuple tuple,
               Action action)
    {
        this.tuple   = tuple;
        this.action = action;
    }

    boolean containsRootFactObject(Object object)
    {
        return getTuple().containsRootFactObject( object );
    }

    ReteTuple getTuple()
    {
        return this.tuple;
    }
    
    Action getAction()
    {
        return this.action;
    }

    void fire(WorkingMemory workingMemory) throws ActionInvokationException
    {
        getAction().invoke( getTuple(),
                            workingMemory );
    }
}
