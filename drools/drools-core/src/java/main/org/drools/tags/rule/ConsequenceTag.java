package org.drools.tags.rule;

/*
 $Id: ConsequenceTag.java,v 1.6 2003-03-25 19:47:32 tdiesler Exp $

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

import org.apache.commons.jelly.JellyTagException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.XMLOutput;
import org.drools.rule.Rule;
import org.drools.spi.Consequence;

/** Construct a <code>Consequence</code> for a <code>Rule</code>.
 *
 *  @see Consequence
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id: ConsequenceTag.java,v 1.6 2003-03-25 19:47:32 tdiesler Exp $
 */
public class ConsequenceTag extends RuleTagSupport implements ConsequenceReceptor
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** The consequence. */
    private Consequence consequence;

    /** The variable. */
    private String var;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    public ConsequenceTag()
    {
        this.consequence = null;
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Set the <code>Consequence</code>.
     *
     *  @param consequence The consequence.
     */
    public void setConsequence(Consequence consequence)
    {
        this.consequence = consequence;
    }

    /** Retrieve the <code>Consequence</code>.
     *
     *  @return The consequence.
     */
    public Consequence getConsequence()
    {
        return this.consequence;
    }

    /** Set the variable in which to store the <code>Consequence</code>.
     *
     *  @param var The variable name.
     */
    public void setVar(String var)
    {
        this.var = var;
    }

    /** Retrieve the variable in which to store the <code>Consequence</code>.
     *
     *  @return The variable name.
     */
    public String getVar()
    {
        return this.var;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     org.drools.tags.rule.ConsequenceReceptor
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Receive a <code>Consequence</code>.
     *
     *  @param consequence The consequence.
     */
    public void receiveConsequence(Consequence consequence)
    {
        setConsequence( consequence );
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     org.apache.commons.jelly.Tag
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    
    /** Perform this tag.
     *
     *  @param output The output sink.
     *
     *  @throws JellyTagException If an error occurs while attempting
     *          to perform this tag.
     */
    public void doTag(XMLOutput output) throws MissingAttributeException, JellyTagException
    {
        Rule rule = getRule();

        if ( rule == null )
        {
            throw new JellyTagException( "No rule available" );
        }

        invokeBody( output );

        if ( this.consequence == null )
        {
            throw new JellyTagException( "Consequence expected" );
        }

        if ( this.var != null )
        {
            getContext().setVariable( this.var,
                                      this.consequence );
        }

        getContext().setVariable( "org.drools.consequence",
                                  this.consequence );


        rule.setConsequence( this.consequence );
    }
}
