
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

    /** Construct.
     *
     *  @param action The <code>Action</code> to invoke upon match.
     */
    public TerminalNode(TupleSource tupleSource,
                        Action action,
                        int priority)
    {
        this.action = action;

        this.priority = priority;

        if ( tupleSource != null )
        {
            tupleSource.setTupleSink( this );
        }
    }

    public int getPriority()
    {
        return this.priority;
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
        /*
        Action action = getAction();

        if ( action != null )
        {
            action.invoke( tuple,
                           workingMemory );
        }
        */

        Action action = getAction();

        Agenda agenda = workingMemory.getAgenda();

        agenda.addToAgenda( tuple,
                            action );
    }

    public void retractObject(TupleSource inputSource,
                              Object object,
                              WorkingMemory workingMemory)
    {

    }
}
