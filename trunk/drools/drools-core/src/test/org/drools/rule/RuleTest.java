package org.drools.rule;

import org.drools.DroolsTestCase;
import org.drools.WorkingMemory;
import org.drools.spi.Duration;
import org.drools.spi.Consequence;
import org.drools.spi.Tuple;
import org.drools.spi.MockObjectType;

import java.io.ByteArrayOutputStream;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.ObjectOutput;
import java.io.ObjectInputStream;
import java.io.ObjectInput;
import java.util.Set;

public class RuleTest
    extends DroolsTestCase
{
    public void testConstruct()
    {
        Rule rule = new Rule( "test-rule" );

        assertTrue( ! rule.isValid() );

        try
        {
            rule.checkValidity();

            fail( "Should have thrown InvalidRuleException" );
        }
        catch (InvalidRuleException e)
        {
            // expected and correct
        }

        assertLength( 0,
                      rule.getParameterDeclarations() );

        assertLength( 0,
                      rule.getLocalDeclarations() );

        assertLength( 0,
                      rule.getAllDeclarations() );

        assertLength( 0,
                      rule.getExtractions() );

        assertLength( 0,
                      rule.getConditions() );

        assertNull( rule.getConsequence() );
    }

    public void testParameterDeclarations()
        throws Exception
    {
        Rule rule = new Rule( "test-rule" );

        Declaration paramDecl = new Declaration( new MockObjectType( true ),
                                                 "paramVar" );

        rule.addParameterDeclaration( paramDecl );

        Declaration[] paramDecls = rule.getParameterDeclarations();

        assertLength( 1,
                      paramDecls );

        assertContains( paramDecl,
                        paramDecls );

        assertSame( paramDecl,
                    rule.getParameterDeclaration( "paramVar" ) );

        assertNull( rule.getParameterDeclaration( "betty" ) );

        Declaration[] localDecls = rule.getLocalDeclarations();

        assertLength( 0,
                      localDecls );

        Declaration[] allDecls = rule.getAllDeclarations();

        assertLength( 1,
                      allDecls );

        assertContains( paramDecl,
                        allDecls );

        assertNull( rule.getDeclaration( "betty" ) );
    }

    public void testLocalDeclarations()
        throws Exception
    {
        Rule rule = new Rule( "test-rule" );

        Declaration paramDecl = new Declaration( new MockObjectType( true ),
                                                 "paramVar" );

        Declaration localDecl = new Declaration( new MockObjectType( true ),
                                                 "localVar" );

        Extraction extraction = new Extraction( localDecl,
                                                null );

        rule.addParameterDeclaration( paramDecl );
        rule.addExtraction( extraction );

        Declaration[] paramDecls = rule.getParameterDeclarations();
        assertLength( 1,
                      paramDecls );


        assertContains( paramDecl,
                        paramDecls );

        Declaration[] localDecls = rule.getLocalDeclarations();

        assertLength( 1,
                      localDecls );

        assertContains( localDecl,
                        localDecls );

        assertSame( localDecl,
                    rule.getDeclaration( "localVar" ) );

        Declaration[] allDecls = rule.getAllDeclarations();

        assertLength( 2,
                      allDecls );

        assertContains( paramDecl,
                        allDecls );

        assertContains( localDecl,
                        allDecls );

        assertNull( rule.getDeclaration( "betty" ) );
    }

    public void testDocumenation()
        throws Exception
    {
        Rule rule = new Rule( "test-rule" );

        assertNull( rule.getDocumentation() );

        rule.setDocumentation( "the cheesiest!" );

        assertEquals( "the cheesiest!",
                      rule.getDocumentation() );
    }

    public void testSalience()
        throws Exception
    {
        Rule rule = new Rule( "test-rule" );

        assertEquals( 0,
                      rule.getSalience() );

        rule.setSalience( 42 );

        assertEquals( 42,
                      rule.getSalience() );
    }

    public void testLoadOrder()
        throws Exception
    {
        Rule rule = new Rule( "test-rule" );

        assertEquals( 0,
                      rule.getLoadOrder() );

        rule.setLoadOrder( 42 );

        assertEquals( 42,
                      rule.getLoadOrder() );
    }

    public void testDuration_SimpleLong()
        throws Exception
    {
        Rule rule = new Rule( "test-rule" );

        rule.setDuration( 42L );

        Duration dur = rule.getDuration();

        assertNotNull( dur );

        assertTrue( dur instanceof FixedDuration );

        assertEquals( 42L,
                      rule.getDuration().getDuration( null ) );
    }

    public void testDuration_WithObject()
        throws Exception
    {
        Duration dur = new FixedDuration( 42 );

        Rule rule = new Rule( "test-rule" );

        rule.setDuration( dur );

        assertSame( dur,
                    rule.getDuration() );
    }

    public void testConsequence()
        throws Exception
    {
        Rule rule = new Rule( "test-rule" );

        assertNull( rule.getConsequence() );

        Consequence consequence = new Consequence()
            {
                public void invoke(Tuple tuple,
                                   WorkingMemory workingMemory)
                {
                    // nothing;
                }
            };

        rule.setConsequence( consequence );

        assertSame( consequence,
                    rule.getConsequence() );
    }

    public void testSerializeRuleSet() throws Exception
    {

        Rule rule = new Rule( "test-rule" );
        Declaration paramDecl = new Declaration( new MockObjectType( true ),
                                                 "paramVar" );

        Declaration localDecl = new Declaration( new MockObjectType( true ),
                                                 "localVar" );

        Extraction extraction = new Extraction( localDecl,
                                                null );

        rule.addParameterDeclaration( paramDecl );
        rule.addExtraction( extraction );

        //add consequence
        rule.setConsequence( new org.drools.spi.InstrumentedConsequence() );

        //add conditions
        rule.addCondition( new org.drools.spi.InstrumentedCondition() );
        rule.addCondition( new org.drools.spi.InstrumentedCondition() );

        rule.setSalience( 42 );
        rule.setLoadOrder( 22 );

        // Serialize to a byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream() ;
        ObjectOutput out = new ObjectOutputStream(bos) ;
        out.writeObject(rule);
        out.close();

        // Get the bytes of the serialized object
        byte[] bytes = bos.toByteArray();

        // Deserialize from a byte array
        ObjectInput in = new ObjectInputStream(new ByteArrayInputStream(bytes));
        rule = (Rule) in.readObject();
        in.close();

        assertEquals(42, rule.getSalience());
        assertEquals(22, rule.getLoadOrder());
        assertLength(1, rule.getLocalDeclarations());
        assertLength(1, rule.getParameterDeclarations());
        assertLength(2, rule.getConditions());
    }
}
