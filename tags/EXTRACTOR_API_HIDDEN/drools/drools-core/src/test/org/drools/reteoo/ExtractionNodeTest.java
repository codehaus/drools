package org.drools.reteoo;

import junit.framework.TestCase;
import org.drools.AssertionException;
import org.drools.RuleBase;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spi.InstrumentedExtractor;
import org.drools.spi.MockObjectType;

import java.util.List;
import java.util.Set;

public class ExtractionNodeTest extends TestCase
{
    private Rule        rule;
    private Declaration stringDecl;
    private Declaration objectDecl;

    public void setUp()
    {
        this.rule = new Rule( getName() );
        this.stringDecl = this.rule.addLocalDeclaration( "string", new MockObjectType( ) );
        this.objectDecl = this.rule.addLocalDeclaration( "object", new MockObjectType( ) );        
    }

    public void tearDown()
    {
        this.rule = null;
        this.stringDecl = null;
        this.objectDecl = null;
    }

    public void testGetTupleDeclarations()
    {
        MockTupleSource source = new MockTupleSource( );

        source.addTupleDeclaration( stringDecl );

        ExtractionNode extractNode = new ExtractionNode( source, objectDecl, null, false);

        Set decls = extractNode.getTupleDeclarations( );

        assertEquals( 2, decls.size( ) );

        assertTrue( decls.contains( objectDecl ) );
        assertTrue( decls.contains( stringDecl ) );
    }

    public void testAssertTuple()
    {
        MockTupleSource source = new MockTupleSource( );

        source.addTupleDeclaration( objectDecl );

        ExtractionNode extractNode = new ExtractionNode( source, stringDecl, new InstrumentedExtractor( "cheese" ), false );

        InstrumentedTupleSink sink = new InstrumentedTupleSink();

        extractNode.setTupleSink( sink );

        RuleBase ruleBase = new RuleBaseImpl( new Rete( ) );
        Declaration paramDecl = rule.addParameterDeclaration( "paramVar", new MockObjectType( true ) );

        //add consequence
        rule.setConsequence( new org.drools.spi.InstrumentedConsequence( ) );
        //add condition
        rule.addCondition( new org.drools.spi.InstrumentedCondition( ) );

        ReteTuple tuple = new ReteTuple( (WorkingMemoryImpl) ruleBase.newWorkingMemory( ), rule, paramDecl, new FactHandleImpl( 1 ) );

        try
        {
            extractNode.assertTuple( tuple, ( WorkingMemoryImpl ) tuple.getWorkingMemory() );

            List assertedTuples = sink.getAssertedTuples( );

            assertEquals( 1, assertedTuples.size( ) );

            ReteTuple assertedTuple = ( ReteTuple ) assertedTuples.get( 0 );

            Object value = assertedTuple.get( stringDecl );

            assertNotNull( value );

            assertEquals( "cheese", value );
        }
        catch ( AssertionException e )
        {
            fail( e.toString( ) );
        }
    }
}

