
package org.drools;

import org.drools.reteoo.RootNode;
import org.drools.reteoo.Builder;
import org.drools.reteoo.ReteConstructionException;
import org.drools.spi.Rule;
import org.drools.spi.RuleSet;

import java.util.Iterator;

/** Collection of {@link Rule}s.
 *
 *  @author <a href="bob@werken.com">bob@werken.com</a>
 */
public class RuleBase
{
    /** The root Rete-OO node of this <code>RuleBase</code>. */
    private RootNode rootNode;

    /** Construct.
     */
    public RuleBase()
    {
        this.rootNode = new RootNode();
    }

    /** Add a <code>RuleSet</code> of <code>Rules</code> to this <code>RuleBase</code>.
     *
     *  <p>
     *  A <code>RuleSet</code> may be added to multiple <code>RuleBases</code>.
     *  Any changes to a <code>RuleSet</code> or its component <code>Rule</code>
     *  once it has been added are ignored.
     *  </p>
     *
     *  @param ruleSet The <code>RuleSet</code> to add.
     *
     *  @throws ReteConstructionException If a member rule does not allow for
     *          complete and correct integration into the underlying Rete network.
     */
    public void addRuleSet(RuleSet ruleSet) throws ReteConstructionException
    {
        Iterator ruleIter = ruleSet.getRuleIterator();
        Rule     eachRule = null;

        while ( ruleIter.hasNext() )
        {
            eachRule = (Rule) ruleIter.next();

            addRule( eachRule );
        }
    }

    /** Add a <code>Rule</code> to this <code>RuleBase</code>.
     *
     *  <p>
     *  A <code>Rule</code> may be added to multiple <code>RuleBases</code>.
     *  Any changes to a <code>Rule</code> once it has been added are ignored.
     *  </p>
     *
     *  @param rule The rule to add.
     *
     *  @throws ReteConstructionException If the rule does not allow for
     *          complete and correct integration into the underlying Rete
     *          network.
     */
    public void addRule(Rule rule) throws ReteConstructionException
    {
        Builder builder = new Builder( getRootNode() );

        builder.addRule( rule );
    }

    /** Create a {@link WorkingMemory} session for
     *  this <code>RuleBase</code>.
     *
     *  @return A newly initialized <code>WorkingMemory</code>.
     */
    public WorkingMemory createWorkingMemory()
    {
        return new WorkingMemory( this );
    }

    /** Retrieve the Rete-OO {@link RootNode} for this
     *  <code>RuleBase</code>.
     */
    protected RootNode getRootNode()
    {
        return this.rootNode;
    }

    /** Assert a new fact object into this <code>RuleBase</code>
     *  and the specified <code>WorkingMemory</code>.
     *
     *  @param object The object to assert.
     *  @param workingMemory The working memory session.
     *
     *  @throws AssertionException if an error occurs during assertion.
     */
    protected void assertObject(Object object,
                                WorkingMemory workingMemory) throws AssertionException
    {
        getRootNode().assertObject( object,
                                    workingMemory );
    }

    /** Retract a fact object from this <code>RuleBase</code>
     *  and the specified <code>WorkingMemory</code>.
     *
     *  @param object The object to retract.
     *  @param workingMemory The working memory session.
     *
     *  @throws RetractionException if an error occurs during retraction.
     */
    protected void retractObject(Object object,
                                 WorkingMemory workingMemory) throws RetractionException
    {
        getRootNode().retractObject( object,
                                     workingMemory );
    }

    /** Modify a fact object in this <code>RuleBase</code>
     *  and the specified <code>WorkingMemory</code>.
     *
     *  With the exception of time-based nodes, modification of
     *  a fact object is semantically equivelent to retracting and
     *  re-asserting it.
     *
     *  @param object The object to modify.
     *  @param workingMemory The working memory session.
     *
     *  @throws ModificationException if an error occurs during assertion.
     */
    protected void modifyObject(Object object,
                                WorkingMemory workingMemory) throws ModificationException
    {
        getRootNode().modifyObject( object,
                                    workingMemory );
    }
}
