package org.drools.jsr94.rules.admin;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

import javax.rules.admin.RuleAdministrator;
import javax.rules.admin.RuleExecutionSet;
import javax.rules.admin.RuleExecutionSetProvider;

import junit.framework.Test;
import junit.framework.TestSuite;

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
    private RuleAdministrator        ruleAdministrator;

    private RuleExecutionSetProvider ruleSetProvider;

    private RuleSet                  ruleSet;

    /**
     * Setup the test case.
     */
    protected void setUp() throws Exception
    {
        super.setUp( );
        ruleAdministrator = ruleServiceProvider.getRuleAdministrator( );
        ruleSetProvider = ruleAdministrator.getRuleExecutionSetProvider( null );

        initRuleSet( );
    }

    private void initRuleSet()
    {
        InputStream resourceAsStream = null;
        try
        {
            resourceAsStream = RuleEngineTestBase.class
                                                       .getResourceAsStream( bindUri );
            Reader reader = new InputStreamReader( resourceAsStream );
            RuleSetReader ruleSetReader = new RuleSetReader( );
            this.ruleSet = ruleSetReader.read( reader );
        }
        catch ( IOException e )
        {
            throw new ExceptionInInitializerError(
                                                   "setUp() could not init the "
                                                                                                                                                                                                            + "RuleSet due to an IOException in the InputStream: "
                                                                                                                                                                                                            + e );
        }
        catch ( Exception e )
        {
            throw new ExceptionInInitializerError(
                                                   "setUp() could not init the "
                                                                                                                                                                                                            + "RuleSet, "
                                                                                                                                                                                                            + e );
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

    protected void tearDown()
    {
        ruleSet = null;
    }

    /**
     * Test createRuleExecutionSet from DOM.
     */
    public void testCreateFromElement() throws Exception
    {
        DOMParser parser = new DOMParser( );
        Document doc = null;
        try
        {
            parser
                  .parse( new InputSource(
                                           RuleEngineTestBase.class
                                                                   .getResourceAsStream( bindUri ) ) );
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
            RuleExecutionSet ruleSet = ruleSetProvider
                                                      .createRuleExecutionSet(
                                                                               element,
                                                                               null );
            assertEquals( "rule set name", "Sisters Rules", ruleSet.getName( ) );
            assertEquals( "number of rules", 1, ruleSet.getRules( ).size( ) );
        }
        else
        {
            fail( "could not build an org.w3c.dom.Element" );
        }
    }

    /**
     * Test createRuleExecutionSet from Serializable.
     */
    public void testCreateFromSerializable() throws Exception
    {
        RuleExecutionSet ruleExecutionSet = ruleSetProvider
                                                           .createRuleExecutionSet(
                                                                                    this.ruleSet,
                                                                                    null );
        assertEquals( "rule set name", "Sisters Rules",
                      ruleExecutionSet.getName( ) );
        assertEquals( "number of rules", 1, ruleExecutionSet.getRules( ).size( ) );
    }

    /**
     * Test createRuleExecutionSet from URI.
     */
    public void testCreateFromURI() throws Exception
    {
        String rulesUri = RuleEngineTestBase.class.getResource( bindUri )
                                                  .toExternalForm( );
        RuleExecutionSet ruleSet = ruleSetProvider
                                                  .createRuleExecutionSet(
                                                                           rulesUri,
                                                                           null );
        assertEquals( "rule set name", "Sisters Rules", ruleSet.getName( ) );
        assertEquals( "number of rules", 1, ruleSet.getRules( ).size( ) );
    }

    public void testIncompatibleSerializableCreation() throws Exception
    {
        try
        {
            RuleExecutionSet ruleSet = ruleSetProvider
                                                      .createRuleExecutionSet(
                                                                               new ArrayList( ),
                                                                               null );
            fail( "Should have thrown an IllegalArgumentException. ArrayList "
                  + "objects are not valid AST representations. " + ruleSet );
        }
        catch ( IllegalArgumentException e )
        {
            /*
             * this is supposed to happen if you pass in a serializable object
             * that isn't a supported AST representation.
             */
        }
    }

    public static void main(String[] args)
    {
        String[] name = {RuleExecutionSetProviderTestCase.class.getName( )};
        junit.textui.TestRunner.main( name );
    }

    public static Test suite()
    {
        return new TestSuite( RuleExecutionSetProviderTestCase.class );
    }
}