package org.drools.semantics.java;

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

    public void testEvaluate() throws Exception
    {
        this.interp.setText( "42 + 12" );

        assertEquals( new Integer(54),
                      this.interp.evaluate() );
    }

    public void testEvaluate_Variables() throws Exception
    {
        this.interp.setText( "a + b" );

        this.interp.setVariable( "a",
                                 new Integer(42) );

        this.interp.setVariable( "b",
                                 new Integer(12) );

        assertEquals( new Integer(54),
                      this.interp.evaluate() );
    }
}
