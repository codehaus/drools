package org.drools.smf;

/*
 * $Id: SMFTestFrameWork.java,v 1.26 2004-12-07 14:52:00 simon Exp $
 *
 * Copyright 2004 (C) The Werken Company. All Rights Reserved.
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

import junit.framework.TestCase;
import org.drools.MockWorkingMemory;
import org.drools.WorkingMemory;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spi.Condition;
import org.drools.spi.ConditionException;
import org.drools.spi.Consequence;
import org.drools.spi.ConsequenceException;
import org.drools.spi.MockTuple;
import org.drools.spi.ObjectType;
import org.drools.spi.Tuple;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author mproctor
 *
 * SMTTestFrameWork is a base class for unit testing Semantic Implementations
 * The semantic implementation unit test simply needs to extend this class along
 * with setup method that instructs SMFTEstFrameWork which semantic url to
 * instantiate for testing. public class JavaSemanticTest extends
 * SMFTestFrameWork { public JavaSemanticTest( String name ) { super( name ); }
 *
 * public void setUp() throws Exception { super.setUp("java"); } }
 *
 * Each class that extends SMFTestFrameWork must create 3 data files;
 * conditions.data, consequences.data. Each file is read depending the testType,
 * a List of the specified tests extracted from the file; usig the delimeter
 * <!--drools-test--!>to seperate each test block.
 *
 * Each testType has a corresponding private helper method to instantiate a
 * Condition, Consequence for each test using the specified parameters
 */
public abstract class SMFTestFrameWork extends TestCase
{
    /** The List of tests extracted from the data file */
    private List tests;

    /** The test type; conditions, consequences */
    private String testType;

    /** The SemanticModule implementation return from the SemanticRepository */
    private SemanticModule module;

    /** The SemanticRepository */
    private SemanticsRepository repository;

    private String newline = System.getProperty( "line.separator" );

    private Set imports;

    public SMFTestFrameWork(String name)
    {
        super( name );
        // use the method name, minus the "test" string, to specify the testType
        this.testType = name.substring( 4 ).toLowerCase( );
    }

    /**
     * Reads in the specified data file and extracts to a List of tests using
     * the delimter <!--drools-test--!>
     */
    public void setUp(String semantic,
                      Set imports) throws Exception
    {
        if ( !"conditions".equals( testType ) && !"consequences".equals( testType ) )
        {
            return;
        }

        ClassLoader cl = Thread.currentThread( ).getContextClassLoader( );
        URL semanticTests = cl.getResource( semantic + "-" + testType + ".data" );
        BufferedReader in = new BufferedReader( new InputStreamReader( semanticTests.openStream( ) ) );
        StringBuffer buffer = new StringBuffer( );
        String blockMarker = "<!--drools-test--!>";
        String line;
        tests = new ArrayList( );
        while ( in.ready( ) )
        {
            line = in.readLine( ) + newline;
            if ( line.startsWith( blockMarker ) )
            {
                tests.add( buffer.toString( ) );
                buffer = new StringBuffer( );
            }
            else
            {
                buffer.append( line );
            }
        }
        tests.add( buffer.toString( ) );

        this.repository = DefaultSemanticsRepository.getInstance( );
        module = this.repository.lookupSemanticModule( "http://drools.org/semantics/" + semantic );

        this.imports = imports;

    }

