package org.drools.reteoo;

import junit.framework.TestCase;
import org.drools.FactHandle;

import java.io.Serializable;

public class FactHandleListTest extends TestCase
{
    public void testIsSerializable()
    {
        assertTrue(Serializable.class.isAssignableFrom( FactHandleList.class ) );
    }

    public void testSingleValueConstruction()
    {
        FactHandle handle = new FactHandleImpl( 37 );
        FactHandleList list = new FactHandleList( 2, handle );
        assertEquals( 3, list.length( ) );
        assertNull( list.get( 0 ) );
        assertNull( list.get( 1 ) );
        assertSame( handle, list.get( 2 ) );
    }

    public void testJoinConstruction()
    {
        FactHandle handleA = new FactHandleImpl( 37 );
        FactHandle handleB = new FactHandleImpl( 43 );

        FactHandleList list = new FactHandleList( new FactHandleList( 1, handleA ),
                                                  new FactHandleList( 3, handleB ) );

        assertEquals( 4, list.length() );
        assertNull( list.get( 0 ) );
        assertSame( handleA, list.get( 1 ) );
        assertNull( list.get( 2 ) );
        assertSame( handleB, list.get( 3 ) );
    }

    public void testContains()
    {
        FactHandle handle = new FactHandleImpl( 13 );
        FactHandleList list = new FactHandleList( 8, handle );
        assertTrue( list.contains( handle ) );
        assertFalse( list.contains( new FactHandleImpl( 203 ) ) );
    }

    public void testContainsAll()
    {
        FactHandle handleA = new FactHandleImpl( 37 );
        FactHandle handleB = new FactHandleImpl( 43 );
        FactHandle handleC = new FactHandleImpl( 59 );
        FactHandle handleD = new FactHandleImpl( 61 );

        FactHandleList listA = new FactHandleList( 1, handleA );
        FactHandleList listB = new FactHandleList( 3, handleB );
        FactHandleList listC = new FactHandleList( 0, handleC );
        FactHandleList listD = new FactHandleList( 2, handleD );

        FactHandleList listAB = new FactHandleList( listA,
                                                    listB );

        FactHandleList listCD = new FactHandleList( listC,
                                                    listD );

        FactHandleList listACD = new FactHandleList( listA,
                                                     listCD );

        FactHandleList listABCD = new FactHandleList( listAB, listCD );

        assertTrue( listABCD.containsAll( listACD ) );
        assertFalse( listACD.containsAll( listABCD ) );
        assertFalse( listABCD.containsAll( new FactHandleList( 99, handleA ) ) );
    }

    public void testIndexOutOfBoundsThrowsException()
    {
        FactHandle handle = new FactHandleImpl( 253 );
        FactHandleList list = new FactHandleList( 0, handle );
        try
        {
            list.get( 1 );
            fail();
        }
        catch ( ArrayIndexOutOfBoundsException e )
        {
            // expected
        }
    }
}