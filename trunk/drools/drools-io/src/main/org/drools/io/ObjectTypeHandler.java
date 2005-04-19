package org.drools.io;

/*
 * $Id: ObjectTypeHandler.java,v 1.5 2005-04-19 22:34:31 mproctor Exp $
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

import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.smf.Configuration;
import org.drools.smf.FactoryException;
import org.drools.smf.ObjectTypeFactory;
import org.drools.smf.SemanticModule;
import org.drools.spi.ImportEntry;
import org.drools.spi.ObjectType;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * @author mproctor
 */
class ObjectTypeHandler extends BaseAbstractHandler
    implements
    Handler
{
    ObjectTypeHandler(RuleSetReader ruleSetReader)
    {
        this.ruleSetReader = ruleSetReader;

        if ( this.validParents == null && this.validPeers == null )
        {
            this.validParents = new HashSet( );
            this.validParents.add( Declaration.class );

            this.validPeers = new HashSet( );
            this.validPeers.add( null );

            this.allowNesting = false;
        }
    }

    public Object start(String uri,
                        String localName,
                        Attributes attrs) throws SAXException
    {
        this.ruleSetReader.startConfiguration( localName,
                                               attrs );
        return null;
    }

    public Object end(String uri,
                      String localName) throws SAXException
    {
        Configuration config = this.ruleSetReader.endConfiguration( );

        SemanticModule module = this.ruleSetReader.lookupSemanticModule( uri,
                                                                         localName );

        ObjectTypeFactory factory = module.getObjectTypeFactory( localName );

        try
        {
            Rule rule = (Rule) this.ruleSetReader.getParent( Rule.class );
            Declaration declaration = (Declaration) this.ruleSetReader.getParent( Declaration.class );
            ((DefaultConfiguration) config).setAttribute( "identifier",
                                                          declaration.getIdentifier( ) );
			
            ObjectType objectType = factory.newObjectType( this.ruleSetReader.getFactoryContext( ),
                                                           config,
                                                           this.ruleSetReader.getRuleSet( ).getImports( ) );

            declaration.setObjectType( objectType );
        }
        catch ( FactoryException e )
        {
            throw new SAXParseException( "error constructing object type",
                                         this.ruleSetReader.getLocator( ),
                                         e );
        }
        return null;
    }

    public Class generateNodeFor()
    {
        return ImportEntry.class;
    }
}
