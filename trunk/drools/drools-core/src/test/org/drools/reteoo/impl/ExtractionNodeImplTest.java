package org.drools.reteoo.impl;

import org.drools.AssertionException;
import org.drools.reteoo.MockTupleSource;
import org.drools.rule.Declaration;
import org.drools.semantics.java.ClassObjectType;

import org.drools.spi.InstrumentedExtractor;

import junit.framework.TestCase;

import java.util.Set;
import java.util.List;

public class ExtractionNodeImplTest extends TestCase
{
    public ExtractionNodeImplTest(String name)
    {
        super( name );
    }

    public void setUp()
    {

    }

    public void tearDown()
    {

    }

    public void testGetTupleDeclarations()
    {
        Declaration stringDecl = new Declaration( new ClassObjectType( String.class ),
                                                  "string" );

        Declaration objectDecl = new Declaration( new ClassObjectType( Object.class),
                                                  "object" );

        MockTupleSource source = new MockTupleSource();

        source.addTupleDeclaration( stringDecl );

        ExtractionNodeImpl extractNode = new ExtractionNodeImpl( source,
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
        Declaration stringDecl = new Declaration( new ClassObjectType( String.class ),
                                                  "string" );

        Declaration objectDecl = new Declaration( new ClassObjectType( Object.class),
                                                  "object" );

        MockTupleSource source = new MockTupleSource();

        source.addTupleDeclaration( objectDecl );

        ExtractionNodeImpl extractNode = new ExtractionNodeImpl( source,
                                                                 stringDecl,
                                                                 new InstrumentedExtractor( "cheese" ) );

        InstrumentedTupleSink sink = new InstrumentedTupleSink();

        extractNode.setTupleSink( sink );

        ReteTuple tuple = new ReteTuple();

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

