package org.drools.tags.rule;

/*
 $Id: SemanticsTag.java,v 1.3 2002-08-19 21:00:13 bob Exp $

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

import org.drools.smf.SemanticModule;
import org.drools.io.SemanticsLoader;

import org.apache.commons.jelly.TagSupport;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.JellyException;

/** Load semantics.
 *
 *  @see SemanticModule
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id: SemanticsTag.java,v 1.3 2002-08-19 21:00:13 bob Exp $
 */
public class SemanticsTag extends TagSupport
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** The module id. */
    private String module;

    /** The variable. */
    private String var;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    public SemanticsTag()
    {
        super( true );
        this.module = null;
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Set the module id to load.
     *
     *  @param module The module id.
     */
    public void setModule(String module)
    {
        this.module = module;
    }

    /** Retrieve the module id.
     *
     *  @return The module id.
     */
    public String getModule()
    {
        return this.module;
    }

    /** Set the variable in which to store the <code>SemanticModule</code>.
     *
     *  @param var The variable name.
     */
    public void setVar(String var)
    {
        this.var = var;
    }

    /** Retrieve the variable in which to store the <code>SemanticModule</code>.
     *
     *  @return The variable name.
     */
    public String getVar()
    {
        return this.var;
    }

    /** Retrieve the <code>SemanticModule</code>.
     *
     *  @return The semantic module.
     *
     *  @throws Exception If an error occurs while attempting
     *          to load the semantic module.
     */
    protected SemanticModule getSemanticModule() throws Exception
    {
        SemanticsLoader loader = new SemanticsLoader();

        return loader.load( getModule() );
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
        if ( this.module == null
             ||
             this.module.trim().equals( "" ) )
        {
            throw new MissingAttributeException( "module" );
        }

        SemanticModule semanticModule = getSemanticModule();

        if ( semanticModule == null )
        {
            throw new JellyException( "Unknown semantic module: " + this.module );
        }

        if ( this.var != null )
        {
            getContext().setVariable( this.var,
                                      semanticModule );
        }

        getContext().registerTagLibrary( semanticModule.getUri(),
                                         new SemanticsTagLibrary( semanticModule ) );
    }
}
