package org.drools.smf;

import org.drools.DroolsTestCase;
import org.drools.spi.MockObjectType;
import org.drools.spi.InstrumentedCondition;
import org.drools.spi.InstrumentedExtractor;
import org.drools.spi.InstrumentedConsequence;

public class SimpleSemanticModuleTest
    extends DroolsTestCase
{
    public void testConstruct()
        throws Exception
    {
        SimpleSemanticModule module = new SimpleSemanticModule( "http://cheese.org/" );

        assertEquals( "http://cheese.org/",
                      module.getUri() );

        assertTrue( module.getObjectTypeNames().isEmpty() );
        assertTrue( module.getConditionNames().isEmpty() );
        assertTrue( module.getExtractorNames().isEmpty() );
        assertTrue( module.getConsequenceNames().isEmpty() );
    }

    public void testAddInvalidComponents()
        throws Exception
    {
        SimpleSemanticModule module = new SimpleSemanticModule( "http://cheese.org/" );

        try
        {
            module.addObjectType( "cheese",
                                  Object.class );

            fail( "should have thrown InvalidObjectTypeException" );
        }
        catch (InvalidObjectTypeException e)
        {
            // expected and correct
            assertSame( Object.class,
                        e.getInvalidClass() );
        }

        try
        {
            module.addCondition( "cheese",
                                  Object.class );

            fail( "should have thrown InvalidConditionException" );
        }
        catch (InvalidConditionException e)
        {
            // expected and correct
            assertSame( Object.class,
                        e.getInvalidClass() );
        }

        try
        {
            module.addExtractor( "cheese",
                                 Object.class );

            fail( "should have thrown InvalidExtractorException" );
        }
        catch (InvalidExtractorException e)
        {
            // expected and correct
            assertSame( Object.class,
                        e.getInvalidClass() );
        }

        try
        {
            module.addConsequence( "cheese",
                                   Object.class );

            fail( "should have thrown InvalidConsequenceException" );
        }
        catch (InvalidConsequenceException e)
        {
            // expected and correct
            assertSame( Object.class,
                        e.getInvalidClass() );
        }
    }

    public void testAddValidComponents()
        throws Exception
    {
        SimpleSemanticModule module = new SimpleSemanticModule( "http://cheese.org/" );

        module.addObjectType( "objectType",
                              MockObjectType.class );

        assertEquals( 1,
                      module.getObjectTypeNames().size() );

        assertTrue( module.getObjectTypeNames().contains( "objectType" ) );

        assertSame( MockObjectType.class,
                    module.getObjectType( "objectType" ) );

        module.addCondition( "condition",
                             InstrumentedCondition.class );

        assertEquals( 1,
                      module.getConditionNames().size() );

        assertTrue( module.getConditionNames().contains( "condition" ) );

        assertSame( InstrumentedCondition.class,
                    module.getCondition( "condition" ) );

        module.addExtractor( "extractor",
                             InstrumentedExtractor.class );

        assertEquals( 1,
                      module.getExtractorNames().size() );

        assertTrue( module.getExtractorNames().contains( "extractor" ) );

        assertSame( InstrumentedExtractor.class,
                    module.getExtractor( "extractor" ) );

        module.addConsequence( "consequence",
                               InstrumentedConsequence.class );

        assertEquals( 1,
                      module.getConsequenceNames().size() );

        assertTrue( module.getConsequenceNames().contains( "consequence" ) );

        assertSame( InstrumentedConsequence.class,
                    module.getConsequence( "consequence" ) );
                              
    }
}
