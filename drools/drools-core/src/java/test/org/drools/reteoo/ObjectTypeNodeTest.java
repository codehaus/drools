
package org.drools.reteoo;

import org.drools.AssertionException;
import org.drools.spi.Declaration;
import org.drools.spi.Tuple;
import org.drools.semantic.java.JavaObjectType;

import junit.framework.TestCase;

import java.util.List;

public class ObjectTypeNodeTest extends TestCase
{
    private Declaration decl;

    public ObjectTypeNodeTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
        this.decl = new Declaration( new JavaObjectType( String.class ),
                                     "object" );
    }

    public void tearDown()
    {
        this.decl = null;
    }

    public void testAssertObject()
    {
        ObjectTypeNode            objectTypeNode = new ObjectTypeNode( new JavaObjectType( String.class ) );

        InstrumentedParameterNode paramNode      = new InstrumentedParameterNode( null,
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
        }
        catch (AssertionException e)
        {
            fail( e.toString() );
        }
    }
}
    
