package org.drools.io;

/*
 * $Id: RuleSetHandler.java,v 1.1 2004-11-03 22:54:36 mproctor Exp $
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

import org.drools.rule.RuleSet;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.SAXParseException;

/**
 * @author mproctor
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
class RuleSetHandler extends BaseAbstractHandler implements Handler
{
    RuleSetHandler( RuleSetReader ruleSetReader )
    {
        this.ruleSetReader = ruleSetReader;
        
        if ( (this.validParents == null) && (validPeers == null) )
        {
            this.validParents = new HashSet( );
            this.validParents.add( null );

            this.validPeers = new HashSet( );
            this.validPeers.add( null );

            this.allowNesting = false;
        }
    }

    public Object start( String uri, String localName, Attributes attrs ) throws SAXException
    {

        String ruleSetName = attrs.getValue( "name" );
        String ruleSetDesc = attrs.getValue( "description" );

        if ( ruleSetName == null || ruleSetName.trim( ).equals( "" ) )
        {
            throw new SAXParseException(
                    "<rule-set> requires a 'name' attribute", ruleSetReader
                            .getLocator( ) );
        }

        RuleSet ruleSet = new RuleSet( ruleSetName.trim( ) );

        if ( ruleSetDesc == null || ruleSetDesc.trim( ).equals( "" ) )
        {
            ruleSet.setDocumentation( "" );
        }
        else
        {
            ruleSet.setDocumentation( ruleSetDesc );
        }

        ruleSetReader.setRuleSet( ruleSet );
        return ruleSet;
    }

    public Object end( String uri, String localName ) throws SAXException
    {
        return null;
    }

    public Class generateNodeFor()
    {
        return RuleSet.class;
    }
}
