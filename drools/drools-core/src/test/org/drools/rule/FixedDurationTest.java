package org.drools.rule;

import junit.framework.TestCase;

public class FixedDurationTest
    extends TestCase
{
    public void testConstruct()
        throws Exception
    {
        FixedDuration dur = new FixedDuration();

        assertEquals( 0,
                      dur.getDuration( null ) );

        dur = new FixedDuration( 42 );

        assertEquals( 42,
                      dur.getDuration( null ) );
    }

    public void testAdd()
        throws Exception
    {
        FixedDuration dur = new FixedDuration();

        assertEquals( 0,
                      dur.getDuration( null ) );

        dur.addSeconds( 42 );

        assertEquals( 42,
                      dur.getDuration( null ) );

        dur.addMinutes( 2 );

        assertEquals( 162,
                      dur.getDuration( null ) );

        dur.addHours( 2 );

        assertEquals( 7362,
                      dur.getDuration( null ) );

        dur.addDays( 2 );

        assertEquals( 180162,
                      dur.getDuration( null ) );

        dur.addWeeks( 2 );

        assertEquals( 1389762,
                      dur.getDuration( null ) );
    }
}
