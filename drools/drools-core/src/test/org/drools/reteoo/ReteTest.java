package org.drools.reteoo;

import org.drools.DroolsTestCase;
import org.drools.MockObjectType;

import java.util.Collection;
import java.util.List;

public class ReteTest extends DroolsTestCase
{
    private Rete                       rete;

    private InstrumentedObjectTypeNode objectTypeNode;

    private InstrumentedObjectTypeNode stringTypeNode;

    public void setUp()
    {
        this.rete = new Rete( );

        this.objectTypeNode = new InstrumentedObjectTypeNode(
                                                              new MockObjectType(
                                                                                  Object.class ) );
        this.stringTypeNode = new InstrumentedObjectTypeNode(
                                                              new MockObjectType(
                                                                                  String.class ) );

        this.rete.addObjectTypeNode( this.objectTypeNode );
        this.rete.addObjectTypeNode( this.stringTypeNode );
    }

    public void tearDown()
    {
        this.rete = null;
    }

    public void testGetObjectTypeNodes() throws Exception
    {
        Collection objectTypeNodes = this.rete.getObjectTypeNodes( );

        assertEquals( 2, objectTypeNodes.size( ) );

        assertTrue( objectTypeNodes.contains( this.objectTypeNode ) );
        assertTrue( objectTypeNodes.contains( this.stringTypeNode ) );
    }

    /**
     * All objects asserted to a RootNode must be propagated to all children
     * ObjectTypeNodes.
     */
    public void testAssertObject() throws Exception
    {
        Object object1 = new Object( );
        String string1 = "cheese";

        this.rete.assertObject( new FactHandleImpl( 1 ), object1, null );

        this.rete.assertObject( new FactHandleImpl( 2 ), string1, null );

        List asserted = null;

        // ----------------------------------------

        asserted = this.objectTypeNode.getAssertedObjects( );

        assertEquals( 2, asserted.size( ) );

        assertSame( object1, asserted.get( 0 ) );

        assertSame( string1, asserted.get( 1 ) );

        // ----------------------------------------

        asserted = this.stringTypeNode.getAssertedObjects( );

        assertEquals( 2, asserted.size( ) );

        assertSame( object1, asserted.get( 0 ) );

        assertSame( string1, asserted.get( 1 ) );
    }

    /**
     * All objects retracted from a RootNode must be propagated to all children
     * ObjectTypeNodes.
     */
    public void testRetractObject() throws Exception
    {
        WorkingMemoryImpl memory = new WorkingMemoryImpl( new RuleBaseImpl( new Rete( ) ) );

        FactHandleImpl handle1 = new FactHandleImpl( 1 );
        FactHandleImpl handle2 = new FactHandleImpl( 2 );

        memory.putObject( handle1, "cheese1" );

        memory.putObject( handle2, "cheese2" );

        this.rete.retractObject( handle1, memory );

        this.rete.retractObject( handle2, memory );

        List retracted = null;

        // ----------------------------------------

        retracted = this.objectTypeNode.getRetractedHandles( );

        assertLength( 2, retracted );

        assertContains( handle1, retracted );

        assertContains( handle2, retracted );

        // ----------------------------------------

        retracted = this.stringTypeNode.getRetractedHandles( );

        assertLength( 2, retracted );

        assertContains( handle1, retracted );

        assertContains( handle2, retracted );
    }

    public void testModifyObject() throws Exception
    {
        WorkingMemoryImpl memory = new WorkingMemoryImpl( new RuleBaseImpl( new Rete() ) );

        FactHandleImpl handle1 = new FactHandleImpl( 1 );
        FactHandleImpl handle2 = new FactHandleImpl( 2 );

        memory.putObject( handle1, "cheese1" );

        memory.putObject( handle2, "cheese2" );

        this.rete.modifyObject( handle1, "cheese1-b", memory );

        this.rete.modifyObject( handle2, "cheese2-b", memory );

        List modified = null;

        modified = this.objectTypeNode.getModifiedHandles( );

        assertLength( 2, modified );

        assertContains( handle1, modified );

        assertContains( handle2, modified );
    }
}