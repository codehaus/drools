package org.drools.semantics.python;

import org.drools.rule.Declaration;
import org.drools.spi.MockTuple;
import org.drools.semantics.java.ClassObjectType;

import junit.framework.TestCase;

public class EvalTest extends TestCase
{
    private Eval eval;
    private Declaration aDecl;
    private Declaration bDecl;

    public EvalTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
        this.eval = new Eval();
        this.aDecl = new Declaration( new ClassObjectType( Integer.class ),
                                      "a" );
        this.bDecl = new Declaration( new ClassObjectType( Integer.class ),
                                      "b" );
    }

    public void tearDown()
    {
        this.eval = null;
    }

    public void testSetText()
    {
        this.eval.setText( "42 + 42" );

        assertNotNull( this.eval.getCode() );
    }

    public void testEvaluate_NoArg()
    {
        this.eval.setText( "42 + 12" );

        assertEquals( new Integer( 54 ),
                      this.eval.evaluate() );
    }

    public void testEvaluate_WithTuple()
    {
        this.eval.setText( "a + b" );

        MockTuple tuple = new MockTuple();

        tuple.put( this.aDecl,
                   new Integer( 42 ) );

        tuple.put( this.bDecl,
                   new Integer( 12 ) );

        assertEquals( new Integer( 54 ),
                      this.eval.evaluate( tuple ) );
    }
}

