package org.drools.reteoo;

import junit.framework.TestCase;

import org.drools.rule.Declaration;
import org.drools.MockObjectType;

import java.util.Map;

public class ReteTupleTest
    extends TestCase
{
    private Declaration rootDecl;
    private Declaration otherDecl;
    private Object rootObj;
    private Object otherObj;

    public ReteTupleTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
        this.rootDecl = new Declaration( new MockObjectType(),
                                         "rootObj" );

        this.otherDecl = new Declaration( new MockObjectType(),
                                          "rootObj" );

        this.rootObj = new Object();

        this.otherObj = new Object();
    }

    public void tearDown()
    {
        this.rootDecl = null;
        this.otherDecl = null;

        this.rootObj = null;
        this.otherObj = null;
    }

    public void testNothing()
    {
        // intentionally left blank
    }

    /*
    public void testConstruct_WithKey()
    {
        ReteTuple tuple = new ReteTuple( this.rootDecl,
                                         this.rootObj );
                                         
        assertSame( this.rootObj,
                    tuple.get( this.rootDecl ) );

        assertTrue( tuple.getKey().containsDeclaration( this.rootDecl ) );
        assertTrue( tuple.getKey().containsRootFactObject( this.rootObj ) );
    }

    public void testDependsOn()
    {
        ReteTuple tuple = new ReteTuple( this.rootDecl,
                                         this.rootObj );

        tuple.putOtherColumn( this.otherDecl,
                              this.otherObj );

        assertTrue( tuple.dependsOn( this.rootObj ) );
        assertTrue( ! tuple.dependsOn( this.otherObj ) );
    }

    public void testGetOtherColumns()
    {
        ReteTuple tuple = new ReteTuple( this.rootDecl,
                                         this.rootObj );

        tuple.putOtherColumn( this.otherDecl,
                              this.otherObj );
        
        Map otherCols = tuple.getOtherColumns();

        assertEquals( 1,
                      otherCols.size() );

        assertTrue( otherCols.containsKey( this.otherDecl ) );
        assertTrue( ! otherCols.containsKey( this.rootDecl ) );

        assertSame( this.otherObj,
                    otherCols.get( this.otherDecl ) );
    }
    */

}
