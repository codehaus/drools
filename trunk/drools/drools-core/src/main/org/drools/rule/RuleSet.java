package org.drools.rule;

/*
 $Id: RuleSet.java,v 1.8 2004-06-25 01:57:26 mproctor Exp $

 Copyright 2001-2003 (C) The Werken Company. All Rights Reserved.

 Redistribution and use of this software and associated documentation
 ("Software"), with or without modification, are permitted provided
 that the following conditions are met:

 1. Redistributions of source code must retain copyright
    statements and notices.  Redistributions must also contain a
    copy of this document.

 2. Redistributions in binary form must reproduce the
    above copyright notice, this list of conditions and the
    following disclaimer in the documentation and/or other
    materials provided with the distribution.

 3. The name "drools" must not be used to endorse or promote
    products derived from this Software without prior written
    permission of The Werken Company.  For written permission,
    please contact bob@werken.com.

 4. Products derived from this Software may not be called "drools"
    nor may "drools" appear in their names without prior written
    permission of The Werken Company. "drools" is a trademark of
    The Werken Company.

 5. Due credit should be given to The Werken Company.
    (http://werken.com/)

 THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE WERKEN COMPANY OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.

 */

import java.util.List;
import java.util.ArrayList;
import java.util.Set;
import java.util.HashSet;

/** Collection of related <code>Rule</code>s.
 *
 *  @see Rule
 *
 *  @author <a href="mail:bob@werken.com">bob mcwhirter</a>
 *
 *  @version $Id: RuleSet.java,v 1.8 2004-06-25 01:57:26 mproctor Exp $
 */
public class RuleSet
{
    // ------------------------------------------------------------
    //     Constants
    // ------------------------------------------------------------

    /** Empty <code>RuleSet</code> array. */
    public static final RuleSet[] EMPTY_ARRAY = new RuleSet[0];

    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Name of the ruleset. */
    private String name;

    /** Documentation. */
    private String documentation;

    /** Set of all rule-names in this <code>RuleSet</code>. */
    private Set ruleNames;

    /** Ordered list of all <code>Rules</code> in this <code>RuleSet</code>. */
    private List rules;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     *
     *  @param name The name of this <code>RuleSet</code>.
     */
    public RuleSet(String name)
    {
        this.name  = name;
        this.ruleNames = new HashSet();
        this.rules = new ArrayList();
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Retrieve the name of this <code>RuleSet</code>.
     *
     *  @return The name of this <code>RuleSet</code>.
     */
    public String getName()
    {
        return this.name;
    }

    /** Set the documentation.
     *
     *  @param documentation The documentation.
     */
    public void setDocumentation(String documentation)
    {
        this.documentation = documentation;
    }

    /** Retrieve the documentation.
     *
     *  @return The documentation or <code>null</code> if none.
     */
    public String getDocumentation()
    {
        return this.documentation;
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

        this.ruleNames.add( name );
        rule.setLoadOrder(rules.size());
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
        Rule[] rules = getRules();

        for ( int i = 0 ; i < rules.length ; ++i )
        {
            if ( rules[ i ].getName().equals( name ) )
            {
                return rules[ i ];
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
        return this.ruleNames.contains( name );
    }

    /** Retrieve all <code>Rules</code> in this <code>RuleSet</code>.
     *
     *  @return An array of all <code>Rules</code>
     *          in this <code>RuleSet</code>.
     */
    public Rule[] getRules()
    {
        return (Rule[]) this.rules.toArray( Rule.EMPTY_ARRAY );
    }
}
