package org.drools.spi;

import org.drools.DroolsTestCase;

public class ConditionExceptionTest extends DroolsTestCase
{
    public void testConstruct()
    {
        ConditionException e = new ConditionException( );

        assertNull( e.getRootCause( ) );
    }

    public void testConstruct_WithRootCause()
    {
        Exception rootCause = new Exception( );

        ConditionException e = new ConditionException( rootCause );

        assertSame( rootCause, e.getRootCause( ) );
    }

}