package org.drools.smf;

import junit.framework.TestCase;

import java.net.URL;

import java.io.InputStreamReader;
import java.io.BufferedReader;

import java.util.ArrayList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.drools.MockTuple;
import org.drools.WorkingMemory;

import org.drools.spi.ObjectType;
import org.drools.spi.Condition;
import org.drools.spi.ConditionException;
import org.drools.spi.Consequence;
import org.drools.spi.ConsequenceException;
import org.drools.spi.Extractor;
import org.drools.spi.ExtractionException;
import org.drools.spi.Tuple;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;

import org.drools.MockWorkingMemory;

/**
 * @author mproctor
 *
 * SMTTestFrameWork is a base class for unit testing Semantic Implementations
 * The semantic implementation unit test simply needs to extend this class
 * along with setup method that instructs SMFTEstFrameWork which semantic url
 * to instantiate for testing.
 * public class JavaSemanticTest extends SMFTestFrameWork
 * {
 *     public JavaSemanticTest( String name )
 *     {
 *         super( name );
 *     }
 *
 *     public void setUp() throws Exception
 *     {
 *         super.setUp("java");
 *     }
 * }
 *
 * Each class that extends SMFTestFrameWork must create 3 data files;
 * conditions.data, extractors.data, consequences.data.
 * Each file is read depending the testType, a List of the specified tests
 * extracted from the file; usig the delimeter <!--drools-test--!>
 * to seperate each test block.
 *
 * Each testType has a corresponding private helper method to instantiate
 * a Condition, Extractor, Consequence for each test using the specified parameters
 */
public abstract class SMFTestFrameWork  extends TestCase
{
    /** the samentic name being tested */
    private String semantic;

    /** The List of tests extracted from the data file */
    private List tests;

    /** The test type; conditions, extractors, consequences */
    private String testType;

    /** The SemanticModule implementation return from the SemanticRepository */
    private SemanticModule module;

    /** The SemanticRepository */
    private SemanticsRepository repository;

    private String newline = System.getProperty("line.separator");

    public SMFTestFrameWork( String name )
    {
          super( name );
          //use the method name, minus the "test" string, to specify the testType
          this.testType = name.substring(4).toLowerCase();
    }


    /**
     * Reads in the specified data file and extracts to a List of tests
     * using the delimter <!--drools-test--!>
     */
    public void setUp(String semantic) throws Exception
    {

        this.semantic = semantic;
        if (!testType.equals("conditions")&&!testType.equals("extractors")&&!testType.equals("consequences")) return;

        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        URL semanticTests = cl.getResource(semantic + "-" + testType +  ".data");
        BufferedReader in = new BufferedReader (new InputStreamReader(semanticTests.openStream()));
        StringBuffer buffer = new StringBuffer();
        String blockMarker = "<!--drools-test--!>";
        String line;
        tests = new ArrayList();
        while (in.ready())
        {
            line = in.readLine() + newline;
            if (line.startsWith(blockMarker))
            {
                tests.add(buffer.toString());
                buffer = new StringBuffer();
            }
            else
            {
                buffer.append(line);
            }
        }
        tests.add(buffer.toString());

        this.repository = DefaultSemanticsRepository.getInstance();
        module = this.repository.lookupSemanticModule("http://drools.org/semantics/" + semantic);
    }

