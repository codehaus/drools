
package org.drools.semantic.java;

import org.drools.spi.FactExtractionException;

import junit.framework.TestCase;

public class JavaBeanFactExtractorTest extends TestCase
{
    public JavaBeanFactExtractorTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
    }

    public void tearDown()
    {
    }

    public void testSimpleProperty()
    {
        String str = "cheese";

        JavaBeanFactExtractor extractor = new JavaBeanFactExtractor( null,
                                                                     "class" );

        try
        {
            Object cls = extractor.extractFactFromObject( str );
            
            assertNotNull( cls );

            assertSame( String.class,
                        cls );
        }
        catch (FactExtractionException e)
        {
            e.printStackTrace();
            fail( e.toString() );
        }
    }

    public void testNestedProperty()
    {
        String str = "cheese";

        JavaBeanFactExtractor extractor = new JavaBeanFactExtractor( null,
                                                                     "class.name" );

        try
        {
            Object name = extractor.extractFactFromObject( str );
            
            assertNotNull( name );

            assertEquals( "java.lang.String",
                          name );
        }
        catch (FactExtractionException e)
        {
            e.printStackTrace();
            fail( e.toString() );
        }
    }
}
