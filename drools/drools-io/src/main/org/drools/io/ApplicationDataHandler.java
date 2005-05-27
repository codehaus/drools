package org.drools.io;

/*
 * $Id: ApplicationDataHandler.java,v 1.3.2.1 2005-04-30 13:49:43 mproctor Exp $
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
import java.util.HashSet;

import org.drools.rule.ApplicationData;
import org.drools.rule.RuleSet;
import org.drools.smf.ApplicationDataFactory;
import org.drools.smf.Configuration;
import org.drools.smf.FactoryException;
import org.drools.smf.SemanticModule;
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
class ApplicationDataHandler extends BaseAbstractHandler implements Handler
{
    ApplicationDataHandler( RuleSetReader ruleSetReader )
    {
        this.ruleSetReader = ruleSetReader;

        if ( (this.validParents == null) && (this.validPeers == null) )
        {
            this.validParents = new HashSet( );
            this.validParents.add( RuleSet.class );

            this.validPeers = new HashSet( );
            this.validPeers.add( null );
            this.validPeers.add( ApplicationData.class );
            this.validPeers.add( ImportEntry.class );

            this.allowNesting = false;
        }
    }

    public Object start( String uri, String localName, Attributes attrs ) throws SAXException
    {
        this.ruleSetReader.startConfiguration( localName, attrs );
        return null;
    }

    public Object end( String uri, String localName ) throws SAXException
    {
        SemanticModule module = this.ruleSetReader.lookupSemanticModule( uri,
                                                                    localName );

        ApplicationDataFactory factory = module.getApplicationDataFactory( localName );        

        Configuration config = this.ruleSetReader.endConfiguration( );
        ApplicationData applicationData;
        try
        {
            applicationData = factory.newApplicationData( this.ruleSetReader.getRuleSet( ),
                                                          this.ruleSetReader.getFactoryContext( ),
                                                          config );
            this.ruleSetReader.getRuleSet( ).addApplicationData( applicationData );

        }
        catch ( FactoryException e )
        {
            throw new SAXParseException( "error constructing import",
                    this.ruleSetReader.getLocator( ), e );
        }
        return applicationData;
    }

    public Class generateNodeFor()
    {
        return ApplicationData.class;
    }
}