    /**
     * Tests each of the extracted tests from conditions.data
     */
    public void testConditions() throws Exception
    {
        // Setup
        int testNumber = 0;
        MockTuple tuple;

        MockConfiguration cheeseConfiguration = new MockConfiguration("test1");
        cheeseConfiguration.setText(Cheese.class.getName());
        ObjectTypeFactory objectTypeFactory = module.getObjectTypeFactory("class");
        ObjectType cheeseType = objectTypeFactory.newObjectType(cheeseConfiguration);

        tuple = new MockTuple();
        tuple.setWorkingMemory(new MockWorkingMemory());

        //simple condition checks
        assertTrue(testCondition(testNumber++, tuple, new Declaration[] {}));  //0
        assertFalse(testCondition(testNumber++, tuple, new Declaration[] {})); //1
        assertTrue(testCondition(testNumber++, tuple, new Declaration[] {}));  //2
        assertTrue(testCondition(testNumber++, tuple, new Declaration[] {}));  //3
        assertTrue(testCondition(testNumber++, tuple, new Declaration[] {}));  //4

        Declaration camembertDecl = new Declaration(cheeseType, "camembert");
        Declaration stiltonDecl = new Declaration(cheeseType, "stilton");

        //condition check with a single declaration
        tuple.put(camembertDecl, new Cheese("camembert"));
        assertTrue(testCondition(testNumber++, tuple, new Declaration[] {camembertDecl})); //5
        assertFalse(testCondition(testNumber++, tuple, new Declaration[] {camembertDecl})); //6

        //condition check with a single declaration
        tuple = new MockTuple();
        tuple.setWorkingMemory(new MockWorkingMemory());
        tuple.put(stiltonDecl, new Cheese("stilton"));
        assertTrue(testCondition(testNumber++, tuple, new Declaration[] {stiltonDecl}));  //7
        assertFalse(testCondition(testNumber++, tuple, new Declaration[] {stiltonDecl})); //8


        //condition check with two declarations
        tuple = new MockTuple();
        tuple.setWorkingMemory(new MockWorkingMemory());
        tuple.put(stiltonDecl, new Cheese("stilton"));
        tuple.put(camembertDecl, new Cheese("camembert"));
        assertFalse(testCondition(testNumber++, tuple, new Declaration[] {stiltonDecl, camembertDecl})); //9
        assertTrue(testCondition(testNumber++, tuple, new Declaration[] {stiltonDecl, camembertDecl}));  //10
        assertTrue(testCondition(testNumber++, tuple, new Declaration[] {stiltonDecl, camembertDecl}));  //11
        assertFalse(testCondition(testNumber++, tuple, new Declaration[] {stiltonDecl, camembertDecl})); //12

        //condition check with 2 declarations and application data
        WorkingMemory workingMemory = new MockWorkingMemory();
        workingMemory.setApplicationData("bites", new Integer(3));
        workingMemory.setApplicationData("favouriteCheese", new Cheese("camembert"));
        tuple.setWorkingMemory(workingMemory);

        assertTrue(testCondition(testNumber++, tuple, new Declaration[] {stiltonDecl, camembertDecl}));  //13
        assertFalse(testCondition(testNumber++, tuple, new Declaration[] {stiltonDecl, camembertDecl})); //14
        assertTrue(testCondition(testNumber++, tuple, new Declaration[] {stiltonDecl, camembertDecl}));  //15

        //test code works no matter what the order of decl are
        tuple = new MockTuple();
        workingMemory = new MockWorkingMemory();
        tuple.setWorkingMemory(workingMemory);

        MockConfiguration stringConfiguration = new MockConfiguration("test2");
        stringConfiguration.setText(String.class.getName());
        ObjectType stringType = objectTypeFactory.newObjectType(stringConfiguration);
        Declaration favouriteCheeseDecl = new Declaration(stringType, "favouriteCheese");

        tuple.put(favouriteCheeseDecl, "camembert");
        tuple.put(camembertDecl, new Cheese("camembert"));
        assertTrue(testCondition(testNumber++, tuple, new Declaration[] {favouriteCheeseDecl, camembertDecl})); //16
        assertTrue(testCondition(testNumber++, tuple, new Declaration[] {camembertDecl, favouriteCheeseDecl})); //17

        // test condition syntax with commas - Drools Issue #77
        assertTrue(testCondition(testNumber++, tuple, new Declaration[] {})); //18
        
        //test exceptions
        Rule rule = new Rule("Test Rule 1");
        tuple.setRule(rule);
        try
        {
            testCondition(testNumber++, tuple, new Declaration[] {});
            fail("Condition should throw an exception");
        }
        catch (ConditionException e)
        {
            assertEquals(rule, e.getRule());
            assertEquals(tests.get(testNumber-1), e.getExpr());
        }
        
    }

    /**
     * private helper method to test each of the extracted conditions
     */
    private boolean testCondition(int testNumber, Tuple tuple, Declaration[] decls) throws Exception
    {
        ConditionFactory conditionFactory = module.getConditionFactory("condition");
        MockConfiguration conditionConfiguration = new MockConfiguration("test" + testNumber);
        conditionConfiguration.setText((String) tests.get(testNumber));
        Condition condition = conditionFactory.newCondition(conditionConfiguration, decls);
        return condition.isAllowed(tuple);
    }

