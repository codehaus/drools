package org.drools.smf;

import org.drools.DroolsTestCase;

public class InvalidConditionExceptionTest
    extends DroolsTestCase
{
    public void testConstruct()
    {
        InvalidConditionException e = new InvalidConditionException( Object.class );

        assertSame( Object.class,
                    e.getInvalidClass() );

        assertEquals( "java.lang.Object is not a valid condition",
                      e.getMessage() );
    }
}
