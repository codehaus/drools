
package org.drools;

import org.drools.reteoo.JoinMemory;
import org.drools.reteoo.JoinNode;
import org.drools.reteoo.Agenda;

import java.util.Map;
import java.util.HashMap;

/** A knowledge session for a {@link RuleBase}.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class WorkingMemory
{
    /** The <code>RuleBase</code> with which this memory is associated. */
    private RuleBase ruleBase;

    /** The actual memory for the {@link JoinNode}s. */
    private Map      joinMemories;

    /** Rule-firing agenda. */
    private Agenda   agenda;

    /** Flag to determine if a rule is currently being fired. */
    private boolean firing;

    /** Construct a new working memory for a ruleBase.
     */
    protected WorkingMemory(RuleBase ruleBase)
    {
        this.ruleBase     = ruleBase;
        this.joinMemories = new HashMap();

        this.agenda       = new Agenda( this );
    }

    /** Produce output suitable for debugging.
     */
    public String toString()
    {
        return "[WorkingMemory: " + this.joinMemories + "]";
    }

    /** Retrieve the rule-firing <code>Agenda</code> for
     *  this <code>WorkingMemory</code>.
     *
     *  @return The <code>Agenda</code>.
     */
    public Agenda getAgenda()
    {
        return this.agenda;
    }

    /** Retrieve the <code>RuleBase</code>
     *  of this working memory.
     *
     *  @return The <code>RuleBase</code>.
     */
    public RuleBase getRuleBase()
    {
        return this.ruleBase;
    }

    /** Assert a new fact object into this working memory.
     *
     *  @param object The object to assert.
     *
     *  @throws AssertionException if an error occurs during assertion.
     */
    synchronized public void assertObject(Object object) throws AssertionException
    {
        getRuleBase().assertObject( object,
                                    this );

        fireAgenda();
    }

    private void fireAgenda() throws AssertionException
    {
        Agenda agenda = getAgenda();
        
        // If we're already firing a rule, then it'll pick up
        // the firing for any other assertObject(..) that get
        // nested inside, avoiding concurrent-modification
        // exceptions, depending on code paths of the actions.

        if ( ! this.firing )
        {
            try
            {
                this.firing = true;
                
                while ( ! agenda.isEmpty() )
                {
                    getAgenda().fireNextItem();
                }
            }
            finally
            {
                this.firing = false;
            }
        }
    }

    /** Retract a fact object from this working memory.
     *
     *  @param object The object to retract.
     *
     *  @throws RetractionException if an error occurs during retraction.
     */
    synchronized public void retractObject(Object object) throws RetractionException
    {
        getRuleBase().retractObject( object,
                                     this );
    }

    /** Modify a fact object in this working memory.
     *
     *  With the exception of time-based nodes, modification of
     *  a fact object is semantically equivelent to retracting and
     *  re-asserting it.
     *
     *  @param object The object to modify.
     *
     *  @throws FactException if an error occurs during modification.
     */
    synchronized public void modifyObject(Object object) throws FactException
    {
        getRuleBase().modifyObject( object,
                                    this );

        fireAgenda();
    }

    /** Retrieve the {@link JoinMemory} for a particular {@link JoinNode}.
     *
     *  @param node The <code>JoinNode</code> key.
     *
     *  @return The node's memory.
     */
    public JoinMemory getJoinMemory(JoinNode node)
    {
        JoinMemory memory = (JoinMemory) this.joinMemories.get( node );

        if ( memory == null )
        {
            memory = new JoinMemory( node );
            this.joinMemories.put( node,
                                   memory );
        }

        return memory;
    }
}