package org.drools;

import junit.framework.TestCase;

public class AssertionExceptionTest
    extends TestCase
{
    public void testConstruct()
    {
        AssertionException e = new AssertionException();

        assertNull( e.getRootCause() );
    }

    public void testConstruct_WithRootCause()
    {
        Exception rootCause = new Exception();

        AssertionException e = new AssertionException( rootCause );

        assertSame( rootCause,
                    e.getRootCause() );
    }

}
