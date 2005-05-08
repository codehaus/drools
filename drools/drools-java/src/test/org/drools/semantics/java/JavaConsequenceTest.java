package org.drools.semantics.java;

import junit.framework.TestCase;

import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.smf.DefaultImporter;
import org.drools.spi.RuleBaseContext;

public class JavaConsequenceTest extends TestCase
{

    public void testJavaConditionCompilationException() throws Exception
    {
        RuleSet ruleSet = new RuleSet( "test RuleSet",
                                       new RuleBaseContext( ) );
        Rule rule = new Rule( "Test Rule 1",
                              ruleSet );
        rule.setImporter( new DefaultImporter( ) );

        // Test JavaCondition throws CompilationException
        String text = "x";
        try
        {
            new JavaCondition( rule,
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
