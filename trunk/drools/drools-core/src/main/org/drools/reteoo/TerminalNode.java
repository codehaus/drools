package org.drools.reteoo;

import org.drools.FactHandle;
import org.drools.AssertionException;
import org.drools.rule.Rule;

import java.io.Serializable;

/** Leaf Rete-OO node responsible for enacting <code>Action</code>s
 *  on a matched <code>Rule</code>.
 *
 *  @see org.drools.rule.Rule
 *  @see org.drools.spi.Action
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
class TerminalNode
    implements TupleSink, Serializable
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** The rule to invoke upon match. */
    private Rule rule;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     *
     *  @param inputSource The parent tuple source.
     *  @param rule The rule.
     */
    TerminalNode(TupleSource inputSource,
                 Rule rule)
    {
        this.rule = rule;

        inputSource.setTupleSink( this );
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

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

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //     org.drools.impl.TupleSink
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /** Assert a new <code>Tuple</code>.
     *
     *  @param tuple The <code>Tuple</code> being asserted.
     *  @param workingMemory The working memory seesion.
     *
     *  @throws AssertionException If an error occurs while asserting.
     */
    public void assertTuple(ReteTuple tuple,
                            WorkingMemoryImpl workingMemory) throws AssertionException
    {
        Agenda agenda = workingMemory.getAgenda();

        agenda.addToAgenda( tuple,
                            getRule() );
    }

    /** Retract tuples.
     *
     *  @param key The tuple key.
     *  @param workingMemory The working memory seesion.
     */
    public void retractTuples(TupleKey key,
                              WorkingMemoryImpl workingMemory)
    {
        Agenda agenda = workingMemory.getAgenda();

        agenda.removeFromAgenda( key,
                                 getRule() );
    }

    /** Modify tuples.
     *
     *  @param trigger The root fact object handle.
     *  @param newTuples Modification replacement tuples.
     *  @param workingMemory The working memory session.
     */
    public void modifyTuples(FactHandle trigger,
                             TupleSet newTuples,
                             WorkingMemoryImpl workingMemory)
    {
        Agenda agenda = workingMemory.getAgenda();

        agenda.modifyAgenda( trigger,
                             newTuples,
                             getRule() );
    }
}
