
package org.drools.reteoo;

import org.drools.AssertionException;
import org.drools.spi.Declaration;
import org.drools.semantic.java.JavaObjectType;

import org.drools.spi.InstrumentedFactExtractor;

import junit.framework.TestCase;

import java.util.Set;
import java.util.List;

public class AssignmentNodeTest extends TestCase
{
    public AssignmentNodeTest(String name)
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
        Declaration stringDecl = new Declaration( new JavaObjectType( String.class ),
                                                  "string" );

        Declaration objectDecl = new Declaration( new JavaObjectType( Object.class),
                                                  "object" );

        MockTupleSource source = new MockTupleSource();

        source.addTupleDeclaration( stringDecl );

        AssignmentNode assignNode = new AssignmentNode( source,
                                                        objectDecl,
                                                        null );

        Set decls = assignNode.getTupleDeclarations();

        assertEquals( 2,
                      decls.size() );

        assertTrue( decls.contains( objectDecl ) );
        assertTrue( decls.contains( stringDecl ) );
    }

    public void testAssertTuple()
    {
        Declaration stringDecl = new Declaration( new JavaObjectType( String.class ),
                                                  "string" );

        Declaration objectDecl = new Declaration( new JavaObjectType( Object.class),
                                                  "object" );

        MockTupleSource source = new MockTupleSource();

        source.addTupleDeclaration( objectDecl );

        AssignmentNode assignNode = new AssignmentNode( source,
                                                        stringDecl,
                                                        new InstrumentedFactExtractor( "cheese" ) );

        InstrumentedTupleSink sink = new InstrumentedTupleSink();

        assignNode.setTupleSink( sink );

        ReteTuple tuple = new ReteTuple();

        try
        {
            assignNode.assertTuple( source,
                                    tuple,
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

