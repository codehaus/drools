package org.drools;

import junit.framework.TestCase;

public class RetractionExceptionTest extends TestCase
{
    public void testConstruct()
    {
        RetractionException e = new RetractionException( );

        assertNull( e.getRootCause( ) );
    }

    public void testConstruct_WithRootCause()
    {
        Exception rootCause = new Exception( );

        RetractionException e = new RetractionException( rootCause );

        assertSame( rootCause, e.getRootCause( ) );
    }

}