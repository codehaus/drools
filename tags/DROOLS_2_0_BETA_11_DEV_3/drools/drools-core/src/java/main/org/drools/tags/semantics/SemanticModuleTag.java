package org.drools.tags.semantics;

/*
 $Id: SemanticModuleTag.java,v 1.4 2003-03-25 19:47:32 tdiesler Exp $

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
import org.drools.smf.SimpleSemanticModule;

/** Defines a <code>SemanticModule</code>.
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
public class SemanticModuleTag extends SemanticsTagSupport
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** The module. */
    private SimpleSemanticModule module;

    /** The uri. */
    private String uri;

    /** Variable to deposit resulting module into. */
    private String var;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    public SemanticModuleTag()
    {
        // intentionally left blank.
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Retrieve the <code>SemanticModule</code>.
     *
     *  @return The semantic module.
     */
    public SimpleSemanticModule getSemanticModule()
    {
        return this.module;
    }

    /** Set the URI.
     *
     *  @param uri The URI.
     */
    public void setUri(String uri)
    {
        this.uri = uri;
    }

    /** Retrieve the URI.
     *
     *  @return The URI.
     */
    public String getUri()
    {
        return this.uri;
    }

    /** Set the variable in which to store the resulting
     *  <code>SemanticModule</code>.
     *
     *  <p>
     *  In addition to any variable specified using
     *  this method, the variable <code>org.drools.semantic-module</code>
     *  will also hold the new <code>SemanticModule</code>.
     *  </p>
     *
     *  @param var The variable in which to store the new semantic module.
     */
    public void setVar(String var)
    {
        this.var = var;
    }

    /** Retrieve the variable in which to store the
     *  resulting <code>SemanticModule</code>.
     *
     *  @return The variable name.
     */
    public String getVar()
    {
        return this.var;
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
        if ( getUri() == null )
        {
            throw new MissingAttributeException( "uri" );
        }

        this.module = new SimpleSemanticModule( getUri() );

        if ( getVar() != null )
        {
            getContext().setVariable( getVar(),
                                      this.module );
        }

        getContext().setVariable( "org.drools.semantic-module",
                                  this.module );

        invokeBody( output );

        this.module = null;
        this.uri    = null;
    }
}