    /**
     * Tests each of the extracted tests from conditions.data
     */
    public void testConditions() throws Exception
    {
        // Setup
        int testNumber = 0;
        MockTuple tuple;

        MockConfiguration cheeseConfiguration = new MockConfiguration( "test1" );
        cheeseConfiguration.setText( Cheese.class.getName( ) );
        ObjectTypeFactory objectTypeFactory = module.getObjectTypeFactory( "class" );
        ObjectType cheeseType = objectTypeFactory.newObjectType( cheeseConfiguration,
                                                                 new HashSet( ) );

        tuple = new MockTuple( );
        final Rule rule = new Rule( "Test Rule 1" );
        rule.setImports( new HashSet( ) );
        tuple.setRule( rule );
        tuple.setWorkingMemory( new MockWorkingMemory( ) );

        // simple condition checks
        assertTrue( testCondition( testNumber++,
                                   tuple,
                                   rule ) ); // 0
        assertFalse( testCondition( testNumber++,
                                    tuple,
                                    rule ) ); // 1
        assertTrue( testCondition( testNumber++,
                                   tuple,
                                   rule ) ); // 2
        assertTrue( testCondition( testNumber++,
                                   tuple,
                                   rule ) ); // 3
        assertTrue( testCondition( testNumber++,
                                   tuple,
                                   rule ) ); // 4

        Declaration camembertDecl = rule.addParameterDeclaration( "camembert",
                                                                  cheeseType );
        Declaration stiltonDecl = rule.addParameterDeclaration( "stilton",
                                                                cheeseType );

        // condition check with a single declaration
        tuple.put( camembertDecl,
                   new Cheese( "camembert" ) );
        assertTrue( testCondition( testNumber++,
                                   tuple,
                                   rule ) ); // 5
        assertFalse( testCondition( testNumber++,
                                    tuple,
                                    rule ) ); // 6

        // condition check with a single declaration
        tuple = new MockTuple( );
        rule.setImports( new HashSet( ) );
        tuple.setRule( rule );
        tuple.setWorkingMemory( new MockWorkingMemory( ) );
        tuple.put( stiltonDecl,
                   new Cheese( "stilton" ) );
        assertTrue( testCondition( testNumber++,
                                   tuple,
                                   rule ) ); // 7
        assertFalse( testCondition( testNumber++,
                                    tuple,
                                    rule ) ); // 8

        // condition check with two declarations
        tuple = new MockTuple( );
        rule.setImports( new HashSet( ) );
        tuple.setRule( rule );
        tuple.setWorkingMemory( new MockWorkingMemory( ) );
        tuple.put( stiltonDecl,
                   new Cheese( "stilton" ) );
        tuple.put( camembertDecl,
                   new Cheese( "camembert" ) );
        assertFalse( testCondition( testNumber++,
                                    tuple,
                                    rule ) ); // 9
        assertTrue( testCondition( testNumber++,
                                   tuple,
                                   rule ) ); // 10
        assertTrue( testCondition( testNumber++,
                                   tuple,
                                   rule ) ); // 11
        assertFalse( testCondition( testNumber++,
                                    tuple,
                                    rule ) ); // 12

        // condition check with 2 declarations and application data
        WorkingMemory workingMemory = new MockWorkingMemory( );
        workingMemory.setApplicationData( "bites",
                                          new Integer( 3 ) );
        workingMemory.setApplicationData( "favouriteCheese",
                                          new Cheese( "camembert" ) );
        tuple.setWorkingMemory( workingMemory );

        HashMap applicationData = new HashMap( );
        applicationData.put( "bites",
                             Integer.class );
        applicationData.put( "favouriteCheese",
                             Cheese.class );

        rule.setApplicationData( applicationData );

        assertTrue( testCondition( testNumber++,
                                   tuple,
                                   rule ) ); // 13
        assertFalse( testCondition( testNumber++,
                                    tuple,
                                    rule ) ); // 14
        assertTrue( testCondition( testNumber++,
                                   tuple,
                                   rule ) ); // 15

        // test code works no matter what the order of decl are
        tuple = new MockTuple( );
        rule.setImports( new HashSet( ) );
        rule.setApplicationData( new HashMap( ) );
        tuple.setRule( rule );
        workingMemory = new MockWorkingMemory( );
        tuple.setWorkingMemory( workingMemory );

        MockConfiguration stringConfiguration = new MockConfiguration( "test2" );
        stringConfiguration.setText( String.class.getName( ) );
        ObjectType stringType = objectTypeFactory.newObjectType( stringConfiguration,
                                                                 new HashSet( ) );
        Declaration favouriteCheeseDecl = rule.addParameterDeclaration( "favouriteCheese",
                                                                        stringType );

        tuple.put( favouriteCheeseDecl,
                   "camembert" );
        tuple.put( camembertDecl,
                   new Cheese( "camembert" ) );
        assertTrue( testCondition( testNumber++,
                                   tuple,
                                   rule ) ); // 16
        assertTrue( testCondition( testNumber++,
                                   tuple,
                                   rule ) ); // 17

        // test condition syntax with commas - Drools Issue #77
        assertTrue( testCondition( testNumber++,
                                   tuple,
                                   rule ) ); // 18

        // test exceptions
        rule.setImports( this.imports );
        tuple.setRule( rule );
        try
        {
            testCondition( testNumber++,
                           tuple,
                           rule );
            fail( "Condition should throw an exception" );
        }
        catch ( ConditionException e )
        {
            assertEquals( rule,
                          e.getRule( ) );
            assertEquals( tests.get( testNumber - 1 ),
                          e.getInfo( ) );
        }

        // need to add a test for declaration order

        // 20
        // test imports
        tuple = new MockTuple( );
        rule.setImports( this.imports );
        tuple.setRule( rule );
        workingMemory = new MockWorkingMemory( );
        tuple.setWorkingMemory( workingMemory );

        assertTrue( testCondition( testNumber++,
                                   tuple,
                                   rule ) ); // 20
    }

    /**
     * private helper method to test each of the extracted conditions
     */
    private boolean testCondition(int testNumber,
                                  Tuple tuple,
                                  Rule rule) throws Exception
    {
        ConditionFactory conditionFactory = module.getConditionFactory( "condition" );
        MockConfiguration conditionConfiguration = new MockConfiguration( "test" + testNumber );
        conditionConfiguration.setText( (String) tests.get( testNumber ) );
        Condition condition = conditionFactory.newCondition( conditionConfiguration,
                                                             rule );
        return condition.isAllowed( tuple );
    }

