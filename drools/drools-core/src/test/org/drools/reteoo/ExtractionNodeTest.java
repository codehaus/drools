package org.drools.reteoo;

import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.drools.AssertionException;
import org.drools.RuleBase;
import org.drools.conflict.DefaultConflictResolver;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spi.InstrumentedExtractor;
import org.drools.spi.MockObjectType;

public class ExtractionNodeTest
    extends TestCase
{
    private Declaration stringDecl;
    private Declaration objectDecl;

    public void setUp()
    {
        this.stringDecl = new Declaration( new MockObjectType(),
                                                  "string" );

        this.objectDecl = new Declaration( new MockObjectType(),
                                                  "object" );
    }

    public void tearDown()
    {

    }

    public void testGetTupleDeclarations()
    {
        MockTupleSource source = new MockTupleSource();

        source.addTupleDeclaration( stringDecl );

        ExtractionNode extractNode = new ExtractionNode( source,
                                                         objectDecl,
                                                         null );

        Set decls = extractNode.getTupleDeclarations();

        assertEquals( 2,
                      decls.size() );

        assertTrue( decls.contains( objectDecl ) );
        assertTrue( decls.contains( stringDecl ) );
    }

    public void testAssertTuple()
    {
        MockTupleSource source = new MockTupleSource();

        source.addTupleDeclaration( objectDecl );

        ExtractionNode extractNode = new ExtractionNode( source,
                                                         stringDecl,
                                                         new InstrumentedExtractor( "cheese" ) );

        InstrumentedTupleSink sink = new InstrumentedTupleSink();

        extractNode.setTupleSink( sink );

    	RuleBase ruleBase = new RuleBaseImpl( new Rete(), new DefaultConflictResolver());
        Rule rule = new Rule( "test-rule 1" );
        Declaration paramDecl = new Declaration( new MockObjectType( true ),
                                                 "paramVar" );                                                 
        rule.addParameterDeclaration( paramDecl );
        //add consequence
        rule.setConsequence( new org.drools.spi.InstrumentedConsequence() );
        //add condition
        rule.addCondition( new org.drools.spi.InstrumentedCondition() );
        
        ReteTuple tuple = new ReteTuple(ruleBase.newWorkingMemory(), rule);
        
        tuple.putKeyColumn(paramDecl, new FactHandleImpl(1), new String("cheese"));
        

        try
        {
            extractNode.assertTuple( tuple,
                                     (WorkingMemoryImpl) tuple.getWorkingMemory() );            

            List assertedTuples = sink.getAssertedTuples();

            assertEquals( 1,
                          assertedTuples.size() );

            ReteTuple assertedTuple = (ReteTuple) assertedTuples.get( 0 );

            Object value = assertedTuple.get( stringDecl );

            assertNotNull( value );

            assertEquals( "cheese",
                          value );
        }
        catch (AssertionException e)
        {
            fail( e.toString() );
        }
    }
}

