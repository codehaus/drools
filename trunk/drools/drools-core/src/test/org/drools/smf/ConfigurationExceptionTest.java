package org.drools.smf;

import org.drools.DroolsTestCase;

public class ConfigurationExceptionTest
    extends DroolsTestCase
{
    public void testConstruct_WithMessage()
    {
        ConfigurationException e = new ConfigurationException( "cheese" );

        assertEquals( "cheese",
                      e.getMessage() );
    }

    public void testConstruct_WithRootCause()
    {
        Exception rootCause = new Exception();

        ConfigurationException e = new ConfigurationException( rootCause );

        assertSame( rootCause,
                    e.getRootCause() );
    }
}
