
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

    public Rule getRule(String name)
    {
        return (Rule) this.rules.get( name );
    }

    public boolean containsRule(String name)
    {
        return this.rules.containsKey( name );
    }

    public Collection getRules()
    {
        return this.rules.values();
    }
}
