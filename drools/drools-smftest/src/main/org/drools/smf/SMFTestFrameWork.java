package org.drools.smf;

import junit.framework.TestCase;
import org.drools.MockTuple;
import org.drools.MockWorkingMemory;
import org.drools.WorkingMemory;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spi.Condition;
import org.drools.spi.ConditionException;
import org.drools.spi.Consequence;
import org.drools.spi.ConsequenceException;
import org.drools.spi.ExtractionException;
import org.drools.spi.Extractor;
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
 * conditions.data, extractors.data, consequences.data. Each file is read
 * depending the testType, a List of the specified tests extracted from the
 * file; usig the delimeter <!--drools-test--!>to seperate each test block.
 *
 * Each testType has a corresponding private helper method to instantiate a
 * Condition, Extractor, Consequence for each test using the specified
 * parameters
 */
public abstract class SMFTestFrameWork extends TestCase
{
    /** the samentic name being tested */
    private String              semantic;

    /** The List of tests extracted from the data file */
    private List                tests;

    /** The test type; conditions, extractors, consequences */
    private String              testType;

    /** The SemanticModule implementation return from the SemanticRepository */
    private SemanticModule      module;

    /** The SemanticRepository */
    private SemanticsRepository repository;

    private String              newline = System.getProperty( "line.separator" );

    private Set             imports;

    public SMFTestFrameWork(String name)
    {
        super( name );
        //use the method name, minus the "test" string, to specify the testType
        this.testType = name.substring( 4 ).toLowerCase( );
    }

