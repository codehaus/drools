
package org.drools.reteoo.impl;

import junit.framework.TestCase;

public class PriorityQueueTest extends TestCase
{
    public PriorityQueueTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
    }

    public void tearDown()
    {
    }

    public void testSequential()
    {
        PriorityQueue q = new PriorityQueue();

        q.add( "one",
               1 );

        q.add( "two",
               2 );

        q.add( "three",
               3 );

        assertEquals( "one",
                      q.removeFirst() );

        assertEquals( "two",
                      q.removeFirst() );

        assertEquals( "three",
                      q.removeFirst() );
    }

    public void testReverse()
    {
        PriorityQueue q = new PriorityQueue();

        q.add( "three",
               3 );

        q.add( "two",
               2 );

        q.add( "one",
               1 );

        assertEquals( "one",
                      q.removeFirst() );

        assertEquals( "two",
                      q.removeFirst() );

        assertEquals( "three",
                      q.removeFirst() );
    }

    public void testDuplicates()
    {
        PriorityQueue q = new PriorityQueue();

        q.add( "one",
               1 );

        q.add( "two",
               1 );

        q.add( "three",
               1 );

        assertEquals( "one",
                      q.removeFirst() );

        assertEquals( "two",
                      q.removeFirst() );

        assertEquals( "three",
                      q.removeFirst() );
    }
}
