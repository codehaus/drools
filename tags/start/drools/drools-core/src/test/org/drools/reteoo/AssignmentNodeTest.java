
package org.drools.reteoo;

import org.drools.spi.Declaration;
import org.drools.semantic.java.JavaObjectType;

import junit.framework.TestCase;

import java.util.Set;

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
}

