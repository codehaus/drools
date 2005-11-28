/*
 * $Id: RuleHandler.java,v 1.6 2005-11-07 21:34:08 mproctor Exp $
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
package org.drools.io;

import java.util.HashSet;

import org.drools.rule.ApplicationData;
import org.drools.rule.Rule;
import org.drools.rule.RuleConstructionException;
import org.drools.rule.RuleSet;
import org.drools.smf.Configuration;
import org.drools.smf.FactoryException;
import org.drools.smf.RuleFactory;
import org.drools.smf.SemanticModule;
import org.drools.spi.Functions;
import org.drools.spi.ImportEntry;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * @author mproctor
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
class RuleHandler extends BaseAbstractHandler
    implements
    Handler
{
    RuleHandler(RuleSetReader ruleSetReader)
    {
        this.ruleSetReader = ruleSetReader;

        if ( (this.validParents == null) && (this.validPeers == null) )
        {
            this.validParents = new HashSet( );
            this.validParents.add( RuleSet.class );

            this.validPeers = new HashSet( );
            this.validPeers.add( null );
            this.validPeers.add( Rule.class );
            this.validPeers.add( ImportEntry.class );
            this.validPeers.add( ApplicationData.class );
            this.validPeers.add( Functions.class );

            this.allowNesting = false;
        }
    }

    public Object start(String uri,
                        String localName,
                        Attributes attrs) throws SAXException
    {

        SemanticModule module = this.ruleSetReader.lookupSemanticModule( uri,
                                                                         localName );

        RuleFactory factory = module.getRuleFactory( localName );

        this.ruleSetReader.startConfiguration( localName,
                                               attrs );

        Configuration config = this.ruleSetReader.endConfiguration( );

        Rule rule;
        try
        {
            rule = factory.newRule( this.ruleSetReader.getRuleSet( ),
                                    this.ruleSetReader.getFactoryContext( ),
                                    config );

            startRule( rule,
                       attrs );
        }
        catch ( FactoryException e )
        {
            throw new SAXParseException( "error constructing rule",
                                         this.ruleSetReader.getLocator( ),
                                         e );
        }
        return rule;
    }

    private void startRule(Rule rule,
                           Attributes attrs) throws SAXException
    {
        String salienceStr = attrs.getValue( "salience" );
        String noLoopStr = attrs.getValue( "no-loop" );
        String xorGroup = attrs.getValue( "xor-group" );
        String ruleDesc = attrs.getValue( "description" );

        if ( !(salienceStr == null || salienceStr.trim( ).equals( "" )) )
        {
            try
            {
                int salience = Integer.parseInt( salienceStr.trim( ) );

                rule.setSalience( salience );
            }
            catch ( NumberFormatException e )
            {
                throw new SAXParseException( "invalid number value for 'salience' attribute: " + salienceStr.trim( ),
                                             this.ruleSetReader.getLocator( ) );
            }
        }

        if ( !(noLoopStr == null || noLoopStr.trim( ).equals( "" )) )
        {
            try
            {
                boolean noLoop = new Boolean( noLoopStr.trim( ) ).booleanValue( );
                rule.setNoLoop( noLoop );
            }
            catch ( NumberFormatException e )
            {
                throw new SAXParseException( "invalid boolean value for 'no-loop' attribute: " + salienceStr.trim( ),
                                             this.ruleSetReader.getLocator( ) );
            }
        }
        
        if ( !(xorGroup == null || xorGroup.trim( ).equals( "" )) )
        {
            rule.setXorGroup( xorGroup );
        }        

        if ( !(ruleDesc == null || ruleDesc.trim( ).equals( "" )) )
        {
            rule.setDocumentation( ruleDesc );
        }

        rule.setImporter( this.ruleSetReader.getRuleSet( ).getImporter( ) );
        rule.setApplicationData( this.ruleSetReader.getRuleSet( ).getApplicationData( ) );
    }

    public Object end(String uri,
                      String localName) throws SAXException
    {
        try
        {
            this.ruleSetReader.getRuleSet( ).addRule( (Rule) this.ruleSetReader.getParents( ).getLast( ) );
        }
        catch ( RuleConstructionException e )
        {
            throw new SAXParseException( "error adding rule",
                                         this.ruleSetReader.getLocator( ),
                                         e );
        }

        return null;

    }

    public Class generateNodeFor()
    {
        return Rule.class;
    }

}