    /**
     * Reads in the specified data file and extracts to a List of tests using
     * the delimter <!--drools-test--!>
     */
    public void setUp(String semantic, Set imports) throws Exception
    {

        this.semantic = semantic;
        if ( !testType.equals( "conditions" )
             && !testType.equals( "extractors" )
             && !testType.equals( "consequences" ) ) return;

        ClassLoader cl = Thread.currentThread( ).getContextClassLoader( );
        URL semanticTests = cl.getResource( semantic + "-" + testType + ".data" );
        BufferedReader in = new BufferedReader( new InputStreamReader( semanticTests.openStream() ) );
        StringBuffer buffer = new StringBuffer();
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
        ObjectType cheeseType = objectTypeFactory.newObjectType( cheeseConfiguration, new HashSet() );

        tuple = new MockTuple( );
        final Rule rule = new InstrumentedRule( "Test Rule 1" );
        rule.setImports(new HashSet());
        tuple.setRule( rule );
        tuple.setWorkingMemory( new MockWorkingMemory( ) );

        //simple condition checks
        assertTrue( testCondition( testNumber++, tuple, new Declaration[]{}, rule ) ); //0
        assertFalse( testCondition( testNumber++, tuple, new Declaration[]{}, rule ) ); //1
        assertTrue( testCondition( testNumber++, tuple, new Declaration[]{}, rule ) ); //2
        assertTrue( testCondition( testNumber++, tuple, new Declaration[]{}, rule ) ); //3
        assertTrue( testCondition( testNumber++, tuple, new Declaration[]{}, rule ) ); //4

        Declaration camembertDecl = rule.addDeclaration( "camembert", cheeseType );
        Declaration stiltonDecl = rule.addDeclaration( "stilton", cheeseType );

        //condition check with a single declaration
        tuple.put( camembertDecl, new Cheese( "camembert" ) );
        assertTrue( testCondition( testNumber++, tuple, new Declaration[]{camembertDecl}, rule ) ); //5
        assertFalse( testCondition( testNumber++, tuple, new Declaration[]{camembertDecl}, rule ) ); //6

        //condition check with a single declaration
        tuple = new MockTuple( );
        rule.setImports(new HashSet());
        tuple.setRule( rule );
        tuple.setWorkingMemory( new MockWorkingMemory( ) );
        tuple.put( stiltonDecl, new Cheese( "stilton" ) );
        assertTrue( testCondition( testNumber++, tuple, new Declaration[]{stiltonDecl}, rule ) ); //7
        assertFalse( testCondition( testNumber++, tuple, new Declaration[]{stiltonDecl}, rule ) ); //8

        //condition check with two declarations
        tuple = new MockTuple( );
        rule.setImports(new HashSet());
        tuple.setRule( rule );
        tuple.setWorkingMemory( new MockWorkingMemory( ) );
        tuple.put( stiltonDecl, new Cheese( "stilton" ) );
        tuple.put( camembertDecl, new Cheese( "camembert" ) );
        assertFalse( testCondition( testNumber++, tuple, new Declaration[]{
        stiltonDecl, camembertDecl}, rule ) ); //9
        assertTrue( testCondition( testNumber++, tuple, new Declaration[]{
        stiltonDecl, camembertDecl}, rule ) ); //10
        assertTrue( testCondition( testNumber++, tuple, new Declaration[]{
        stiltonDecl, camembertDecl}, rule ) ); //11
        assertFalse( testCondition( testNumber++, tuple, new Declaration[]{
        stiltonDecl, camembertDecl}, rule ) ); //12

        //condition check with 2 declarations and application data
        WorkingMemory workingMemory = new MockWorkingMemory( );
        workingMemory.setApplicationData( "bites", new Integer( 3 ) );
        workingMemory.setApplicationData( "favouriteCheese", new Cheese( "camembert" ) );
        tuple.setWorkingMemory( workingMemory );

        assertTrue( testCondition( testNumber++, tuple, new Declaration[]{
        stiltonDecl, camembertDecl}, rule ) ); //13
        assertFalse( testCondition( testNumber++, tuple, new Declaration[]{
        stiltonDecl, camembertDecl}, rule ) ); //14
        assertTrue( testCondition( testNumber++, tuple, new Declaration[]{
        stiltonDecl, camembertDecl}, rule ) ); //15

        //test code works no matter what the order of decl are
        tuple = new MockTuple( );
        rule.setImports(new HashSet());
        tuple.setRule( rule );
        workingMemory = new MockWorkingMemory( );
        tuple.setWorkingMemory( workingMemory );

        MockConfiguration stringConfiguration = new MockConfiguration( "test2" );
        stringConfiguration.setText( String.class.getName( ) );
        ObjectType stringType = objectTypeFactory.newObjectType( stringConfiguration, new HashSet() );
        Declaration favouriteCheeseDecl = rule.addDeclaration( "favouriteCheese", stringType );

        tuple.put( favouriteCheeseDecl, "camembert" );
        tuple.put( camembertDecl, new Cheese( "camembert" ) );
        assertTrue( testCondition( testNumber++, tuple, new Declaration[]{favouriteCheeseDecl, camembertDecl}, rule ) ); //16
        assertTrue( testCondition( testNumber++, tuple, new Declaration[]{camembertDecl, favouriteCheeseDecl}, rule ) ); //17

        // test condition syntax with commas - Drools Issue #77
        assertTrue( testCondition( testNumber++, tuple, new Declaration[]{}, rule ) ); //18

        //test exceptions
        rule.setImports(new HashSet());
        tuple.setRule( rule );
        try
        {
            testCondition( testNumber++, tuple, new Declaration[]{}, rule );
            fail( "Condition should throw an exception" );
        }
        catch (ConditionException e)
        {
            assertEquals( rule, e.getRule( ) );
            assertEquals( tests.get( testNumber - 1 ),
                          e.getInfo() );
        }

        // need to add a test for declaration order

        // 20
        //test imports
        tuple = new MockTuple( );
        rule.setImports(this.imports);
        tuple.setRule( rule );
        workingMemory = new MockWorkingMemory( );
        tuple.setWorkingMemory( workingMemory );

        assertTrue( testCondition( testNumber++, tuple, new Declaration[]{}, rule ) ); //20
    }

