
package org.drools.spi;

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.Iterator;

/** Collection of related {@link Rule}s.
 *
 *  @author <a href="mail:bob@werken.com">bob@werken.com</a>
 */
public class RuleSet
{
    /** The name of the ruleset. */
    private String name;

    /** Set of all rule-names in this <code>RuleSet</code>. */
    private Set names;

    /** Ordered list of all <code>Rules</code> in this <code>RuleSet</code>. */
    private List rules;

    /** Construct.
     *
     *  @param name The name of this <code>RuleSet</code>.
     */
    public RuleSet(String name)
    {
        this.name  = name;
        this.names = new HashSet();
        this.rules = new ArrayList();
    }

    /** Set the name of this <code>RuleSet</code>
     *
     *  @param name The name of this <code>RuleSet</code>
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /** Retrieve the name of this <code>RuleSet</code>.
     *
     *  @return The name of this <code>RuleSet</code>.
     */
    public String getName()
    {
        return this.name;
    }

    /** Add a <code>Rule</code> to this <code>RuleSet</code>.
     *
     *  @param rule The rule to add.
     *
     *  @throws DuplicateRuleNameException If the <code>Rule</code> attempting to be
     *          added has the same name as another previously added <code>Rule</code>.
     *  @throws InvalidRuleException If the <code>Rule</code> is not valid.
     */
    public void addRule(Rule rule) throws DuplicateRuleNameException, InvalidRuleException
    {
        rule.checkValidity();

        String name = rule.getName();

        if ( containsRule( name ) )
        {
            throw new DuplicateRuleNameException( this,
                                                  getRule( name ),
                                                  rule );
        }

        this.names.add( name );
        this.rules.add( rule );
    }

    /** Retrieve a <code>Rule</code> by name.
     *
     *  @param name The name of the <code>Rule</code> to retrieve.
     *
     *  @return The named <code>Rule</code>, or <code>null</code> if not
     *          such <code>Rule</code> has been added to this <code>RuleSet</code>.
     */
    public Rule getRule(String name)
    {
        Iterator ruleIter = getRuleIterator();
        Rule     eachRule = null;

        while ( ruleIter.hasNext() )
        {
            eachRule = (Rule) ruleIter.next();

            if ( eachRule.getName().equals( name ) )
            {
                return eachRule;
            }
        }

        return null;
    }

    /** Determine if this <code>RuleSet</code> contains a <code>Rule</code
     *  with the specified name.
     *
     *  @param name The name of the <code>Rule</code>.
     *
     *  @return <code>true</code> if this <code>RuleSet</code> contains a
     *          <code>Rule</code> with the specified name, else <code>false</code>.
     */
    public boolean containsRule(String name)
    {
        return this.names.contains( name );
    }

    /** Retrieve a <code>List</code> of all <code>Rules</code>
     *  in this <code>RuleSet</code>.
     *
     *  @return A <code>List</code> of all <code>Rules</code>
     *          in this <code>RuleSet</code>.
     */
    public List getRules()
    {
        return this.rules;
    }

    /** Retrieve an <code>Iterator</code> of all <code>Rules</code>
     *  in this <code>RuleSet</code>.
     *
     *  @return A <code>Iterator</code> over all <code>Rules</code>
     *          in this <code>RuleSet</code>.
     */
    public Iterator getRuleIterator()
    {
        return getRules().iterator();
    }
}
