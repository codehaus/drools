package org.drools.reteoo;

import org.drools.AssertionException;
import org.drools.rule.Declaration;
import org.drools.spi.Tuple;
import org.drools.MockObjectType;

import junit.framework.TestCase;

import java.util.List;
import java.util.Set;

public class ObjectTypeNodeTest
    extends TestCase
{
    private Declaration decl;

    public void setUp()
    {
        this.decl = new Declaration( new MockObjectType(),
                                     "object" );
    }

    public void tearDown()
    {
        this.decl = null;
    }

    public void testAssertObject()
    {
        /*
        ObjectTypeNodeImpl objectTypeNode = new ObjectTypeNodeImpl( new ClassObjectType( String.class ) );

        InstrumentedParameterNode paramNode = new InstrumentedParameterNode( null,
                                                                             this.decl );

        objectTypeNode.addParameterNode( paramNode );

        Object string1 = new String( "cheese" );
        Object object1 = new Object();

        try
        {
            objectTypeNode.assertObject( string1,
                                         null );
            
            objectTypeNode.assertObject( object1,
                                         null );
            
            List asserted = paramNode.getAssertedObjects();

            assertEquals( 1,
                          asserted.size() );

            assertSame( string1,
                        asserted.get( 0 ) );

            Set paramNodes = objectTypeNode.getParameterNodes();

            assertEquals( 1,
                          paramNodes.size() );

            assertTrue( paramNodes.contains( paramNode ) );
        }
        catch (AssertionException e)
        {
            fail( e.toString() );
        }
        */
    }
}
    