    /**
     * private helper method to test each of the extracted conditions
     */
    private boolean testCondition(int testNumber,
                                  Tuple tuple,
                                  Declaration[] decls,
                                  Rule rule ) throws Exception
    {
        ((InstrumentedRule) rule).setDeclarations(decls);
        ConditionFactory conditionFactory = module.getConditionFactory( "condition" );
        MockConfiguration conditionConfiguration = new MockConfiguration( "test"
                                                                                                                                                                                                                                                              + testNumber );
        conditionConfiguration.setText( ( String ) tests.get( testNumber ) );
        Condition condition = conditionFactory.newCondition( conditionConfiguration, rule );
        return condition.isAllowed( tuple );
    }

    /**
     * Tests each of the extracted tests from extractors.data
     */
    public void testExtractors() throws Exception
    {
        Rule rule = new InstrumentedRule( "Test Rule 1" );

        MockTuple tuple;
        ObjectTypeFactory objectTypeFactory = module.getObjectTypeFactory( "class" );

        // Cheese ObjectType
        MockConfiguration cheeseConfiguration = new MockConfiguration( "cheeseConfig" );
        cheeseConfiguration.setText( Cheese.class.getName( ) );
        ObjectType cheeseType = objectTypeFactory.newObjectType( cheeseConfiguration, null );

        // Integer ObjectType
        MockConfiguration integerConfiguration = new MockConfiguration( "integerConfig" );
        integerConfiguration.setText( Integer.class.getName( ) );
        ObjectType integerType = objectTypeFactory.newObjectType( integerConfiguration, null );

        // Declarations
        Declaration camembertDecl = rule.addDeclaration( "camembert", cheeseType );
        Declaration stiltonDecl = rule.addDeclaration( "stilton", cheeseType );
        Declaration integerDecl = rule.addDeclaration( "bitesLeft", integerType );

        // Setup
        int testNumber = 0;
        tuple = new MockTuple( );
        rule.setImports(new HashSet());
        tuple.setRule( rule );
        tuple.setWorkingMemory( new MockWorkingMemory( ) );

        // The Tests

        // 0
        assertEquals( "camembert",
                      ( String ) testExtractor( testNumber++,
                                                "java.lang.String", tuple,
                                                new Declaration[]{}, rule ) );

        // 1
        Cheese camembert = new Cheese( "camembert" );
        tuple.put( camembertDecl, camembert );
        assertEquals(
                      "I have 3 bites of camembert left",
                      ( String ) testExtractor(
                                                testNumber++,
                                                "java.lang.String",
                                                tuple,
                                                new Declaration[]{camembertDecl}, rule ) );

        // 2
        Cheese stilton = new Cheese( "stilton" );
        tuple.put( stiltonDecl, stilton );
        assertEquals( "I have 3 bites of stilton left",
                      ( String ) testExtractor( testNumber++,
                                                "java.lang.String", tuple,
                                                new Declaration[]{
                                                camembertDecl, stiltonDecl}, rule ) );

        // 3
        tuple.put( integerDecl, new Integer( camembert.getBitesLeft( ) ) );
        assertEquals( new Integer( 3 ),
                      testExtractor( testNumber++, "java.lang.Integer", tuple,
                                     new Declaration[]{camembertDecl,
                                     stiltonDecl, integerDecl}, rule ) );

        // 4
        tuple.put( integerDecl, new Integer( stilton.getBitesLeft( ) ) );
        assertEquals( new Integer( 6 ),
                      testExtractor( testNumber++, "java.lang.Integer", tuple,
                                     new Declaration[]{camembertDecl,
                                     stiltonDecl, integerDecl}, rule ) );

        // 5
        assertEquals( new Cheese( "cheddar" ),
                      testExtractor( testNumber++,
                                     "org.drools.smf.SMFTestFrameWork$Cheese",
                                     tuple, new Declaration[]{camembertDecl,
                                     stiltonDecl}, rule ) );

        // 6
        camembert.eatCheese( );
        assertEquals( new Cheese( "mozzerella" ),
                      testExtractor( testNumber++,
                                     "org.drools.smf.SMFTestFrameWork$Cheese",
                                     tuple, new Declaration[]{camembertDecl,
                                     stiltonDecl}, rule ) );

        // 7
        //test exceptions
        rule = new InstrumentedRule( "Test Rule 1" );
        rule.setImports(new HashSet());
        tuple.setRule( rule );
        try
        {
            testExtractor( testNumber++, "java.lang.Boolean", tuple,
                           new Declaration[]{camembertDecl}, rule );
            fail( "Condition should throw an exception" );
        }
        catch ( ExtractionException e )
        {
            assertEquals( rule, e.getRule( ) );
            assertEquals( tests.get( testNumber - 1 ), e.getExpr( ) );
        }

        //8
        //test imports
        tuple = new MockTuple( );
        rule = new InstrumentedRule( "Test Rule 1" );
        rule.setImports( this.imports );
        tuple.setRule( rule );
        WorkingMemory workingMemory = new MockWorkingMemory( );
        tuple.setWorkingMemory( workingMemory );

        assertEquals( "1brie",
                      testExtractor( testNumber++,
                                     "java.lang.String",
                                     tuple, new Declaration[]{},
                                     rule ) );
    }

