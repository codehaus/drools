package org.drools.spi;

import org.drools.DroolsTestCase;

public class ExtractionExceptionTest
    extends DroolsTestCase
{
    public void testConstruct()
    {
        ExtractionException e = new ExtractionException();

        assertNull( e.getRootCause() );
    }

    public void testConstruct_WithRootCause()
    {
        Exception rootCause = new Exception();

        ExtractionException e = new ExtractionException( rootCause );

        assertSame( rootCause,
                    e.getRootCause() );
    }

}
