package org.drools.semantics.java;

import org.drools.rule.Declaration;
import org.drools.spi.MockTuple;

import bsh.EvalError;
import bsh.NameSpace;

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

        NameSpace ns = new NameSpace( "" );

        assertEquals( new Integer(54),
                      this.interp.evaluate( ns ) );
    }

    public void testEvaluate_Variables() throws Exception
    {
        NameSpace ns = new NameSpace( "" );

        this.interp.setText( "a + b" );
        
        ns.setVariable( "a",
                        new Integer(42) );
        
        ns.setVariable( "b",
                        new Integer(12) );
        
        assertEquals( new Integer(54),
                      this.interp.evaluate( ns ) );
    }

    public void testEvaluate_Tuple() throws Exception
    {
        MockTuple tuple = new MockTuple();

        tuple.put( new Declaration( new ClassObjectType( java.lang.Number.class ),
                                    "a" ),
                   new Integer(42) );

        tuple.put( new Declaration( new ClassObjectType( java.lang.Number.class ),
                                    "b" ),
                   new Integer(12) );

        this.interp.setText( "a + b" );

        assertEquals( new Integer(54),
                      this.interp.evaluate( tuple ) );

        tuple = new MockTuple();

        try
        {
            this.interp.evaluate( tuple );
            fail( "Should have thrown EvalError.  No variables set." );
        }
        catch (EvalError e)
        {
            // expected and correct
        }
    }
}