    /**
     * private helper method to test each of the extracted extractors
     */
    private Object testExtractor(int testNumber,
                                 String returnType,
                                 Tuple tuple,
                                 Declaration[] decls,
                                 Rule rule ) throws Exception

    {
        ((InstrumentedRule) rule).setDeclarations(decls);

        ExtractorFactory extractorFactory = module.getExtractorFactory( "extractor" );
        MockConfiguration extractorConfiguration = new MockConfiguration( "test"
                                                                                                                                                                                                                                                                                                        + testNumber );
        extractorConfiguration.setAttribute( "javaClass", returnType );
        extractorConfiguration.setText( ( String ) tests.get( testNumber ) );
        Extractor extractor = extractorFactory.newExtractor( extractorConfiguration, rule );
        Object fact = extractor.extractFact( tuple );
        ClassLoader cl = Thread.currentThread( ).getContextClassLoader( );
        if ( !cl.loadClass( returnType ).isInstance( fact ) )
        {
            fail( "Incorrect return type for Extractor" );
        }
        return fact;
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
        ObjectType cheeseType = objectTypeFactory.newObjectType( cheeseConfiguration, null );

        tuple = new MockTuple( );
        Rule rule = new InstrumentedRule( "Test Rule 1" );
        rule.setImports(new HashSet());
        tuple.setRule( rule );
        tuple.setWorkingMemory( new MockWorkingMemory( ) );

        //simple condition, no declrations
        testConsequence( 0, tuple, new Declaration[]{}, rule );

        //need to declare so that the tests have SMFTestFrameWork.Cheese
        // imported
        Declaration camembertDecl = rule.addDeclaration( "camembert", cheeseType );
        Declaration stiltonDecl = rule.addDeclaration( "stilton", cheeseType );

        Cheese camembert = new Cheese( "camembert" );
        Cheese stilton = new Cheese( "stilton" );
        tuple.put( camembertDecl, camembert );
        tuple.put( stiltonDecl, stilton );

        //tests nested classes, public static class SMFTestFrameWork.Cheese,
        // works
        testConsequence( 1, tuple, new Declaration[]{}, rule );

        //now start doing tests with declarations
        //first confirm that biteLeft is 3
        assertEquals( 3, camembert.getBitesLeft( ) );
        assertEquals( 3, stilton.getBitesLeft( ) );
        //execute consequence that calles eatCheese()
        testConsequence( 2, tuple,
                         new Declaration[]{stiltonDecl, camembertDecl}, rule );
        //camembert should be eaten once, and stilton twice
        assertEquals( 2, camembert.getBitesLeft( ) );
        assertEquals( 1, stilton.getBitesLeft( ) );

        //test condition with declarations and application data
        WorkingMemory workingMemory = new MockWorkingMemory( );
        workingMemory.setApplicationData( "bites", new Integer( 3 ) );
        workingMemory.setApplicationData( "cheeses", new HashMap( ) );
        tuple.setWorkingMemory( workingMemory );
        testConsequence( 3, tuple,
                         new Declaration[]{stiltonDecl, camembertDecl}, rule );
        assertEquals( 1, camembert.getBitesLeft( ) );
        assertEquals( 0, stilton.getBitesLeft( ) );
        Map map = ( Map ) workingMemory.getApplicationData( "cheeses" );
        assertEquals( camembert, map.get( "favourite cheese" ) );
        assertEquals( 3, ( ( Integer ) map.get( "bites" ) ).intValue( ) );

        // 4
        //test exceptions
        rule = new InstrumentedRule( "Test Rule 1" );
        rule.setImports(new HashSet());
        tuple.setRule( rule );
        try
        {
            testConsequence( 6, tuple, new Declaration[]{camembertDecl}, rule );
            fail( "Consequence should throw an exception" );
        }
        catch ( ConsequenceException e )
        {
            assertEquals( rule, e.getRule( ) );
        }

        //test code works no matter what the order of decl are
        /*
         * In java this doesn't actually do anything now as Declaration[] in the
         * constructor is ignored, it uses the tuples.getDeclaration() of the
         * invoke method to compiled and also invoke
         */
        /*
         * tuple = new MockTuple(); workingMemory = new MockWorkingMemory();
         * tuple.setWorkingMemory(workingMemory);
         *
         * MockConfiguration stringConfiguration = new
         * MockConfiguration("test2");
         * stringConfiguration.setText(String.class.getName()); ObjectType
         * stringType = objectTypeFactory.newObjectType(stringConfiguration);
         * Declaration favouriteCheeseDecl = new Declaration(stringType,
         * "favouriteCheese");
         *
         * tuple.put(favouriteCheeseDecl, "camembert"); tuple.put(camembertDecl,
         * new Cheese("camembert")); testConsequence(4, tuple, new Declaration[]
         * {favouriteCheeseDecl, camembertDecl}); testConsequence(5, tuple, new
         * Declaration[] {camembertDecl, favouriteCheeseDecl});
         */

        // 7
        //test imports
        tuple = new MockTuple( );
        rule = new InstrumentedRule( "Test Rule 1" );
        rule.setImports( this.imports );
        tuple.setRule( rule );
        workingMemory = new MockWorkingMemory( );
        tuple.setWorkingMemory( workingMemory );
        try
        {
            testConsequence( 7, tuple, new Declaration[]{}, rule );
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
                                 Declaration[] decls,
                                 Rule rule ) throws Exception

    {
        ((InstrumentedRule) rule).setDeclarations(decls);

        ConsequenceFactory consequenceFactory = module
                                                      .getConsequenceFactory( "consequence" );
        MockConfiguration consequenceConfiguration = new MockConfiguration(
                                                                            "test"
                                                                                                                                                                                                                                                                                                                + testNumber );
        consequenceConfiguration.setText( ( String ) tests.get( testNumber ) );
        Consequence consequence = consequenceFactory.newConsequence( consequenceConfiguration, rule );
        consequence.invoke( tuple, tuple.getWorkingMemory() );
    }

    public static boolean conditionExceptionTest() throws Exception
    {
        if ( true )
        {
            throw new Exception( "this is a condition exception" );
        }
        return true;
    }

    public static Boolean extractionExceptionTest() throws Exception
    {
        if ( true )
        {
            throw new Exception( "this is an extraction exception" );
        }
        return new Boolean( true );
    }

    public static void consequenceExceptionTest() throws Exception
    {
        if ( true )
        {
            throw new Exception( "this is a consequence exception" );
        }
    }

    /**
     * Simple nested class used with testing
     */
    public static class Cheese
    {
        private String name;

        private int    bitesLeft = 3;

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
            if ( object == null ) return false;
            if ( !( object instanceof Cheese ) ) return false;
            Cheese otherCheese = ( Cheese ) object;
            return this.name.equals( otherCheese.getName( ) );
        }

        public int hashCode()
        {
            return this.name.hashCode( );
        }
    }
}
