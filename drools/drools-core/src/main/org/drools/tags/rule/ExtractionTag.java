package org.drools.tags.rule;

/*
 $Id: ExtractionTag.java,v 1.2 2002-08-19 21:00:13 bob Exp $

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
import org.drools.rule.Extraction;
import org.drools.rule.Rule;
import org.drools.spi.Extractor;

import org.apache.commons.jelly.XMLOutput;
import org.apache.commons.jelly.JellyException;

/** Construct an <code>Extraction</code> for a <code>Rule</code>.
 *
 *  @see Extraction
 * 
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id: ExtractionTag.java,v 1.2 2002-08-19 21:00:13 bob Exp $
 */
public class ExtractionTag extends RuleTagSupport
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Target of the extraction. */
    private String target;

    /** The semantic extractor. */
    private Extractor extractor;

    private String var;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    public ExtractionTag()
    {
        this.target        = null;
        this.extractor = null;
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Set the <code>Extractor</code>.
     *
     *  @param extractor The extractor.
     */
    protected void setExtractor(Extractor extractor)
    {
        this.extractor = extractor;
    }

    /** Retrieve the <code>Extractor</code>.
     *
     *  @return The extractor.
     */
    public Extractor getExtractor()
    {
        return this.extractor;
    }

    /** Set the target of the extraction.
     *
     *  @param target The target variable name.
     */
    public void setTarget(String target)
    {
        this.target = target;
    }

    /** Retrieve the target of the extraction.
     *
     *  @return The target variable name.
     */
    public String getTarget()
    {
        return this.target;
    }

    public void setVar(String var)
    {
        this.var = var;
    }

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
     *  @throws Exception If an error occurs while attempting
     *          to perform this tag.
     */
    public void doTag(XMLOutput output) throws Exception
    {
        requiredAttribute( "target",
                           this.target );

        Rule rule = getRule();

        if ( rule == null )
        {
            throw new JellyException( "No rule available" );
        }

        Declaration decl = rule.getDeclaration( this.target );

        if ( decl == null )
        {
            throw new JellyException( "Unknown declaration: " + this.target );
        }

        invokeBody( output );

        if ( this.extractor == null )
        {
            throw new JellyException( "Extractor expected" );
        }

        Extraction extraction = new Extraction( decl,
                                                this.extractor );

        if ( this.var != null )
        {
            getContext().setVariable( this.var,
                                      extraction );
        }

        getContext().setVariable( "org.drools.extraction",
                                  extraction );

        rule.addExtraction( extraction );
    }
}