    /**
     * Tests each of the extracted tests from consequences.data
     */
    public void testConsequences() throws Exception
    {
        MockTuple tuple;

        MockConfiguration cheeseConfiguration = new MockConfiguration( "test1" );
        cheeseConfiguration.setText( Cheese.class.getName( ) );
        ObjectTypeFactory objectTypeFactory = module.getObjectTypeFactory( "class" );
        ObjectType cheeseType = objectTypeFactory.newObjectType( cheeseConfiguration,
                                                                 null );

        tuple = new MockTuple( );
        Rule rule = new Rule( "Test Rule 1" );
        rule.setImports( this.imports );
        tuple.setRule( rule );
        tuple.setWorkingMemory( new MockWorkingMemory( ) );

        // simple condition, no declrations
        testConsequence( 0,
                         tuple,
                         rule );

        // need to declare so that the tests have SMFTestFrameWork.Cheese
        // imported
        Declaration camembertDecl = rule.addParameterDeclaration( "camembert",
                                                                  cheeseType );
        Declaration stiltonDecl = rule.addParameterDeclaration( "stilton",
                                                                cheeseType );

        Cheese camembert = new Cheese( "camembert" );
        Cheese stilton = new Cheese( "stilton" );
        tuple.put( camembertDecl,
                   camembert );
        tuple.put( stiltonDecl,
                   stilton );

        // tests nested classes, public static class SMFTestFrameWork.Cheese,
        // works
        testConsequence( 1,
                         tuple,
                         rule );

        // now start doing tests with declarations
        // first confirm that biteLeft is 3
        assertEquals( 3,
                      camembert.getBitesLeft( ) );
        assertEquals( 3,
                      stilton.getBitesLeft( ) );
        // execute consequence that calles eatCheese()
        testConsequence( 2,
                         tuple,
                         rule );
        // camembert should be eaten once, and stilton twice
        assertEquals( 2,
                      camembert.getBitesLeft( ) );
        assertEquals( 1,
                      stilton.getBitesLeft( ) );

        // test condition with declarations and application data
        WorkingMemory workingMemory = new MockWorkingMemory( );
        workingMemory.setApplicationData( "bites",
                                          new Integer( 3 ) );
        workingMemory.setApplicationData( "cheeses",
                                          new HashMap( ) );

        HashMap applicationData = new HashMap( );
        applicationData.put( "bites",
                             Integer.class );
        applicationData.put( "cheeses",
                             HashMap.class );

        rule.setApplicationData( applicationData );

        tuple.setWorkingMemory( workingMemory );
        testConsequence( 3,
                         tuple,
                         rule );
        assertEquals( 1,
                      camembert.getBitesLeft( ) );
        assertEquals( 0,
                      stilton.getBitesLeft( ) );
        Map map = (Map) workingMemory.getApplicationData( "cheeses" );
        assertEquals( camembert,
                      map.get( "favourite cheese" ) );
        assertEquals( 3,
                      ((Integer) map.get( "bites" )).intValue( ) );

        // 4
        // test exceptions
        rule = new Rule( "Test Rule 1" );
        rule.setImports( this.imports );
        tuple.setRule( rule );
        try
        {
            testConsequence( 6,
                             tuple,
                             rule );
            fail( "Consequence should throw an exception" );
        }
        catch ( ConsequenceException e )
        {
            assertEquals( rule,
                          e.getRule( ) );
        }

        // 7
        // test imports
        tuple = new MockTuple( );
        rule = new Rule( "Test Rule 1" );
        rule.setImports( this.imports );
        tuple.setRule( rule );
        workingMemory = new MockWorkingMemory( );
        tuple.setWorkingMemory( workingMemory );
        try
        {
            testConsequence( 7,
                             tuple,
                             rule );
        }
        catch ( ConsequenceException e )
        {
            fail( "Consequence should execute without errors" );
        }
    }

    /**
     * private helper method to test each of the extracted consequences
     */
    private void testConsequence(int testNumber,
                                 Tuple tuple,
                                 Rule rule) throws Exception
    {
        ConsequenceFactory consequenceFactory = module.getConsequenceFactory( "consequence" );
        MockConfiguration consequenceConfiguration = new MockConfiguration( "test" + testNumber );
        consequenceConfiguration.setText( (String) tests.get( testNumber ) );
        Consequence consequence = consequenceFactory.newConsequence( consequenceConfiguration,
                                                                     rule );
        consequence.invoke( tuple,
                            tuple.getWorkingMemory( ) );
    }

    public static boolean conditionExceptionTest() throws Exception
    {
        throw new Exception( "this is a condition exception" );
    }

    public static void consequenceExceptionTest() throws Exception
    {
        throw new Exception( "this is a consequence exception" );
    }

    /**
     * Simple nested class used with testing
     */
    public static class Cheese
    {
        private String name;

        private int bitesLeft = 3;

        public Cheese(String name)
        {
            this.name = name;
        }

        public String getName()
        {
            return this.name;
        }

        public void eatCheese()
        {
            bitesLeft--;
        }

        public int getBitesLeft()
        {
            return this.bitesLeft;
        }

        public boolean equals(Object object)
        {
            if ( this == object )
            {
                return true;
            }

            if ( object == null || getClass( ) != object.getClass( ) )
            {
                return false;
            }

            return this.name.equals( ( ( Cheese ) object ).name );
        }

        public int hashCode()
        {
            return this.name.hashCode( );
        }
    }
}
