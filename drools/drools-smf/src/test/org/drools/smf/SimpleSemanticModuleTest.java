package org.drools.smf;

import java.io.IOException;
import java.util.Enumeration;
import java.util.Set;

import org.drools.DroolsTestCase;
import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.spi.Condition;
import org.drools.spi.Consequence;
import org.drools.spi.Duration;
import org.drools.spi.ObjectType;
import org.drools.spi.RuleBaseContext;

/**
 * 
 * 
 * 
 */

public class SimpleSemanticModuleTest extends DroolsTestCase
{

    public void testAddGetRuleFactory()
    {

        SimpleSemanticModule module = new SimpleSemanticModule( "http://cheese.org" );

        RuleFactory factory = new MockRuleFactory( );

        module.addRuleFactory( "mockCheese",
                               factory );

        assertSame( module.getRuleFactory( "mockCheese" ),
                    factory );

        assertEquals( module.getRuleFactoryNames( ).size( ),
                      1 );

    }

    public void testAddGetObjectTypeFactory()
    {

        SimpleSemanticModule module = new SimpleSemanticModule( "http://cheese.org" );

        ObjectTypeFactory factory = new MockObjectTypeFactory( );

        module.addObjectTypeFactory( "mockCheese",
                                     factory );

        assertSame( module.getObjectTypeFactory( "mockCheese" ),
                    factory );

        assertEquals( module.getObjectTypeFactoryNames( ).size( ),
                      1 );

    }

    public void testAddGetConditionFactory()
    {

        SimpleSemanticModule module = new SimpleSemanticModule( "http://cheese.org" );

        ConditionFactory factory = new MockConditionFactory( );

        module.addConditionFactory( "mockCheese",
                                    factory );

        assertSame( module.getConditionFactory( "mockCheese" ),
                    factory );

        assertEquals( module.getConditionFactoryNames( ).size( ),
                      1 );

    }

    public void testAddGetConsequenceFactory()
    {

        SimpleSemanticModule module = new SimpleSemanticModule( "http://cheese.org" );

        ConsequenceFactory factory = new MockConsequenceFactory( );

        module.addConsequenceFactory( "mockCheese",
                                      factory );

        assertSame( module.getConsequenceFactory( "mockCheese" ),
                    factory );

        assertEquals( module.getConsequenceFactoryNames( ).size( ),
                      1 );

    }

    public void testAddGetDurationFactory()
    {

        SimpleSemanticModule module = new SimpleSemanticModule( "http://cheese.org" );

        DurationFactory factory = new MockDurationFactory( );

        module.addDurationFactory( "mockCheese",
                                   factory );

        assertSame( module.getDurationFactory( "mockCheese" ),
                    factory );

        assertEquals( module.getDurationFactoryNames( ).size( ),
                      1 );

    } 
    
    private class MockRuleFactory
        implements
        RuleFactory
    {

        public Rule newRule(RuleSet ruleSet,
                            RuleBaseContext context,
                            Configuration config) throws FactoryException
        {

            return null;

        }

    }

    private class MockObjectTypeFactory
        implements
        ObjectTypeFactory
    {

        public ObjectType newObjectType(RuleBaseContext context,
                                        Configuration config,
                                        Set imports)
        {

            return null;

        }

    }

    private class MockConditionFactory
        implements
        ConditionFactory
    {

        public Condition[] newCondition(Rule rule,
                                      RuleBaseContext context,
                                      Configuration c)
        {

            return null;

        }

    }

    private class MockConsequenceFactory
        implements
        ConsequenceFactory
    {

        public Consequence newConsequence(Rule rule,
                                          RuleBaseContext context,
                                          Configuration c)
        {

            return null;

        }

    }

    private class MockDurationFactory
        implements
        DurationFactory
    {

        public Duration newDuration(Rule rule,
                                    RuleBaseContext context,
                                    Configuration c)
        {

            return null;

        }

    }

}