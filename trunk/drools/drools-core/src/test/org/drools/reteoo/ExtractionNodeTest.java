package org.drools.reteoo;

import org.drools.AssertionException;
import org.drools.RuleBase;
import org.drools.conflict.DefaultConflictResolver;
import org.drools.reteoo.MockTupleSource;
import org.drools.rule.Declaration;
import org.drools.rule.RuleSet;
import org.drools.MockObjectType;

import org.drools.spi.InstrumentedExtractor;

import junit.framework.TestCase;

import java.util.Set;
import java.util.List;

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
        ReteTuple tuple = new ReteTuple(ruleBase.newWorkingMemory(), null);

        try
        {
            extractNode.assertTuple( tuple,
                                     null );

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

