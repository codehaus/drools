package org.drools.rule;

import junit.framework.TestCase;
import org.drools.spi.Consequence;
import org.drools.spi.Extractor;
import org.drools.spi.MockObjectType;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectInputStream;
import java.io.ObjectInput;


public class RuleSetTest extends TestCase
{
    private RuleSet ruleSet;

    public RuleSetTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
        this.ruleSet = new RuleSet( "rule_set" );
    }

    public void tearDown()
    {
        this.ruleSet = null;
    }

    public void testBasics()
    {
        assertEquals( "rule_set",
                      this.ruleSet.getName() );
    }

    public void testDocumentation()
    {
        assertNull( this.ruleSet.getDocumentation() );

        this.ruleSet.setDocumentation( "the cheesiest!" );

        assertEquals( "the cheesiest!",
                      this.ruleSet.getDocumentation() );
    }

    /** A RuleSet MUST accept any Rule that does not have
     *  a conflicting name.
     */
    public void testAddRule()
        throws Exception
    {
        InstrumentedRule rule;

        rule = new InstrumentedRule( "cheese" );
        rule.isValid( true );
        this.ruleSet.addRule( rule );
        assertEquals(0, rule.getLoadOrder());

        rule = new InstrumentedRule( "meat" );
        rule.isValid( true );
        this.ruleSet.addRule( rule );
        assertEquals(1, rule.getLoadOrder());

        rule = new InstrumentedRule( "vegetables" );
        rule.isValid( true );
        this.ruleSet.addRule( rule );
        assertEquals(2, rule.getLoadOrder());
    }

    public void testGetRule()
        throws Exception
    {
        assertNull( this.ruleSet.getRule( "cheese" ) );

        InstrumentedRule rule = new InstrumentedRule( "cheese" );

        rule.isValid( true );

        this.ruleSet.addRule( rule );

        assertSame( rule,
                    this.ruleSet.getRule( "cheese" ) );

        assertNull( this.ruleSet.getRule( "betty" ) );
    }

    /** A RuleSet MUST throw a DuplicateRuleNameException
     *  if an attempt to add a Rule whose name conflicts
     *  with an already added Rule.
     */
    public void testAddRuleDuplicate()
        throws Exception
    {
        InstrumentedRule rule1 = new InstrumentedRule( "cheese" );
        InstrumentedRule rule2 = new InstrumentedRule( "cheese" );

        rule1.isValid( true );
        rule2.isValid( true );

        this.ruleSet.addRule( rule1 );

        try
        {
            this.ruleSet.addRule( rule2 );

            fail( "Should have thrown DuplicateRuleNameException" );
        }
        catch (DuplicateRuleNameException e)
        {
            assertSame( this.ruleSet,
                        e.getRuleSet() );

            assertSame( rule1,
                        e.getOriginalRule() );

            assertSame( rule2,
                        e.getConflictingRule() );
        }
    }

    public void testSerializeRuleSet() throws Exception
    {
        Rule rule1 = new Rule( "test-rule 1" );
        Declaration paramDecl = new Declaration( new MockObjectType( true ),
                                                 "paramVar" );

        Declaration localDecl = new Declaration( new MockObjectType( true ),
                                                 "localVar" );

        Extraction extraction = new Extraction( localDecl,
                                                null );

        rule1.addParameterDeclaration( paramDecl );
        rule1.addExtraction( extraction );

        //add consequence
        rule1.setConsequence( new org.drools.spi.InstrumentedConsequence() );

        //add conditions
        rule1.addCondition( new org.drools.spi.InstrumentedCondition() );
        rule1.addCondition( new org.drools.spi.InstrumentedCondition() );

        rule1.setSalience( 42 );
        rule1.setLoadOrder( 22 );

        Rule rule2 = new Rule( "test-rule 2" );
        paramDecl = new Declaration( new MockObjectType( true ),
                                                 "paramVar" );

        localDecl = new Declaration( new MockObjectType( true ),
                                                 "localVar" );

        extraction = new Extraction( localDecl,
                                                null );

        rule2.addParameterDeclaration( paramDecl );
        rule2.addExtraction( extraction );

        //add consequence
        rule2.setConsequence( new org.drools.spi.InstrumentedConsequence() );

        //add conditions
        rule2.addCondition( new org.drools.spi.InstrumentedCondition() );
        rule2.addCondition( new org.drools.spi.InstrumentedCondition() );
        rule2.setSalience( 12 );
        rule2.setLoadOrder( 2 );

        RuleSet ruleSet= new RuleSet( "rule_set" );
        ruleSet.addRule(rule1);
        ruleSet.addRule(rule2);

        // Serialize to a byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
        ObjectOutput out = new ObjectOutputStream(bos) ;
        out.writeObject(ruleSet);
        out.close();

        // Get the bytes of the serialized object
        byte[] bytes = bos.toByteArray();

        // Deserialize from a byte array
        ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(bytes));
        ruleSet = (RuleSet) in.readObject();
        in.close();
    }
}
