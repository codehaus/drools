package org.drools.io;

/*
 * $Id: ExtractionHandler.java,v 1.3 2004-11-26 13:12:10 mproctor Exp $
 *
 * Copyright 2001-2003 (C) The Werken Company. All Rights Reserved.
 *
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. The name "drools" must not be used to endorse or promote products derived
 * from this Software without prior written permission of The Werken Company.
 * For written permission, please contact bob@werken.com.
 *
 * 4. Products derived from this Software may not be called "drools" nor may
 * "drools" appear in their names without prior written permission of The Werken
 * Company. "drools" is a trademark of The Werken Company.
 *
 * 5. Due credit should be given to The Werken Company. (http://werken.com/)
 *
 * THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE WERKEN COMPANY OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */
import org.drools.rule.Declaration;
import org.drools.rule.Extraction;
import org.drools.rule.InvalidRuleException;
import org.drools.rule.Rule;
import org.drools.smf.Configuration;
import org.drools.smf.ExtractorFactory;
import org.drools.smf.FactoryException;
import org.drools.smf.SemanticModule;
import org.drools.spi.Extractor;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * @author mproctor
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
class ExtractionHandler extends BaseAbstractHandler
    implements
    Handler
{
    ExtractionHandler(RuleSetReader ruleSetReader)
    {
        this.ruleSetReader = ruleSetReader;

        if ( this.validParents == null && validPeers == null )
        {
            this.validParents = new HashSet( );
            this.validParents.add( Rule.class );

            this.validPeers = new HashSet( );
            this.validPeers.add( Declaration.class );
            this.validPeers.add( Extraction.class );

            this.allowNesting = false;
        }
    }

    public Set getValidParents()
    {
        return this.validParents;
    }

    public Set getValidPeers()
    {
        return this.validPeers;
    }

    public boolean allowNesting()
    {
        return this.allowNesting;
    }

    public Object start(String uri,
                        String localName,
                        Attributes attrs) throws SAXException
    {
        Rule rule = (Rule) ruleSetReader.getParent( Rule.class );

        String targetDeclName = attrs.getValue( "target" );

        if ( targetDeclName == null || targetDeclName.trim( ).equals( "" ) )
        {
            throw new SAXParseException( "extraction requires a 'target' attribute",
                                         ruleSetReader.getLocator( ) );
        }

        Declaration targetDecl = rule.getDeclaration( targetDeclName.trim( ) );

        if ( targetDecl == null )
        {
            throw new SAXParseException( "'" + targetDeclName + "' is not a valid declaration",
                                         ruleSetReader.getLocator( ) );
        }

        Extraction extraction;
        try
        {
            extraction = rule.addExtraction( targetDecl.getIdentifier( ),
                                             null );
        }
        catch ( InvalidRuleException e )
        {
            throw new SAXParseException( "extraction has an illegal 'target' attribute",
                                         ruleSetReader.getLocator( ) );
        }

        ruleSetReader.startConfiguration( localName,
                                          attrs );
        return extraction;
    }

    public Object end(String uri,
                      String localName) throws SAXException
    {
        Configuration config = ruleSetReader.endConfiguration( );
        SemanticModule module = ruleSetReader.lookupSemanticModule( uri,
                                                                    localName );
        ExtractorFactory factory = module.getExtractorFactory( localName );

        Rule rule = (Rule) ruleSetReader.getParent( Rule.class );

        Extractor extractor;
        try
        {
            extractor = factory.newExtractor( config,
                                              rule );
            Extraction extraction = (Extraction) ruleSetReader.getCurrent( );

            extraction.setExtractor( extractor );
        }
        catch ( FactoryException e )
        {
            throw new SAXParseException( "error constructing extractor",
                                         ruleSetReader.getLocator( ),
                                         e );
        }
        return null;
    }

    public Class generateNodeFor()
    {
        return Extraction.class;
    }
}
