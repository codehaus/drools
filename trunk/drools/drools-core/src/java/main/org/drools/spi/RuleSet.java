
package org.drools.spi;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.Collections;

/** Collection of related {@link Rule}s.
 *
 *  @author <a href="mail:bob@werken.com">bob@werken.com</a>
 */
public class RuleSet
{
    /** The name of the ruleset. */
    private String name;

    /** Collection of {@link Rule}s indexed by name. */
    private Map rules;

    /** Construct.
     *
     *  @param name The name of this <code>RuleSet</code>.
     */
    public RuleSet(String name)
    {
        this.name  = name;
        this.rules = Collections.EMPTY_MAP;
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
        if ( ! rule.isValid() )
        {
            throw new InvalidRuleException( rule );
        }

        if ( this.rules == Collections.EMPTY_MAP )
        {
            this.rules = new HashMap();
        }

        String name = rule.getName();

        if ( containsRule( name ) )
        {
            throw new DuplicateRuleNameException( this,
                                                  getRule( name ),
                                                  rule );
        }

        this.rules.put( name,
                        rule );
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
        return (Rule) this.rules.get( name );
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
        return this.rules.containsKey( name );
    }

    /** Retrieve a <code>Collection</code> of all <code>Rules</code>
     *  in this <code>RuleSet</code>.
     *
     *  @return A <code>Collection</code> of all <code>Rules</code>
     *          in this <code>RuleSet</code>.
     */
    public Collection getRules()
    {
        return this.rules.values();
    }
}
