package org.drools.io;

import org.drools.smf.SemanticModule;

import junit.framework.TestCase;

public class SemanticsLoaderTest extends TestCase
{
    SemanticsLoader loader;

    public SemanticsLoaderTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
        this.loader = new SemanticsLoader();
    }

    public void tearDown()
    {
        this.loader = null;
    }

    public void testLoad_Invalid() 
    {
        try
        {
            this.loader.load( "goober.goober.goober" );
            fail( "Should have thrown Exception" );
        }
        catch (Exception e)
        {
            // expected and correct
        }
    }

    public void testLoad_Valid() throws Exception
    {
        SemanticModule module = this.loader.load( "org.drools.semantics.java" );

        assertNotNull( module );
    }
}
