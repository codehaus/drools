package org.drools.tags.rule;

/*
 $Id: RuleTagSupport.java,v 1.6 2002-09-27 20:55:32 bob Exp $

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

import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.rule.RuleSet;

import org.apache.commons.jelly.TagSupport;
import org.apache.commons.jelly.JellyException;
import org.apache.commons.jelly.MissingAttributeException;

/** Support for rule tags.
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id: RuleTagSupport.java,v 1.6 2002-09-27 20:55:32 bob Exp $
 */
public abstract class RuleTagSupport extends TagSupport
{
    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    protected RuleTagSupport()
    {
        super( true );
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Retrieve the current <code>RuleSet</code>.
     *
     *  @return The current rule-set or <code>null</code> if
     *          no rule-set is in scope.
     */
    protected RuleSet getRuleSet() 
    {
        RuleSetTag ruleSetTag = (RuleSetTag) findAncestorWithClass( RuleSetTag.class );

        if ( ruleSetTag == null )
        {
            return null;
        }

        return ruleSetTag.getRuleSet();
    }

    /** Retrieve the current <code>Rule<code>.
     *
     *  @return The current rule.
     */
    protected Rule getRule() 
    {
        RuleTag ruleTag = (RuleTag) findAncestorWithClass( RuleTag.class );

        if ( ruleTag == null )
        {
            return null;
        }

        return ruleTag.getRule();
    }

    /** Retrieve the array of available <code>Declaration</code>s.
     *
     *  @return The array of declarations.
     * 
     *  @throws JellyException If no declarations are currently
     *          available in scope.
     */
    public Declaration[] getAvailableDeclarations() throws JellyException
    {
        Rule rule = getRule();

        if ( rule == null )
        {
            throw new JellyException( "No rule available" );
        }

        return rule.getDeclarationsArray();
    }

    /** Check required attribute.
     *
     *  @param name Attribute name.
     *  @param value Attribute value.
     *
     *  @throws MissingAttributeException If the value is either <code>null</code>
     *          or contains only whitespace.
     */
    protected void requiredAttribute(String name,
                                     String value) throws MissingAttributeException
    {
        if ( value == null
             ||
             value.trim().equals( "" ) )
        {
            throw new MissingAttributeException( name );
        }
    }
}
