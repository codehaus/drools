
package org.drools.reteoo.impl;

import org.drools.AssertionException;
import org.drools.RetractionException;
import org.drools.semantics.java.ClassObjectType;

import junit.framework.TestCase;

import java.util.List;

public class ReteImplTest extends TestCase
{
    private ReteImpl rete;

    private InstrumentedObjectTypeNode objectTypeNode;
    private InstrumentedObjectTypeNode stringTypeNode;

    public ReteImplTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
        this.rete = new ReteImpl();

        this.objectTypeNode = new InstrumentedObjectTypeNode( new ClassObjectType( Object.class ) );
        this.stringTypeNode = new InstrumentedObjectTypeNode( new ClassObjectType( String.class ) );

        this.rete.addObjectTypeNode( this.objectTypeNode );
        this.rete.addObjectTypeNode( this.stringTypeNode );
    }

    public void tearDown()
    {
        this.rete = null;
    }

    /** All objects asserted to a RootNode must be propagated
     *  to all children ObjectTypeNodes.
     */
    public void testAssertObject()
    {
        Object object1 = new Object();
        String string1 = "cheese";

        try
        {
            this.rete.assertObject( object1,
                                    null );
            
            this.rete.assertObject( string1,
                                    null );
            
            List asserted = null;

            // ----------------------------------------

            asserted = this.objectTypeNode.getAssertedObjects();

            assertEquals( 2,
                          asserted.size() );

            assertSame( object1,
                        asserted.get( 0 ) );

            assertSame( string1,
                        asserted.get( 1 ) );

            // ----------------------------------------

            asserted = this.stringTypeNode.getAssertedObjects();

            assertEquals( 2,
                          asserted.size() );

            assertSame( object1,
                        asserted.get( 0 ) );

            assertSame( string1,
                        asserted.get( 1 ) );
            
        }
        catch (AssertionException e)
        {
            fail( e.toString() );
        }
    }

    /** All objects retracted from a RootNode must be propagated
     *  to all children ObjectTypeNodes.
     */
    public void testRetractObject()
    {
        Object object1 = new Object();
        String string1 = "cheese";

        try
        {
            this.rete.retractObject( object1,
                                     null );
            
            this.rete.retractObject( string1,
                                     null );
            
            List retracted = null;

            // ----------------------------------------

            retracted = this.objectTypeNode.getRetractedObjects();

            assertEquals( 2,
                          retracted.size() );

            assertSame( object1,
                        retracted.get( 0 ) );

            assertSame( string1,
                        retracted.get( 1 ) );

            // ----------------------------------------

            retracted = this.stringTypeNode.getRetractedObjects();

            assertEquals( 2,
                          retracted.size() );

            assertSame( object1,
                        retracted.get( 0 ) );

            assertSame( string1,
                        retracted.get( 1 ) );
            
        }
        catch (RetractionException e)
        {
            fail( e.toString() );
        }
    }
}