    /**
     * Tests each of the extracted tests from extractors.data
     */
    public void testExtractors() throws Exception
    {
        MockTuple tuple;
        ObjectTypeFactory objectTypeFactory = module.getObjectTypeFactory("class");

        // Cheese ObjectType
        MockConfiguration cheeseConfiguration =
            new MockConfiguration("cheeseConfig");
        cheeseConfiguration.setText(Cheese.class.getName());
        ObjectType cheeseType =
            objectTypeFactory.newObjectType(cheeseConfiguration);

        // Integer ObjectType
        MockConfiguration integerConfiguration =
            new MockConfiguration("integerConfig");
        integerConfiguration.setText(Integer.class.getName());
        ObjectType integerType =
            objectTypeFactory.newObjectType(integerConfiguration);

        // Declarations
        Declaration camembertDecl = new Declaration(cheeseType, "camembert");
        Declaration stiltonDecl = new Declaration(cheeseType, "stilton");
        Declaration integerDecl = new Declaration(integerType, "bitesLeft");

        // Setup
        int testNumber = 0;
        tuple = new MockTuple();
        tuple.setWorkingMemory(new MockWorkingMemory());

        // The Tests

        // 0
        assertEquals("camembert",
                     (String) testExtractor(testNumber++,
                                            "java.lang.String",
                                            tuple, new Declaration[] {}));

        // 1
        Cheese camembert = new Cheese("camembert");
        tuple.put(camembertDecl, camembert);
        assertEquals("I have 3 bites of camembert left",
                     (String) testExtractor(testNumber++,
                                            "java.lang.String",
                                            tuple,
                                            new Declaration[] {camembertDecl}));

        // 2
        Cheese stilton = new Cheese("stilton");
        tuple.put(stiltonDecl, stilton);
        assertEquals("I have 3 bites of stilton left",
                     (String) testExtractor(testNumber++,
                                            "java.lang.String",
                                            tuple,
                                            new Declaration[] {camembertDecl, stiltonDecl}));

        // 3
        tuple.put(integerDecl, new Integer(camembert.getBitesLeft()));
        assertEquals(new Integer(3),
                     testExtractor(testNumber++,
                                   "java.lang.Integer",
                                   tuple,
                                   new Declaration[] {camembertDecl, stiltonDecl, integerDecl}));

        // 4
        tuple.put(integerDecl, new Integer(stilton.getBitesLeft()));
        assertEquals(new Integer(6),
                     testExtractor(testNumber++,
                                   "java.lang.Integer",
                                   tuple,
                                   new Declaration[] {camembertDecl, stiltonDecl, integerDecl}));

        // 5
        assertEquals(new Cheese("cheddar"),
                     testExtractor(testNumber++,
                                   "org.drools.smf.SMFTestFrameWork$Cheese",
                                   tuple,
                                   new Declaration[] {camembertDecl, stiltonDecl}));

       // 6
       camembert.eatCheese();
       assertEquals(new Cheese("mozzerella"),
                    testExtractor(testNumber++,
                                  "org.drools.smf.SMFTestFrameWork$Cheese",
                                  tuple,
                                  new Declaration[] {camembertDecl, stiltonDecl}));

       // 7
       //test exceptions
       Rule rule = new Rule("Test Rule 1");
       tuple.setRule(rule);
       try
       {
           testExtractor(testNumber++, "java.lang.Boolean", tuple, new Declaration[] {camembertDecl});
           fail("Condition should throw an exception");
       }
       catch (ExtractionException e)
       {
           assertEquals(rule, e.getRule());
           assertEquals(tests.get(testNumber-1), e.getExpr());
       }       
    }

    /**
     * private helper method to test each of the extracted extractors
     */
    private Object  testExtractor(
            int testNumber, String returnType, Tuple tuple, Declaration[] decls)
        throws Exception
    {
        ExtractorFactory extractorFactory = module.getExtractorFactory("extractor");
        MockConfiguration extractorConfiguration =
            new MockConfiguration("test" + testNumber);
        extractorConfiguration.setAttribute("javaClass", returnType);
        extractorConfiguration.setText((String) tests.get(testNumber));
        Extractor extractor =
            extractorFactory.newExtractor(extractorConfiguration, decls);
        Object fact = extractor.extractFact(tuple);
        ClassLoader cl = Thread.currentThread().getContextClassLoader();
        if (!cl.loadClass(returnType).isInstance(fact))
        {
           fail("Incorrect return type for Extractor");
        }
        return fact;
    }

