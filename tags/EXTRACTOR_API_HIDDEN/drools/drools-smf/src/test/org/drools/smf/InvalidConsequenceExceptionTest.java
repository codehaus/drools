package org.drools.smf;

import org.drools.DroolsTestCase;

public class InvalidConsequenceExceptionTest extends DroolsTestCase
{
    public void testConstruct()
    {
        InvalidConsequenceException e = new InvalidConsequenceException(
                                                                         Object.class );

        assertSame( Object.class, e.getInvalidClass( ) );

        assertEquals( "java.lang.Object is not a valid consequence",
                      e.getMessage( ) );
    }
}