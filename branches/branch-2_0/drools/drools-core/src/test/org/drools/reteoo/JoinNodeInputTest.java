package org.drools.reteoo;

import junit.framework.TestCase;

public class JoinNodeInputTest extends TestCase
{
    public void setUp()
    {
    }

    public void tearDown()
    {
    }

    public void testConstants()
    {
        assertTrue( JoinNodeInput.LEFT != JoinNodeInput.RIGHT );
    }

    public void testGetSide()
    {
        JoinNodeInput left = new JoinNodeInput( null, JoinNodeInput.LEFT );

        JoinNodeInput right = new JoinNodeInput( null, JoinNodeInput.RIGHT );

        assertEquals( JoinNodeInput.LEFT, left.getSide( ) );

        assertEquals( JoinNodeInput.RIGHT, right.getSide( ) );
    }
}