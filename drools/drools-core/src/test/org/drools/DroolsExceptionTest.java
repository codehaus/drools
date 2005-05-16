package org.drools;

import junit.framework.TestCase;

public class DroolsExceptionTest extends TestCase
{
    public void testConstruct()
    {
        DroolsException e = new DroolsException( );

        assertNull( e.getRootCause( ) );

        assertNull( e.getMessage( ) );
    }

    public void testConstruct_WithMessage()
    {
        DroolsException e = new DroolsException( "cheese" );

        assertNull( e.getRootCause( ) );

        assertEquals( "cheese", e.getMessage( ) );

        assertEquals( "cheese", e.getLocalizedMessage( ) );
    }

    public void testConstruct_WithRootCause()
    {
        Exception rootCause = new Exception( "rootCheese" );

        DroolsException e = new DroolsException( rootCause );

        assertSame( rootCause, e.getRootCause( ) );

        assertEquals( "rootCheese", e.getMessage( ) );

        assertEquals( "rootCheese", e.getLocalizedMessage( ) );
    }
}