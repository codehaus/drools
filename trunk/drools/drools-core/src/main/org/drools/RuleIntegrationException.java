package org.drools;

import org.drools.rule.Rule;

/** Indicates an error integrating a <code>Rule</code> or <code>RuleSet</code>
 *  into a <code>RuleBase</code>.
 *
 *  @see RuleBase#addRule
 *  @see RuleBase#addRuleSet
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id: RuleIntegrationException.java,v 1.4 2003-10-28 18:57:15 bob Exp $
 */
public class RuleIntegrationException extends DroolsException
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** The rule. */
    private Rule rule;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     *
     *  @param rule The offending rule.
     */
    public RuleIntegrationException(Rule rule)
    {
        this.rule = rule;
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Retrieve the <code>Rule</code>.
     *
     *  @return The rule.
     */
    public Rule getRule()
    {
        return this.rule;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     java.lang.Throwable
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Retrieve the error message.
     *
     *  @return The erroe message.
     */
    public String getMessage()
    {
        return getRule().getName() + " cannot be integrated";
    }
}
