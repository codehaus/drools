package org.drools.reteoo;

import java.util.List;
import java.util.Set;

import org.drools.DroolsTestCase;
import org.drools.MockObjectType;
import org.drools.rule.Declaration;

public class ObjectTypeNodeTest
    extends DroolsTestCase
{
    private Declaration decl;

    public void setUp()
    {
        this.decl = new Declaration( new MockObjectType( Object.class ),
                                     "object" );
    }

    public void tearDown()
    {
        this.decl = null;
    }

    public void testAssertObject()
        throws Exception
    {
        ObjectTypeNode objectTypeNode = new ObjectTypeNode( new MockObjectType( String.class ) );
        
        InstrumentedParameterNode paramNode = new InstrumentedParameterNode( null,
                                                                             this.decl );

        objectTypeNode.addParameterNode( paramNode );

        WorkingMemoryImpl memory = new WorkingMemoryImpl( null );

        Object string1 = new String( "cheese" );
        Object object1 = new Object();

        FactHandleImpl handle1 = new FactHandleImpl( 1 );
        FactHandleImpl handle2 = new FactHandleImpl( 2 );

        memory.putObject( handle1,
                          string1 );

        memory.putObject( handle2,
                          object1 );

        objectTypeNode.assertObject( handle1,
                                     string1,
                                     memory );
        
        objectTypeNode.assertObject( handle2,
                                     object1,
                                     memory );
        
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
}
    
