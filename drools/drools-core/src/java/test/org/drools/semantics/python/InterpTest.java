package org.drools.semantics.python;

import junit.framework.TestCase;

public class InterpTest extends TestCase
{
    private Interp interp;

    public InterpTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
        this.interp = new Interp();
    }

    public void tearDown()
    {
        this.interp = null;
    }

    public void testNothing()
    {
        this.interp.setText( "42 + 12" );

        assertEquals( new Integer( 54 ),
                      this.interp.evaluate() );
    }
}
