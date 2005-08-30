package org.drools.semantics.java;

import junit.framework.TestCase;

import org.drools.rule.RuleSet;
import org.drools.smf.DefaultImporter;
import org.drools.spi.RuleBaseContext;

public class JavaFunctionsTest extends TestCase
{

    public void testJavaFunctionsCompilationException() throws Exception
    {
        RuleSet ruleSet = new RuleSet( "test RuleSet",
                                       new RuleBaseContext( ) );
        
        ruleSet.setImporter( new DefaultImporter( ) );

        // Test JavaCondition throws CompilationException
        String text = "x";
        try
        {
            new JavaFunctions( ruleSet,
                               0,
                               text );
            fail( "should throw CompilationException" );
        }
        catch ( CompilationException e )
        {
            //
        }

    }

}
