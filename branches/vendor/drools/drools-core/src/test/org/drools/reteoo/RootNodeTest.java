
package org.drools.reteoo;

import org.drools.AssertionException;
import org.drools.RetractionException;
import org.drools.semantic.java.JavaObjectType;

import junit.framework.TestCase;

import java.util.List;

public class RootNodeTest extends TestCase
{
    private RootNode rootNode;

    private InstrumentedObjectTypeNode objectTypeNode;
    private InstrumentedObjectTypeNode stringTypeNode;

    public RootNodeTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
        this.rootNode = new RootNode();

        this.objectTypeNode = new InstrumentedObjectTypeNode( new JavaObjectType( Object.class ) );
        this.stringTypeNode = new InstrumentedObjectTypeNode( new JavaObjectType( String.class ) );

        this.rootNode.addObjectTypeNode( this.objectTypeNode );
        this.rootNode.addObjectTypeNode( this.stringTypeNode );
    }

    public void tearDown()
    {
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
            this.rootNode.assertObject( object1,
                                        null );
            
            this.rootNode.assertObject( string1,
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
            this.rootNode.retractObject( object1,
                                         null );
            
            this.rootNode.retractObject( string1,
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
