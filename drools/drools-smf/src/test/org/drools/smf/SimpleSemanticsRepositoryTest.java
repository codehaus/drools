package org.drools.smf;

import org.drools.DroolsTestCase;

public class SimpleSemanticsRepositoryTest
    extends DroolsTestCase
{
    public void testRegisterLookup()
        throws Exception
    {
        SimpleSemanticsRepository repo = new SimpleSemanticsRepository();

        try
        {
            repo.lookupSemanticModule( "http://cheese.org/" );

            fail( "should have thrown NoSuchSemanticModuleException" );
        }
        catch (NoSuchSemanticModuleException e)
        {
            // expected and correct

            assertEquals( "http://cheese.org/",
                          e.getUri() );

            assertEquals( "no such semantic module: http://cheese.org/",
                          e.getMessage() );
        }

        SimpleSemanticModule module = new SimpleSemanticModule( "http://cheese.org/" );

        repo.registerSemanticModule( module );

        assertSame( module,
                    repo.lookupSemanticModule( "http://cheese.org/" ) );
    }
}
