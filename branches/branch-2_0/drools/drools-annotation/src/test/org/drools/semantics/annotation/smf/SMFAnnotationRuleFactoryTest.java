package org.drools.semantics.annotation.smf;

import java.util.List;

import junit.framework.TestCase;

import org.drools.RuleBase;
import org.drools.io.RuleBaseLoader;
import org.drools.rule.RuleSet;
import org.drools.smf.FactoryException;
import org.xml.sax.SAXParseException;

public class SMFAnnotationRuleFactoryTest extends TestCase
{

    public void testLoadRulesClassNotFound( ) throws Exception
    {
        try
        {
            RuleBase ruleBase = RuleBaseLoader.loadFromUrl( SMFAnnotationRuleFactoryTest.class
                    .getResource( "for-test-classnotfound.drl" ) );
            fail( "Expected exception" );
        }
        catch (SAXParseException e)
        {
            assertTrue( e.getException( ) instanceof FactoryException );
        }
    }

    public void testLoadRules( ) throws Exception
    {
        RuleBase ruleBase = RuleBaseLoader.loadFromUrl( SMFAnnotationRuleFactoryTest.class
                .getResource( "for-test.drl" ) );

        List< RuleSet > ruleSets = ruleBase.getRuleSets( );
        assertEquals( 1, ruleSets.size( ) );

        RuleSet ruleSet = ruleSets.get( 0 );

        assertEquals( 2, ruleSet.getRules( ).length );
        assertNotNull( ruleSet.getRule( "Rule One" ) );
        assertNotNull( ruleSet.getRule( "Rule Two" ) );
    }
}
