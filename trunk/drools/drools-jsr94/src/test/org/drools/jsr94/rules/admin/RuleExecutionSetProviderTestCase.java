package org.drools.jsr94.rules.admin;

/*
 * $Id: RuleExecutionSetProviderTestCase.java,v 1.12 2004-11-17 03:09:57 dbarnett Exp $
 *
 * Copyright 2002-2004 (C) The Werken Company. All Rights Reserved.
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
 * Company. "drools" is a registered trademark of The Werken Company.
 *
 * 5. Due credit should be given to The Werken Company.
 * (http://drools.werken.com/).
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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import javax.rules.admin.RuleAdministrator;
import javax.rules.admin.RuleExecutionSet;
import javax.rules.admin.RuleExecutionSetProvider;

import org.apache.xerces.parsers.DOMParser;
import org.drools.io.RuleSetReader;
import org.drools.jsr94.rules.RuleEngineTestBase;
import org.drools.rule.RuleSet;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * Test the RuleExecutionSetProvider implementation.
 *
 * @author N. Alex Rupp (n_alex <at>codehaus.org)
 */
public class RuleExecutionSetProviderTestCase extends RuleEngineTestBase
{
    private RuleAdministrator ruleAdministrator;

    private RuleExecutionSetProvider ruleSetProvider;

    private RuleSet ruleSet;

    /**
     * Setup the test case.
     */
    protected void setUp( ) throws Exception
    {
        super.setUp( );
        ruleAdministrator = ruleServiceProvider.getRuleAdministrator( );
        ruleSetProvider = ruleAdministrator.getRuleExecutionSetProvider( null );

        initRuleSet( );
    }

    private void initRuleSet( )
    {
        InputStream resourceAsStream = null;
        try
        {
            resourceAsStream =
                RuleEngineTestBase.class.getResourceAsStream( bindUri );
            Reader reader = new InputStreamReader( resourceAsStream );
            RuleSetReader ruleSetReader = new RuleSetReader( );
            this.ruleSet = ruleSetReader.read( reader );
        }
        catch ( IOException e )
        {
            throw new ExceptionInInitializerError(
                "setUp() could not init the " +
                "RuleSet due to an IOException in the InputStream: " + e );
        }
        catch ( Exception e )
        {
            throw new ExceptionInInitializerError(
                "setUp() could not init the RuleSet, " + e );
        }
        finally
        {
            if ( resourceAsStream != null )
            {
                try
                {
                    resourceAsStream.close( );
                }
                catch ( IOException e )
                {
                    e.printStackTrace( );
                }
            }
        }
    }

    protected void tearDown( )
    {
        ruleSet = null;
    }

    /**
     * Test createRuleExecutionSet from DOM.
     */
    public void testCreateFromElement( ) throws Exception
    {
        DOMParser parser = new DOMParser( );
        Document doc = null;
        try
        {
            parser.parse( new InputSource(
                RuleEngineTestBase.class.getResourceAsStream( bindUri ) ) );
            doc = parser.getDocument( );
        }
        catch ( SAXException e )
        {
            fail( "could not parse incoming data stream: " + e );
        }
        catch ( IOException e )
        {
            fail( "could not open incoming data stream: " + e );
        }
        Element element = null;
        NodeList children = doc.getChildNodes( );
        if ( children != null )
        {
            for ( int i = 0; i < children.getLength( ); i++ )
            {
                Node child = children.item( i );
                if ( Node.ELEMENT_NODE == child.getNodeType( ) )
                {
                    element = ( Element ) child;
                }
            }
        }

        if ( element != null )
        {
            RuleExecutionSet testRuleSet =
                ruleSetProvider.createRuleExecutionSet( element, null );
            assertEquals(
                "rule set name", "Sisters Rules", testRuleSet.getName( ) );
            assertEquals(
                "number of rules", 1, testRuleSet.getRules( ).size( ) );
        }
        else
        {
            fail( "could not build an org.w3c.dom.Element" );
        }
    }

    /**
     * Test createRuleExecutionSet from Serializable.
     */
    public void testCreateFromSerializable( ) throws Exception
    {
        RuleExecutionSet ruleExecutionSet =
            ruleSetProvider.createRuleExecutionSet( this.ruleSet, null );
        assertEquals( "rule set name", "Sisters Rules",
                      ruleExecutionSet.getName( ) );
        assertEquals(
            "number of rules", 1, ruleExecutionSet.getRules( ).size( ) );
    }

    /**
     * Test createRuleExecutionSet from URI.
     */
    public void testCreateFromURI( ) throws Exception
    {
        String rulesUri =
            RuleEngineTestBase.class.getResource( bindUri ).toExternalForm( );
        RuleExecutionSet testRuleSet =
            ruleSetProvider.createRuleExecutionSet( rulesUri, null );
        assertEquals(
            "rule set name", "Sisters Rules", testRuleSet.getName( ) );
        assertEquals( "number of rules", 1, testRuleSet.getRules( ).size( ) );
    }

    public void testIncompatibleSerializableCreation( ) throws Exception
    {
        try
        {
            RuleExecutionSet testRuleSet =
                ruleSetProvider.createRuleExecutionSet( new ArrayList( ), null );
            fail( "Should have thrown an IllegalArgumentException. ArrayList " +
                  "objects are not valid AST representations. " + testRuleSet );
        }
        catch ( IllegalArgumentException e )
        {
            /*
             * this is supposed to happen if you pass in a serializable object
             * that isn't a supported AST representation.
             */
        }
    }
}
