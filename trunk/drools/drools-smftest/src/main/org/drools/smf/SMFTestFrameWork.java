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
import org.drools.spi.Consequence;
import org.drools.spi.Extractor;
import org.drools.spi.Tuple;
import org.drools.rule.Declaration;

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
        MockTuple tuple;

        MockConfiguration cheeseConfiguration = new MockConfiguration("test1");
        cheeseConfiguration.setText(Cheese.class.getName());
        ObjectTypeFactory objectTypeFactory = module.getObjectTypeFactory("class");
        ObjectType cheeseType = objectTypeFactory.newObjectType(cheeseConfiguration);

        tuple = new MockTuple();
        tuple.setWorkingMemory(new MockWorkingMemory());

        //simple condition checks
        assertTrue(testCondition(0, tuple, new Declaration[] {}));
        assertFalse(testCondition(1, tuple, new Declaration[] {}));
        assertTrue(testCondition(2, tuple, new Declaration[] {}));
        assertTrue(testCondition(3, tuple, new Declaration[] {}));
        assertTrue(testCondition(4, tuple, new Declaration[] {}));

        Declaration camembertDecl = new Declaration(cheeseType, "camembert");
        Declaration stiltonDecl = new Declaration(cheeseType, "stilton");

        //condition check with a single declaration
        tuple.put(camembertDecl, new Cheese("camembert"));
        assertTrue(testCondition(5, tuple, new Declaration[] {camembertDecl}));
        assertFalse(testCondition(6, tuple, new Declaration[] {camembertDecl}));

        //condition check with a single declaration
        tuple = new MockTuple();
        tuple.setWorkingMemory(new MockWorkingMemory());
        tuple.put(stiltonDecl, new Cheese("stilton"));
        assertTrue(testCondition(7, tuple, new Declaration[] {stiltonDecl}));
        assertFalse(testCondition(8, tuple, new Declaration[] {stiltonDecl}));


        //condition check with two declarations
        tuple = new MockTuple();
        tuple.setWorkingMemory(new MockWorkingMemory());
        tuple.put(stiltonDecl, new Cheese("stilton"));
        tuple.put(camembertDecl, new Cheese("camembert"));
        assertFalse(testCondition(9, tuple, new Declaration[] {stiltonDecl, camembertDecl}));
        assertTrue(testCondition(10, tuple, new Declaration[] {stiltonDecl, camembertDecl}));
        assertTrue(testCondition(11, tuple, new Declaration[] {stiltonDecl, camembertDecl}));
        assertFalse(testCondition(12, tuple, new Declaration[] {stiltonDecl, camembertDecl}));

        //condition check with 2 declarations and application data
        WorkingMemory workingMemory = new MockWorkingMemory();
        workingMemory.setApplicationData("bites", new Integer(3));
        workingMemory.setApplicationData("favouriteCheese", new Cheese("camembert"));
        tuple.setWorkingMemory(workingMemory);

        assertTrue(testCondition(13, tuple, new Declaration[] {stiltonDecl, camembertDecl}));
        assertFalse(testCondition(14, tuple, new Declaration[] {stiltonDecl, camembertDecl}));
        assertTrue(testCondition(15, tuple, new Declaration[] {stiltonDecl, camembertDecl}));

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
        assertTrue(testCondition(16, tuple, new Declaration[] {favouriteCheeseDecl, camembertDecl}));
        assertTrue(testCondition(17, tuple, new Declaration[] {camembertDecl, favouriteCheeseDecl}));
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

        MockConfiguration cheeseConfiguration = new MockConfiguration("test1");
        cheeseConfiguration.setText(Cheese.class.getName());

        ObjectTypeFactory objectTypeFactory = module.getObjectTypeFactory("class");
        ObjectType cheeseType = objectTypeFactory.newObjectType(cheeseConfiguration);

        Declaration camembertDecl = new Declaration(cheeseType, "camembert");
        Declaration stiltonDecl = new Declaration(cheeseType, "stilton");


        tuple = new MockTuple();
        tuple.setWorkingMemory(new MockWorkingMemory());
        assertEquals("camembert", (String) testExtractor(0, "java.lang.String", tuple, new Declaration[] {}));

        tuple.put(camembertDecl, new Cheese("camembert"));
        assertEquals("I have 3 bites of camembert left", (String) testExtractor(1, "java.lang.String", tuple, new Declaration[] {camembertDecl}));
    }

    /**
     * private helper method to test each of the extracted extractors
     */
    private Object  testExtractor(int testNumber, String returnType, Tuple tuple, Declaration[] decls) throws Exception
    {
        ExtractorFactory extractorFactory = module.getExtractorFactory("extractor");
        MockConfiguration extractorConfiguration = new MockConfiguration("test" + testNumber);
        extractorConfiguration.setAttribute("javaClass", returnType);
        extractorConfiguration.setText((String) tests.get(testNumber));
        Extractor extractor = extractorFactory.newExtractor(extractorConfiguration, decls);
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
        testConsequence(4, tuple, new Declaration[] {favouriteCheeseDecl, camembertDecl});
        testConsequence(5, tuple, new Declaration[] {camembertDecl, favouriteCheeseDecl});
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
