/*
 * Created on Jul 17, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
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
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class SMFTestFrameWork  extends TestCase
{
    private String semantic;
    private List tests;
    private String testType;
    private SemanticModule module;
    private SemanticsRepository repository;

    private String newline = System.getProperty("line.separator");

    public SMFTestFrameWork( String name )
    {
          super( name );
          this.testType = name.substring(4).toLowerCase();
    }

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

    public void testConditions() throws Exception
    {
        MockTuple tuple;

        MockConfiguration itemConfiguration = new MockConfiguration("test1");
        itemConfiguration.setText(Cheese.class.getName());
        ObjectTypeFactory objectTypeFactory = module.getObjectTypeFactory("class");
        ObjectType itemType = objectTypeFactory.newObjectType(itemConfiguration);

        tuple = new MockTuple();
        tuple.setWorkingMemory(new MockWorkingMemory());
        assertTrue(testCondition(0, tuple, new Declaration[] {}));
        assertFalse(testCondition(1, tuple, new Declaration[] {}));
        assertTrue(testCondition(2, tuple, new Declaration[] {}));
        assertTrue(testCondition(3, tuple, new Declaration[] {}));
        assertTrue(testCondition(4, tuple, new Declaration[] {}));

        Declaration camembertDecl = new Declaration(itemType, "camembert");
        Declaration stiltonDecl = new Declaration(itemType, "stilton");

        tuple.put(camembertDecl, new Cheese("camembert"));
        assertTrue(testCondition(5, tuple, new Declaration[] {camembertDecl}));
        assertFalse(testCondition(6, tuple, new Declaration[] {camembertDecl}));

        tuple = new MockTuple();
        tuple.setWorkingMemory(new MockWorkingMemory());
        tuple.put(stiltonDecl, new Cheese("stilton"));
        assertTrue(testCondition(7, tuple, new Declaration[] {stiltonDecl}));
        assertFalse(testCondition(8, tuple, new Declaration[] {stiltonDecl}));

        tuple = new MockTuple();
        tuple.setWorkingMemory(new MockWorkingMemory());
        tuple.put(stiltonDecl, new Cheese("stilton"));
        tuple.put(camembertDecl, new Cheese("camembert"));
        assertFalse(testCondition(9, tuple, new Declaration[] {stiltonDecl, camembertDecl}));
        assertTrue(testCondition(10, tuple, new Declaration[] {stiltonDecl, camembertDecl}));
        assertTrue(testCondition(11, tuple, new Declaration[] {stiltonDecl, camembertDecl}));
        assertFalse(testCondition(12, tuple, new Declaration[] {stiltonDecl, camembertDecl}));

        WorkingMemory workingMemory = new MockWorkingMemory();
        workingMemory.setApplicationData("bites", new Integer(3));
        workingMemory.setApplicationData("favouriteCheese", new Cheese("camembert"));

        tuple.setWorkingMemory(workingMemory);
        assertTrue(testCondition(13, tuple, new Declaration[] {stiltonDecl, camembertDecl}));
        assertFalse(testCondition(14, tuple, new Declaration[] {stiltonDecl, camembertDecl}));
        assertTrue(testCondition(15, tuple, new Declaration[] {stiltonDecl, camembertDecl}));
    }

    private boolean testCondition(int testNumber, Tuple tuple, Declaration[] decls) throws Exception
    {
        ConditionFactory conditionFactory = module.getConditionFactory("condition");
        MockConfiguration conditionConfiguration = new MockConfiguration("test" + testNumber);
        conditionConfiguration.setText((String) tests.get(testNumber));
        Condition condition = conditionFactory.newCondition(conditionConfiguration, decls);
        return condition.isAllowed(tuple);
    }

    public void testExtractors() throws Exception
    {
        MockTuple tuple;

        MockConfiguration itemConfiguration = new MockConfiguration("test1");
        itemConfiguration.setText(Cheese.class.getName());

        ObjectTypeFactory objectTypeFactory = module.getObjectTypeFactory("class");
        ObjectType itemType = objectTypeFactory.newObjectType(itemConfiguration);

        Declaration camembertDecl = new Declaration(itemType, "camembert");
        Declaration stiltonDecl = new Declaration(itemType, "stilton");


        tuple = new MockTuple();
        tuple.setWorkingMemory(new MockWorkingMemory());
        assertEquals("camembert", (String) testExtractor(0, "java.lang.String", tuple, new Declaration[] {}));

        tuple.put(camembertDecl, new Cheese("camembert"));
        assertEquals("I have 3 bites of camembert left", (String) testExtractor(1, "java.lang.String", tuple, new Declaration[] {camembertDecl}));
    }

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
//        if (!fact.getClass().instanceOf(cl.loadClass(returnType)))
        {
           fail("Incorrect return type for Extractor");
        }
        return fact;
    }

    public void testConsequences() throws Exception
    {
        MockTuple tuple;

        MockConfiguration itemConfiguration = new MockConfiguration("test1");
        itemConfiguration.setText(Cheese.class.getName());
        ObjectTypeFactory objectTypeFactory = module.getObjectTypeFactory("class");
        ObjectType itemType = objectTypeFactory.newObjectType(itemConfiguration);

        Declaration camembertDecl = new Declaration(itemType, "camembert");
        Declaration stiltonDecl = new Declaration(itemType, "stilton");


        tuple = new MockTuple();
        tuple.setWorkingMemory(new MockWorkingMemory());
        testConsequence(0, tuple, new Declaration[] {});
        Cheese camembert = new Cheese("camembert");
        Cheese stilton = new Cheese("stilton");
        tuple.put(camembertDecl, camembert);
        tuple.put(stiltonDecl, stilton);
        testConsequence(1, tuple, new Declaration[] {});

        assertEquals(3, camembert.getBitesLeft());
        assertEquals(3, stilton.getBitesLeft());
        testConsequence(2, tuple, new Declaration[] {stiltonDecl, camembertDecl});
        assertEquals(2, camembert.getBitesLeft());
        assertEquals(1, stilton.getBitesLeft());

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

        //assertTrue(testCondition(0, tuple, new Declaration[] {camembertDecl}));
        //assertFalse(testCondition(1, tuple, new Declaration[] {camembertDecl}));
    }

    private void  testConsequence(int testNumber, Tuple tuple, Declaration[] decls) throws Exception
    {
        ConsequenceFactory consequenceFactory = module.getConsequenceFactory("consequence");
        MockConfiguration consequenceConfiguration = new MockConfiguration("test" + testNumber);
        consequenceConfiguration.setText((String) tests.get(testNumber));
        Consequence consequence = consequenceFactory.newConsequence(consequenceConfiguration, decls);
        consequence.invoke(tuple, tuple.getWorkingMemory());
    }

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
