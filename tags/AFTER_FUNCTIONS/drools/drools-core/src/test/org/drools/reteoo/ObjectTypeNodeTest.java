package org.drools.reteoo;

import org.drools.DroolsTestCase;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spi.MockObjectType;

import java.util.Collection;
import java.util.List;

public class ObjectTypeNodeTest extends DroolsTestCase
{
    public void testAssertObject() throws Exception
    {
        Rule rule = new Rule( getName( ) );

        Declaration decl = rule.addParameterDeclaration( "object", new MockObjectType( Object.class ) );

        ObjectTypeNode objectTypeNode = new ObjectTypeNode( new MockObjectType( String.class ) );

        InstrumentedParameterNode paramNode = new InstrumentedParameterNode( null, decl );
        paramNode.addTupleSink( new InstrumentedTupleSink( ) );

        objectTypeNode.addParameterNode( paramNode );

        WorkingMemoryImpl memory = new WorkingMemoryImpl( new RuleBaseImpl( new Rete( ) ) );

        Object string1 = "cheese";
        Object object1 = new Object( );

        FactHandleImpl handle1 = new FactHandleImpl( 1 );
        FactHandleImpl handle2 = new FactHandleImpl( 2 );

        memory.putObject( handle1, string1 );

        memory.putObject( handle2, object1 );

        objectTypeNode.assertObject( handle1, string1, memory );

        objectTypeNode.assertObject( handle2, object1, memory );

        List asserted = paramNode.getAssertedObjects( );

        assertEquals( 1, asserted.size( ) );

        assertSame( string1, asserted.get( 0 ) );

        Collection paramNodes = objectTypeNode.getParameterNodes( );

        assertEquals( 1, paramNodes.size( ) );

        assertTrue( paramNodes.contains( paramNode ) );
    }
}
