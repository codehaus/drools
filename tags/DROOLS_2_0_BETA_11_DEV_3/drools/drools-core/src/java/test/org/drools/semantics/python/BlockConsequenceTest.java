package org.drools.semantics.python;

import org.drools.semantics.java.ClassObjectType;

import org.drools.WorkingMemory;
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
                           new TestWorkingMemory() );
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

        conseq.setText( "test.poke();test.prod(appData);" );

        MockTuple tuple = new MockTuple();

        tuple.put( new Declaration( new ClassObjectType( BlockConsequenceTest.class ),
                                    "test" ),
                   this );

        WorkingMemory memory = new TestWorkingMemory();
        memory.setApplicationData( "This is app data" );
        
        conseq.invoke( tuple,
                       memory );

        assertTrue( this.poked );
        assertTrue( this.prodded );
    }

    public void poke()
    {
        this.poked = true;
    }

    public void prod( String appData )
    {
        if ( appData.equals( "This is app data" ) )
        {
            this.prodded = true;
        }
    }

    /** Simple subclass so we can call the protected constructor */
    private static class TestWorkingMemory extends WorkingMemory
    {
        public TestWorkingMemory()
        {
            super( null );
        }
    }
}
