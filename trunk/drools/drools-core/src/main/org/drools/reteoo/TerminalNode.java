
package org.drools.reteoo;

import org.drools.WorkingMemory;
import org.drools.AssertionException;
import org.drools.RetractionException;

import org.drools.spi.Rule;

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

    /** The rule to invoke upon match. */
    private Rule rule;

    /** Construct.
     *
     *  @param action The <code>Action</code> to invoke upon match.
     */
    public TerminalNode(TupleSource tupleSource,
                        Rule rule,
                        int priority)
    {
        this.rule = rule;

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
    public Rule getRule()
    {
        return this.rule;
    }

    public void assertTuple(TupleSource inputSource,
                            ReteTuple tuple,
                            WorkingMemory workingMemory) throws AssertionException
    {
        Agenda agenda = workingMemory.getAgenda();

        agenda.addToAgenda( tuple,
                            getRule(),
                            getPriority() );
    }

    public void retractTuples(TupleKey key,
                              WorkingMemory workingMemory) 
    {
        Agenda agenda = workingMemory.getAgenda();

        agenda.removeFromAgenda( key,
                                 getRule() );
    }

    public void modifyTuples(TupleSource tupleSource,
                             Object trigger,
                             TupleSet newTuples,
                             WorkingMemory workingMemory)
    {
        Agenda agenda = workingMemory.getAgenda();
        
        agenda.modifyAgenda( trigger,
                             newTuples,
                             getRule(),
                             getPriority() );
    }
}
