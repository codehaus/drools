package org.drools.tags.rule;

/*
 $Id: RuleSetTag.java,v 1.4 2002-08-20 21:19:55 bob Exp $

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

import org.drools.io.RuleSetLoader;
import org.drools.rule.RuleSet;
import org.drools.tags.knowledge.RuleBaseTag;

import org.apache.commons.jelly.XMLOutput;

/** Construct a <code>RuleSet</code>.
 *
 *  @see RuleSet
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id: RuleSetTag.java,v 1.4 2002-08-20 21:19:55 bob Exp $
 */
public class RuleSetTag extends RuleTagSupport
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Rule-set name. */
    private String name;

    /** The rule-set. */
    private RuleSet ruleSet;

    /** Var name. */
    private String var;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    public RuleSetTag()
    {
        // intentionally left blank
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Set the <code>RuleSet</code> name.
     *
     *  @param name The name.
     */
    public void setName(String name)
    {
        this.name = name;
    }

    /** Retrieve the <code>RuleSet</code> name.
     *
     *  @return The name.
     */
    public String getName()
    {
        return this.name;
    }

    /** Set the variable in which to store the <code>RuleSet</code>.
     *
     *  @param var The variable name.
     */
    public void setVar(String var)
    {
        this.var = var;
    }

    /** Retrieve the variable in which to store the <code>RuleSet</code>.
     *
     *  @return The variable name.
     */
    public String getVar()
    {
        return this.var;
    }

    /** Retrieve the <code>RuleSet</code>.
     *
     *  @return The rule-set.
     */
    public RuleSet getRuleSet()
    {
        return this.ruleSet;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     org.apache.commons.jelly.Tag
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Perform this tag.
     *
     *  @param output The output sink.
     *
     *  @throws Exception If an error occurs while attempting
     *          to perform this tag.
     */
    public void doTag(XMLOutput output) throws Exception
    {
        requiredAttribute( "name",
                           this.name );

        this.ruleSet = new RuleSet( this.name );

        if ( this.var != null )
        {
            getContext().setVariable( this.var,
                                      this.ruleSet );
        }

        getContext().setVariable( "org.drools.rule-set",
                                  this.ruleSet );

        invokeBody( output );

        RuleSetLoader loader = (RuleSetLoader) getContext().getVariable( "org.drools.io.RuleSetLoader" );

        if ( loader != null )
        {
            loader.addRuleSet( this.ruleSet );
        }

        RuleBaseTag tag = (RuleBaseTag) findAncestorWithClass( RuleBaseTag.class );

        if ( tag != null )
        {
            tag.addRuleSet( this.ruleSet );
        }
    }
}
