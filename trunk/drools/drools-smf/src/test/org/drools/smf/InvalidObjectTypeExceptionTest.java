package org.drools.smf;

import org.drools.DroolsTestCase;

public class InvalidObjectTypeExceptionTest
    extends DroolsTestCase
{
    public void testConstruct()
    {
        InvalidObjectTypeException e = new InvalidObjectTypeException( Object.class );

        assertSame( Object.class,
                    e.getInvalidClass() );

        assertEquals( "java.lang.Object is not a valid object type",
                      e.getMessage() );
    }
}
