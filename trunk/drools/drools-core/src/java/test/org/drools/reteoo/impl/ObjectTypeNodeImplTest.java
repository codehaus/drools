
package org.drools.reteoo.impl;

import org.drools.AssertionException;
import org.drools.rule.Declaration;
import org.drools.spi.Tuple;
import org.drools.semantics.java.ClassObjectType;

import junit.framework.TestCase;

import java.util.List;

public class ObjectTypeNodeImplTest extends TestCase
{
    private Declaration decl;

    public ObjectTypeNodeImplTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
        this.decl = new Declaration( new ClassObjectType( String.class ),
                                     "object" );
    }

    public void tearDown()
    {
        this.decl = null;
    }

    public void testAssertObject()
    {
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
        }
        catch (AssertionException e)
        {
            fail( e.toString() );
        }
    }
}
    