    /**
     * Tests each of the extracted tests from consequences.data
     */
    public void testConsequences() throws Exception
    {
        MockTuple tuple;

        MockConfiguration cheeseConfiguration = new MockConfiguration("test1");
        cheeseConfiguration.setText(Cheese.class.getName());
        ObjectTypeFactory objectTypeFactory = module.getObjectTypeFactory("class");
        ObjectType cheeseType = objectTypeFactory.newObjectType(cheeseConfiguration);

        tuple = new MockTuple();
        tuple.setWorkingMemory(new MockWorkingMemory());

        //simple condition, no declrations
        testConsequence(0, tuple, new Declaration[] {});

        //need to declare so that the tests have SMFTestFrameWork.Cheese imported
        Declaration camembertDecl = new Declaration(cheeseType, "camembert");
        Declaration stiltonDecl = new Declaration(cheeseType, "stilton");

        Cheese camembert = new Cheese("camembert");
        Cheese stilton = new Cheese("stilton");
        tuple.put(camembertDecl, camembert);
        tuple.put(stiltonDecl, stilton);

        //tests nested classes, public static class SMFTestFrameWork.Cheese, works
        testConsequence(1, tuple, new Declaration[] {});

        //now start doing tests with declarations
        //first confirm that biteLeft is 3
        assertEquals(3, camembert.getBitesLeft());
        assertEquals(3, stilton.getBitesLeft());
        //execute consequence that calles eatCheese()
        testConsequence(2, tuple, new Declaration[] {stiltonDecl, camembertDecl});
        //camembert should be eaten once, and stilton twice
        assertEquals(2, camembert.getBitesLeft());
        assertEquals(1, stilton.getBitesLeft());

        //test condition with declarations and application data
        WorkingMemory workingMemory = new MockWorkingMemory();
        workingMemory.setApplicationData("bites", new Integer(3));
        workingMemory.setApplicationData("cheeses", new HashMap());
        tuple.setWorkingMemory(workingMemory);
        testConsequence(3, tuple, new Declaration[] {stiltonDecl, camembertDecl});
        assertEquals(1, camembert.getBitesLeft());
        assertEquals(0, stilton.getBitesLeft());
        Map map = (Map) workingMemory.getApplicationData("cheeses");
        assertEquals(camembert, map.get("favourite cheese"));
        assertEquals(3, ((Integer) map.get("bites")).intValue());

        // 7
        //test exceptions
        Rule rule = new Rule("Test Rule 1");
        tuple.setRule(rule);
        try
        {
            testConsequence(4, tuple, new Declaration[] {camembertDecl});
            fail("Condition should throw an exception");
        }
        catch (ConsequenceException e)
        {
            assertEquals(rule, e.getRule());
        }           

        //test code works no matter what the order of decl are
        /* In java this doesn't actually do anything now as Declaration[]
           in the constructor is ignored, it uses the tuples.getDeclaration()
           of the invoke method to compiled and also invoke
        */
        /*
        tuple = new MockTuple();
        workingMemory = new MockWorkingMemory();
        tuple.setWorkingMemory(workingMemory);

        MockConfiguration stringConfiguration = new MockConfiguration("test2");
        stringConfiguration.setText(String.class.getName());
        ObjectType stringType = objectTypeFactory.newObjectType(stringConfiguration);
        Declaration favouriteCheeseDecl = new Declaration(stringType, "favouriteCheese");

        tuple.put(favouriteCheeseDecl, "camembert");
        tuple.put(camembertDecl, new Cheese("camembert"));
        testConsequence(4, tuple, new Declaration[] {favouriteCheeseDecl, camembertDecl});
        testConsequence(5, tuple, new Declaration[] {camembertDecl, favouriteCheeseDecl});
        */
    }

    /**
     * private helper method to test each of the extracted consequences
     */
    private void  testConsequence(int testNumber, Tuple tuple, Declaration[] decls) throws Exception
    {
        ConsequenceFactory consequenceFactory = module.getConsequenceFactory("consequence");
        MockConfiguration consequenceConfiguration = new MockConfiguration("test" + testNumber);
        consequenceConfiguration.setText((String) tests.get(testNumber));
        Consequence consequence = consequenceFactory.newConsequence(consequenceConfiguration, decls);
        consequence.invoke(tuple, tuple.getWorkingMemory());
    }

    public static boolean conditionExceptionTest() throws Exception
    {
        if (true) {
            throw new Exception("this is a condition exception");   
        }
        return true;
    }  

    public static Boolean extractionExceptionTest() throws Exception
    {
        if (true) {
            throw new Exception("this is an extraction exception");   
        }
        return new Boolean(true);
    }  

    public static void consequenceExceptionTest() throws Exception
    {
        if (true) {
            throw new Exception("this is a consequence exception");   
        }
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
            if (object == null) return false;
            if (!(object instanceof Cheese)) return false;
            Cheese otherCheese = (Cheese) object;
            return this.name.equals(otherCheese.getName());
        }

        public int hashCode()
        {
            return this.name.hashCode();
        }        
    }
}
