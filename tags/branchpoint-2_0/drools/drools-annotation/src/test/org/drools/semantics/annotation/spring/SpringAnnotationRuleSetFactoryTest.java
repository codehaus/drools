package org.drools.semantics.annotation.spring;

import java.util.Map;

import java.util.logging.Logger;
import java.util.logging.Level;
import org.drools.RuleBase;
import org.drools.RuleBaseBuilder;
import org.drools.WorkingMemory;
import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.semantics.annotation.testrules.AuditService;
import org.drools.semantics.annotation.testrules.FooBar;
import org.springframework.beans.BeansException;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import junit.framework.TestCase;

public class SpringAnnotationRuleSetFactoryTest extends TestCase
{
    private static boolean isStaticSetupComplete;
    private static ConfigurableApplicationContext context;

    protected void setUp( ) throws Exception
    {
        super.setUp( );
        if (!isStaticSetupComplete)
        {
            staticSetUp( );
            isStaticSetupComplete = true;
        }
    }

    protected void tearDown( ) throws Exception
    {
        super.tearDown( );
    }

    protected void staticSetUp( ) throws Exception
    {
        Logger.getLogger( "org.springframework" ).setLevel( Level.WARNING );
        context = new ClassPathXmlApplicationContext(
                "org/drools/semantics/annotation/spring/for-test.appctx.xml" );
        registerStaticTearDownShutdownHook( );
    }

    protected void staticTearDown( ) throws Exception
    {
        context.close( );
    }

    private void registerStaticTearDownShutdownHook( )
    {
        Runtime.getRuntime( ).addShutdownHook( new Thread( new Runnable( ) {
            public void run( )
            {
                try
                {
                    staticTearDown( );
                }
                catch (Exception e)
                {
                    throw new RuntimeException( e );
                }
            }
        } ) );
    }

    public void testGetObjectType( ) throws Exception
    {
        SpringAnnotationRuleSetFactory setFactory = new SpringAnnotationRuleSetFactory( );
        assertSame( RuleSet.class, setFactory.getObjectType( ) );
    }

    public void testIsSingleton( ) throws Exception
    {
        SpringAnnotationRuleSetFactory setFactory = new SpringAnnotationRuleSetFactory( );
        assertFalse( setFactory.isSingleton( ) );
    }

    public void testRulesMissingRuleSetName( ) throws Exception
    {
        RuleSet ruleSet;
        try
        {
            ruleSet = (RuleSet) context.getBean( "ruleSet_missingRuleSetName" );
            fail( "expected BeansException" );
        }
        catch (BeansException e)
        {
            assertTrue( e.getCause( ).getClass( ) == IllegalArgumentException.class );
        }
    }

    public void testRulesMissingRuleName( ) throws Exception
    {
        RuleSet ruleSet;
        try
        {
            ruleSet = (RuleSet) context.getBean( "ruleSet_missingRuleName" );
            fail( "expected BeansException" );
        }
        catch (BeansException e)
        {
            assertTrue( e.getCause( ).getClass( ) == IllegalArgumentException.class );
        }
    }

    public void testRulesMissingPojo( ) throws Exception
    {
        RuleSet ruleSet;
        try
        {
            ruleSet = (RuleSet) context.getBean( "ruleSet_missingPojo" );
            fail( "expected BeansException" );
        }
        catch (BeansException e)
        {
            assertTrue( e.getCause( ).getClass( ) == IllegalArgumentException.class );
        }
    }

    public void testRulesExplicitProperties( ) throws Exception
    {
        RuleSet ruleSet = (RuleSet) context.getBean( "ruleSet_explicitRuleProperties" );

        assertEquals( 2, ruleSet.getRules( ).length );

        Rule rule_1 = ruleSet.getRule( "Rule One" );
        assertNotNull( rule_1 );
        assertEquals( 88, rule_1.getSalience( ) );
        assertTrue( rule_1.getNoLoop( ) );
        assertEquals( "RuleOne", rule_1.getDocumentation( ) );

        Rule rule_2 = ruleSet.getRule( "Rule Two" );
        assertNotNull( rule_2 );
        assertEquals( 99, rule_2.getSalience( ) );
        assertFalse( rule_2.getNoLoop( ) );
        assertEquals( "RuleTwo", rule_2.getDocumentation( ) );
    }

    public void testInjectedRule( ) throws Exception
    {
        RuleSet ruleSet = (RuleSet) context.getBean( "ruleSet_injectedRules" );
        assertEquals( 2, ruleSet.getRules( ).length );
        AuditService auditService = (AuditService) context.getBean( "auditService" );

        RuleBaseBuilder builder = new RuleBaseBuilder( );
        builder.addRuleSet( ruleSet );
        RuleBase ruleBase = builder.build( );
        WorkingMemory workingMemory = ruleBase.newWorkingMemory( );

        FooBar fooBar1 = new FooBar( "foo-bar-1" );
        FooBar fooBar2 = new FooBar( "foo-bar-2" );

        // should match condition of injected-rule-1 but not injected-rule-2
        fooBar1.setMin( 11 );
        fooBar1.setMax( 19 );

        // should match condition of injected-rule-2 but not injected-rule-1
        fooBar2.setMin( 31 );
        fooBar2.setMax( 39 );

        workingMemory.assertObject( fooBar1 );
        workingMemory.assertObject( fooBar2 );
        workingMemory.fireAllRules( );

        Map< String, Integer > auditValues = auditService.getValues( );
        int fooBar1Value = auditValues.get( "injected-rule-1:foo-bar-1" );
        assertEquals( 99, fooBar1Value );
        int fooBar2Value = auditValues.get( "injected-rule-2:foo-bar-2" );
        assertEquals( 77, fooBar2Value );
    }

}
