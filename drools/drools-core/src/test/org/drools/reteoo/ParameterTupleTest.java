package org.drools.reteoo;

import junit.framework.TestCase;

import org.drools.MockObjectType;
import org.drools.rule.Declaration;

public class ParameterTupleTest
    extends TestCase
{
    private Declaration decl;
    private Declaration otherDecl;

    public void setUp()
    {
        this.decl = new Declaration( new MockObjectType( String.class ),
                                     "decl" );

        this.otherDecl = new Declaration( new MockObjectType( String.class ),
                                          "otherDecl" );
    }

    public void tearDown()
    {
        this.decl = null;
    }

    public void testNothing()
    {
    }

/*
    public void testConstruct()
    {
        Object obj = new Object();

        ParameterTuple tuple = new ParameterTuple( this.decl,
                                                   obj );
    }

    public void testGetParameterObject()
    {
        Object obj = new Object();

        ParameterTuple tuple = new ParameterTuple( this.decl,
                                                   obj );

        assertSame( obj,
                    tuple.getParameterObject() );
    }

    public void testGetParameterDeclaration()
    {
        Object obj = new Object();

        ParameterTuple tuple = new ParameterTuple( this.decl,
                                                   obj );

        assertSame( this.decl,
                    tuple.getParameterDeclaration() );
    }

    public void testGetDeclarations()
    {
        Object obj = new Object();

        ParameterTuple tuple = new ParameterTuple( this.decl,
                                                   obj );
        Set decls = tuple.getDeclarations();

        assertEquals( 1,
                      decls.size() );

        assertTrue( decls.contains( this.decl ) );
    }

    public void testGet()
    {
        Object obj = new Object();

        ParameterTuple tuple = new ParameterTuple( this.decl,
                                                   obj );


        Object otherObj = new Object();

        tuple.putOtherColumn( this.otherDecl,
                              otherObj );

        assertSame( obj,
                    tuple.get( this.decl ) );

        assertSame( otherObj,
                    tuple.get( this.otherDecl ) );

    }
*/
}
