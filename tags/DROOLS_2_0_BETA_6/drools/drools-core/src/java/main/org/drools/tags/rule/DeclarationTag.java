package org.drools.tags.rule;

/*
 $Id: DeclarationTag.java,v 1.4 2002-08-20 05:06:24 bob Exp $

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
import org.drools.spi.ObjectType;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.JellyException;

/** Construct a <code>Declaration</code> for a <code>Rule</code>.
 *
 *  @see Declaration
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id: DeclarationTag.java,v 1.4 2002-08-20 05:06:24 bob Exp $
 */
public class DeclarationTag extends RuleTagSupport
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** The identifier. */
    private String identifier;

    /** The semantic type. */
    private ObjectType objectType;

    /** The variable. */
    private String var;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    public DeclarationTag()
    {
        this.identifier = null;
    }

    /** Set the identifier.
     *
     *  @param identifier The identifier.
     */
    public void setIdentifier(String identifier)
    {
        this.identifier = identifier;
    }

    /** Retrieve the identifier.
     *
     *  @return The identifier.
     */
    public String getIdentifier()
    {
        return this.identifier;
    }

    /** Set the <code>ObjectType</code>
     *
     *  @param objectType The object type.
     */
    protected void setObjectType(ObjectType objectType)
    {
        this.objectType = objectType;
    }

    /** Set the variable in which to store the <code>Declaration</code>.
     *
     *  @param var The variable name.
     */
    public void setVar(String var)
    {
        this.var = var;
    }

    /** Retrieve the variable in which to store the <code>Declaration</code>.
     *
     *  @return The variable name.
     */
    public String getVar()
    {
        return this.var;
    } 

    /** Verify required attributes.
     *
     *  @throws MissingAttributeException If a required
     *          attribute is not present.
     */
    protected void verifyAttributes() throws MissingAttributeException
    {
        requiredAttribute( "identifier",
                           this.identifier );
    }

    /** Create the <code>Declaration</code>.
     *
     *  @param output The output sink.
     *
     *  @return The configured declaration.
     *
     *  @throws Exception If an error occurs while attempting  to
     *          configure the declaration.
     */
    protected Declaration createDeclaration(XMLOutput output) throws Exception
    {
        invokeBody( output );

        if ( this.objectType == null )
        {
            throw new JellyException( "No object type specified" );
        }

        Declaration decl = new Declaration( this.objectType,
                                            getIdentifier() );

        return decl;
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
        verifyAttributes();

        Declaration decl = createDeclaration( output );


        if ( this.var != null )
        {
            getContext().setVariable( this.var,
                                      decl );
        }

        getContext().setVariable( "org.drools.declaration",
                                  decl );

        Rule rule = getRule();

        if ( rule != null )
        {
            rule.addDeclaration( decl );
        }
    }
}
