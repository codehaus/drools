package org.drools.tags.knowledge;

/*
 $Id: LoadRulesTag.java,v 1.2 2002-08-20 21:49:32 bob Exp $

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

import org.drools.RuleBase;

import org.apache.commons.jelly.Script;
import org.apache.commons.jelly.TagSupport;
import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.JellyException;
import org.apache.commons.jelly.MissingAttributeException;
import org.apache.commons.jelly.parser.XMLParser;

import java.net.URL;

/** Load <code>Rule</code>s and <code>RuleSet</code>s
 *  into a <code>RuleBase</code>.
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id: LoadRulesTag.java,v 1.2 2002-08-20 21:49:32 bob Exp $
 */
public class LoadRulesTag extends TagSupport
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** The uri. */
    private String uri;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    public LoadRulesTag()
    {
        super( true );
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Set the URI to load.
     *
     *  @param uri The URI to load.
     */
    public void setUri(String uri)
    {
        this.uri = uri;
    }

    /** Retrieve the URI to load.
     *
     *  @return uri The URI to load.
     */
    public String getUri()
    {
        return this.uri;
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
        if ( getUri() == null )
        {
            throw new MissingAttributeException( "uri" );
        }

        URL uri = new URL( getUri() );

        RuleBaseTag tag = (RuleBaseTag) findAncestorWithClass( RuleBaseTag.class );

        if ( tag == null )
        {
            throw new JellyException( "<load-rules> may only be used within a <rule-set>" );
        }

        RuleBase ruleBase = tag.getRuleBase();

        XMLParser parser = new XMLParser();

        JellyContext context = new JellyContext( getContext(),
                                                 uri,
                                                 uri );

        context.setInherit( true );
        context.setExport( false );

        parser.setContext( context );

        try
        {
            Script script = parser.parse( uri.toExternalForm() );
            
            script.run( context,
                        output );
        }
        catch (JellyException e)
        {
            throw (JellyException) e.fillInStackTrace();

        }
        catch (Exception e)
        {
            throw new JellyException( e );
        }
    }
}
