package org.drools.spi;

import org.drools.DroolsTestCase;

public class ConsequenceExceptionTest
    extends DroolsTestCase
{
    public void testConstruct()
    {
        ConsequenceException e = new ConsequenceException();

        assertNull( e.getRootCause() );
    }

    public void testConstruct_WithRootCause()
    {
        Exception rootCause = new Exception();

        ConsequenceException e = new ConsequenceException( rootCause );

        assertSame( rootCause,
                    e.getRootCause() );
    }

}
