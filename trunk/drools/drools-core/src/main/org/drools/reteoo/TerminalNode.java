
package org.drools.reteoo;

import org.drools.WorkingMemory;
import org.drools.AssertionException;
import org.drools.RetractionException;
import org.drools.ModificationException;

import org.drools.spi.Action;

/** Leaf Rete-OO node responsible for enacting {@link Action}s
 *  on a matched {@link org.drools.spi.Rule}.
 *
 *  @see org.drools.spi.Rule
 *  @see org.drools.spi.Action
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class TerminalNode implements TupleSink
{
    /** Total-ordering priority of this terminal node
     *  for rule-firings. */
    private int priority;

    /** The action to invoke upon match. */
    private Action action;

    private long duration;

    /** Construct.
     *
     *  @param action The <code>Action</code> to invoke upon match.
     */
    public TerminalNode(TupleSource tupleSource,
                        Action action,
                        long duration,
                        int priority)
    {
        this.action = action;

        this.priority = priority;

        this.duration = duration;

        if ( tupleSource != null )
        {
            tupleSource.setTupleSink( this );
        }
    }

    public int getPriority()
    {
        return this.priority;
    }

    public long getDuration()
    {
        return this.duration;
    }

    /** Retrieve the <code>Action</code> associated with
     *  this node.
     *
     *  @return The <code>Action</code> associated with
     *          this node.
     */
    public Action getAction()
    {
        return this.action;
    }

    public void assertTuple(TupleSource inputSource,
                            ReteTuple tuple,
                            WorkingMemory workingMemory) throws AssertionException
    {
        Agenda agenda = workingMemory.getAgenda();

        agenda.addToAgenda( tuple,
                            getAction(),
                            getDuration() );
    }

    public void retractTuples(TupleKey key,
                              WorkingMemory workingMemory) 
    {
        Agenda agenda = workingMemory.getAgenda();

        agenda.removeFromAgenda( key,
                                 getAction() );
    }

    public void modifyTuples(TupleSource tupleSource,
                             Object trigger,
                             TupleSet newTuples,
                             WorkingMemory workingMemory)
    {
        Agenda agenda = workingMemory.getAgenda();
        
        agenda.modifyAgenda( newTuples,
                             getAction(),
                             getDuration() );
    }
}
