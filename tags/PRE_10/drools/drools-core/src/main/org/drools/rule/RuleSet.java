package org.drools.rule;

/*
 $Id: RuleSet.java,v 1.1 2002-08-01 18:47:33 bob Exp $

 Copyright 2002 (C) The Werken Company. All Rights Reserved.
 
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
    permission of The Werken Company. "drools" is a registered
    trademark of The Werken Company.
 
 5. Due credit should be given to The Werken Company.
    (http://drools.werken.com/).
 
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
import java.util.Iterator;

/** Collection of related <code>Rule</code>s.
 *
 *  @see Rule
 *
 *  @author <a href="mail:bob@eng.werken.com">bob mcwhirter</a>
 */
public class RuleSet
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** The name of the ruleset. */
    private String name;

    /** Set of all rule-names in this <code>RuleSet</code>. */
    private Set names;

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
        this.names = new HashSet();
        this.rules = new ArrayList();
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

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
