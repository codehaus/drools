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

    public void testSetText()
    {
        this.interp.setText( "42 + 42", "eval" );

        assertNotNull( this.interp.getCode() );
    }
}
