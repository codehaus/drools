package org.drools.smf;

import junit.framework.TestCase;

import java.io.File;
import java.io.IOException;

import java.util.Set;

public class SemanticsLoaderTest extends TestCase
{
    private SemanticsRepository repo;
    private SemanticsLoader loader;

    public SemanticsLoaderTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
        this.repo   = new SimpleSemanticsRepository();
        this.loader = new SemanticsLoader( repo );
    }

    public void tearDown()
    {
        this.loader = null;
    }

    public void testLoader_Invalid()
    {
        try
        {
            this.loader.load( new File( "invalid.xml" ) );
            fail( "Should have thrown IOException" );
        }
        catch (IOException e)
        {
            // exptected and correct
        }
        catch (Exception e)
        {
            fail( e.getLocalizedMessage() );
        }
    }

    public void testLoader_Valid()
    {
        try
        {
            this.loader.load( getClass().getResource( "semantics1.xml" ) );

            SemanticModule module = this.repo.lookupSemanticModule( "http://drools.org/test/semantics" );

            assertNotNull( module );

            Set types = module.getObjectTypeNames();

            assertEquals( 1,
                          types.size() );

            assertTrue( types.contains( "testType" ) );

            Class type = module.getObjectType( "testType" );

            assertSame( org.drools.spi.ObjectType.class,
                        type );

            Set extractors = module.getFactExtractorNames();

            assertEquals( 1,
                          extractors.size() );

            assertTrue( extractors.contains( "testExtractor" ) );

            Class extractor = module.getFactExtractor( "testExtractor" );

            assertSame( org.drools.spi.FactExtractor.class,
                        extractor );

            Set consequences = module.getConsequenceNames();

            assertEquals( 1,
                          consequences.size() );

            assertTrue( consequences.contains( "testConsequence" ) );

            Class consequence = module.getConsequence( "testConsequence" );

            assertSame( org.drools.spi.Consequence.class,
                        consequence );
        }
        catch (Exception e)
        {
            fail( e.getLocalizedMessage() );
        }
    }
}
