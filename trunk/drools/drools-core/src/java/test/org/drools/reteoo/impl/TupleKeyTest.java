package org.drools.reteoo.impl;

import org.drools.rule.Declaration;
import org.drools.semantics.java.ClassObjectType;

import junit.framework.TestCase;

public class TupleKeyTest extends TestCase
{
    private TupleKey key;

    private Declaration decl1;
    private Declaration decl2;

    private Object obj1;
    private Object obj2;

    public TupleKeyTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
        this.key = new TupleKey();

        this.decl1 = new Declaration( new ClassObjectType( Object.class ),
                                      "declOne" );

        this.decl2 = new Declaration( new ClassObjectType( Object.class ),
                                      "declTwo" );

        this.obj1 = new Object();
        this.obj2 = new Object();
    }

    public void tearDown()
    {
        this.key = null;
        this.decl1 = null;
        this.decl2 = null;
        this.obj1 = null;
        this.obj2 = null;
    }

    /*
    public void testPutAll()
    {
        this.key.put( this.decl1,
                      this.obj1 );

        this.key.put( this.decl2,
                      this.obj2 );

        TupleKey otherKey = new TupleKey();

        otherKey.putAll( this.key );

        assertEquals( 2,
                      otherKey.size() );

        assertTrue( otherKey.containsDeclaration( this.decl1 ) );
        assertTrue( otherKey.containsDeclaration( this.decl2 ) );

        assertTrue( otherKey.containsRootFactObject( this.obj1 ) );
        assertTrue( otherKey.containsRootFactObject( this.obj2 ) );

        assertSame( this.obj1,
                    otherKey.get( this.decl1 ) );
        assertSame( this.obj2,
                    otherKey.get( this.decl2 ) );
    }

    public void testContainsAll_Exact()
    {
        TupleKey otherKey = new TupleKey();

        this.key.put( this.decl1,
                      this.obj1 );

        this.key.put( this.decl2,
                      this.obj2 );

        otherKey.put( this.decl1,
                      this.obj1 );

        otherKey.put( this.decl2,
                      this.obj2 );

        assertTrue( this.key.containsAll( otherKey ) );
        assertTrue( otherKey.containsAll( this.key ) );
    }

    public void testContainsAll_Superset()
    {
        TupleKey otherKey = new TupleKey();

        this.key.put( this.decl1,
                      this.obj1 );

        this.key.put( this.decl2,
                      this.obj2 );

        otherKey.put( this.decl1,
                      this.obj1 );

        assertTrue( this.key.containsAll( otherKey ) );
        assertTrue( ! otherKey.containsAll( this.key ) );
    }

    public void testContainsAll_Subset()
    {
        TupleKey otherKey = new TupleKey();

        this.key.put( this.decl1,
                      this.obj1 );

        otherKey.put( this.decl1,
                      this.obj1 );

        otherKey.put( this.decl2,
                      this.obj2 );


        assertTrue( ! this.key.containsAll( otherKey ) );
        assertTrue( otherKey.containsAll( this.key ) );
    }

    public void testContainsAll_Null_Null()
    {
        TupleKey otherKey = new TupleKey();

        this.key.put( this.decl1,
                      this.obj1 );

        this.key.put( this.decl2,
                      null );

        otherKey.put( this.decl1,
                      this.obj1 );

        otherKey.put( this.decl2,
                      null );

        assertTrue( this.key.containsAll( otherKey ) );
        assertTrue( otherKey.containsAll( this.key ) );
    }

    public void testContainsAll_Null_NotNull()
    {
        TupleKey otherKey = new TupleKey();

        this.key.put( this.decl1,
                      this.obj1 );

        this.key.put( this.decl2,
                      null );

        otherKey.put( this.decl1,
                      this.obj1 );

        otherKey.put( this.decl2,
                      this.obj2 );

        assertTrue( ! this.key.containsAll( otherKey ) );
        assertTrue( ! otherKey.containsAll( this.key ) );
    }

    public void testContainsAll_MismatchValues()
    {
        TupleKey otherKey = new TupleKey();

        this.key.put( this.decl1,
                      this.obj1 );

        this.key.put( this.decl2,
                      this.obj2 );

        otherKey.put( this.decl1,
                      this.obj1 );

        otherKey.put( this.decl2,
                      new Object() );

        assertTrue( ! this.key.containsAll( otherKey ) );
        assertTrue( ! otherKey.containsAll( this.key ) );
    }

    public void testContainsAll_MismatchDecls()
    {
        TupleKey otherKey = new TupleKey();

        Declaration decl = new Declaration( new ClassObjectType( Object.class ),
                                            "yetAnother" );

        this.key.put( this.decl1,
                      this.obj1 );

        this.key.put( this.decl2,
                      this.obj2 );

        otherKey.put( this.decl1,
                      this.obj1 );

        otherKey.put( decl,
                      this.obj2 );

        assertTrue( ! this.key.containsAll( otherKey ) );
        assertTrue( ! otherKey.containsAll( this.key ) );
    }
    */

    public void testNothing()
    {

    }
}
