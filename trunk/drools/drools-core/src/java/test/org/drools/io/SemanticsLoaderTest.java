package org.drools.io;

import org.drools.smf.SemanticModule;

import junit.framework.TestCase;

import java.util.Set;

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

        Set names = null;

        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
        //     ObjectType
        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

        names = module.getObjectTypeNames();

        assertEquals( 1,
                      names.size() );

        assertTrue( names.contains( "class" ) );

        assertEquals( org.drools.semantics.java.ClassObjectType.class,
                      module.getObjectType( "class" ) );

        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
        //     Condition
        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

        names = module.getConditionNames();

        assertEquals( 1,
                      names.size() );

        assertTrue( names.contains( "condition" ) );

        assertEquals( org.drools.semantics.java.ExprCondition.class,
                      module.getCondition( "condition" ) );

        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
        //     FactExtractor
        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

        names = module.getFactExtractorNames();

        assertEquals( 1,
                      names.size() );

        assertTrue( names.contains( "extractor" ) );

        assertEquals( org.drools.semantics.java.ExprExtractor.class,
                      module.getFactExtractor( "extractor" ) );

        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
        //     Consequence
        // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

        names = module.getConsequenceNames();

        assertEquals( 1,
                      names.size() );

        System.err.println( "conseq names: " + names );

        assertTrue( names.contains( "consequence" ) );

        assertEquals( org.drools.semantics.java.BlockConsequence.class,
                      module.getConsequence( "consequence" ) );   
    }
}
