package org.drools.reteoo;

import org.drools.WorkingMemory;
import org.drools.FactHandle;

import org.drools.rule.Rule;
import org.drools.spi.Activation;
import org.drools.spi.Tuple;
import org.drools.spi.ConsequenceException;

/** Item entry in the <code>Agenda</code>.
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
class AgendaItem
    implements Activation
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** The tuple. */
    private ReteTuple tuple;

    /** The rule. */
    private Rule      rule;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------
    
    /** Construct.
     *
     *  @param tuple The tuple.
     *  @param rule The rule.
     */
    AgendaItem(ReteTuple tuple,
               Rule rule)
    {
        this.tuple    = tuple;
        this.rule     = rule;
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------
    
    /** Retrieve the rule.
     *
     *  @return The rule.
     */
    public Rule getRule()
    {
        return this.rule;
    }

    /** Determine if this tuple depends on the values 
     *  derrived from a particular root object.
     *
     *  @param object The root object handle.
     *
     *  @return <code>true<code> if this agenda item depends
     *          upon the item, otherwise <code>false</code>.
     */
    boolean dependsOn(FactHandle handle)
    {
        return this.tuple.dependsOn( handle );
    }

    /** Set the tuple.
     *
     *  @param tuple The tuple.
     */
    void setTuple(ReteTuple tuple)
    {
        this.tuple = tuple;
    }

    /** Retrieve the tuple.
     *
     *  @return The tuple.
     */
    public Tuple getTuple()
    {
        return this.tuple;
    }

    TupleKey getKey()
    {
        return this.tuple.getKey();
    }
    
    /** Fire this item.
     *
     *  @param workingMemory The working memory context.
     *
     *  @throws ConsequenceException If an error occurs while
     *          attempting to fire the consequence.
     */
    void fire(WorkingMemory workingMemory) throws ConsequenceException
    {
        getRule().getConsequence().invoke( getTuple(),
                                           workingMemory );
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     java.lang.Object;
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Retrieve a debug string.
     *
     *  @return The debug string.
     */
    public String toString()
    {
        return "[" + getTuple() + "; " + getRule().getName() + "]";
    }
}
