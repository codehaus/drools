package org.drools.io;

/*
 $Id: SemanticsLoader.java,v 1.4 2002-08-19 17:24:00 bob Exp $

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
import org.drools.tags.semantics.SemanticsTagLibrary;

import org.apache.commons.jelly.Script;
import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.parser.XMLParser;

import java.io.IOException;
import java.net.URL;

/** Loads <code>SemanticModule</code> definition from XML descriptor.
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
public class SemanticsLoader
{
    // ------------------------------------------------------------
    //     Constants
    // ------------------------------------------------------------

    /** Name of smf descriptor. */
    public static final String DESCRIPTOR_NAME = "semantics.xml";

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    public SemanticsLoader()
    {
        // intentionally left blank.
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Load a <code>SemanticModule</code> deifnition from a URL.
     *
     *  @param packageName The java package containing the module.
     *
     *  @return The loaded semantic module or <code>null</code> if none found.
     *
     *  @throws IOException If an IO errors occurs.
     *  @throws Exception If an error occurs evaluating the definition.
     */
    public SemanticModule load(String packageName) throws IOException, Exception
    {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        if ( cl == null )
        {
            cl = ClassLoader.getSystemClassLoader();
        }

        String moduleDescriptor = packageName.replace( '.',
                                                       '/' );
        moduleDescriptor += "/" + DESCRIPTOR_NAME;

        URL url = cl.getResource( moduleDescriptor );

        if ( url == null )
        {
            return null;
        }

        XMLParser parser = new XMLParser();

        JellyContext context = new JellyContext();

        context.registerTagLibrary( "http://drools.org/semantic-module",
                                    new SemanticsTagLibrary() );

        parser.setContext( context );

        Script script = parser.parse( url.toExternalForm() );
        
        XMLOutput output = XMLOutput.createXMLOutput( System.err );
        
        script.run( context,
                    output );

        return (SemanticModule) context.getVariable( "org.drools.semantic-module" );
    }
}     
