package org.drools.semantics.python;

import org.drools.rule.Declaration;
import org.drools.spi.MockTuple;
import org.drools.semantics.java.ClassObjectType;

import junit.framework.TestCase;

public class ExecTest extends TestCase
{
    private Exec exec;
    private Declaration testDecl;

    private boolean poked;
    private boolean prodded;

    public ExecTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
        this.exec = new Exec();
        this.testDecl = new Declaration( new ClassObjectType( ExecTest.class ),
                                      "test" );

        this.poked = false;
        this.prodded = false;
    }

    public void tearDown()
    {
        this.exec     = null;
        this.testDecl = null;
        this.poked    = false;
        this.prodded  = false;
    }

    public void testSetText()
    {
        this.exec.setText( "42 + 42" );

        assertNotNull( this.exec.getCode() );
    }

    public void testExecute()
    {
        MockTuple tuple = new MockTuple();

        tuple.put( this.testDecl,
                   this );

        this.exec.setText( "test.poke()\ntest.prod()\n" );

        this.exec.execute( tuple );

        assertTrue( this.poked );
        assertTrue( this.prodded );
    }

    public void poke()
    {
        this.poked = true;
    }

    public void prod()
    {
        this.prodded = true;
    }
}

