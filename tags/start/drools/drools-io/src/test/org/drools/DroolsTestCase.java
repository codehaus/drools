package org.drools;

import junit.framework.TestCase;

import java.util.Arrays;
import java.util.Collection;

public class DroolsTestCase
    extends TestCase
{
    public void assertLength(int len,
                             Object[] array)
    {
        assertEquals( Arrays.asList( array ) + " does not have length of " + len,
                      len,
                      array.length );
    }

    public void assertLength(int len,
                             Collection collection)
    {
        assertEquals( collection + " does not have length of " + len,
                      len,
                      collection.size() );
    }

    public void assertContains(Object obj,
                               Object[] array)
    {
        for ( int i = 0 ; i < array.length ; ++i )
        {
            if ( array[ i ] == obj )
            {
                return;
            }
        }

        fail( Arrays.asList( array ) + " does not contain " + obj );
    }

    public void assertContains(Object obj,
                               Collection collection)
    {
        assertTrue( collection + " does not contain " + obj,
                    collection.contains( obj ) );
    }
}