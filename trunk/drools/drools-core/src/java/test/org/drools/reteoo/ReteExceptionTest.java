package org.drools.reteoo;

import junit.framework.TestCase;

public class ReteExceptionTest extends TestCase
{
    public ReteExceptionTest(String name)
    {
        super( name );
    }

    public void testConstruct_WithCause()
    {
        Exception root = new Exception( "root cause" );
        ReteException e = new ReteException( root );

        assertSame( root,
                    e.getRootCause() );
    }
}
