package org.drools;

import junit.framework.TestCase;

public class NoSuchFactObjectExceptionTest
    extends TestCase
{
    public void testConstruct()
    {
        FactHandle handle = new FactHandle()
            {
                public String toExternalForm()
                {
                    return "cheese";
                }
            };

        NoSuchFactObjectException e = new NoSuchFactObjectException( handle );

        assertSame( handle,
                    e.getFactHandle() );

        assertEquals( "no such fact object for handle: cheese",
                      e.getMessage() );
    }
}
