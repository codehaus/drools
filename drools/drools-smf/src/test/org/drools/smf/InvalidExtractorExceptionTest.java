package org.drools.smf;

import org.drools.DroolsTestCase;

public class InvalidExtractorExceptionTest extends DroolsTestCase
{
    public void testConstruct()
    {
        InvalidExtractorException e = new InvalidExtractorException(
                                                                     Object.class );

        assertSame( Object.class, e.getInvalidClass( ) );

        assertEquals( "java.lang.Object is not a valid fact extractor",
                      e.getMessage( ) );
    }
}