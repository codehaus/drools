package org.drools.semantics.python;

import org.drools.semantics.java.ClassObjectType;

import org.drools.rule.Declaration;
import org.drools.spi.MockTuple;
import org.drools.spi.ConsequenceException;

import org.python.core.PyException;

import junit.framework.TestCase;

public class BlockConsequenceTest extends TestCase
{
    private boolean poked;
    private boolean prodded;

    public BlockConsequenceTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
        this.poked = false;
        this.prodded = false;
    }

    public void tearDown()
    {
        this.poked = false;
        this.prodded = false;
    }

    public void testInvoke_NoText()
    {
        BlockConsequence conseq = new BlockConsequence();

        MockTuple tuple = new MockTuple();

        try
        {
            conseq.invoke( tuple,
                           null );
        }
        catch (ConsequenceException e)
        {
            // expected and correct
            NullPointerException npe = (NullPointerException) e.getRootCause();
        }
    }

    public void testInvoke_Valid() throws Exception
    {
        BlockConsequence conseq = new BlockConsequence();

        conseq.setText( "test.poke();test.prod();" );

        MockTuple tuple = new MockTuple();

        tuple.put( new Declaration( new ClassObjectType( BlockConsequenceTest.class ),
                                    "test" ),
                   this );

        conseq.invoke( tuple,
                       null );

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
