package org.drools.bsh;

import junit.framework.TestCase;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.io.RuleSetLoader;
import org.drools.jsr94.rules.RuleServiceProviderImpl;
import org.drools.rule.RuleSet;

import javax.rules.RuleRuntime;
import javax.rules.RuleServiceProvider;
import javax.rules.RuleServiceProviderManager;
import javax.rules.StatelessRuleSession;
import javax.rules.admin.LocalRuleExecutionSetProvider;
import javax.rules.admin.RuleAdministrator;
import javax.rules.admin.RuleExecutionSet;
import java.io.FileReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.HashMap;

public class BeanShellTest extends TestCase
{
    private static String BEAN_SHELL_TEST_RULES = "src/java/test/org/drools/bsh/BeanShellTest.drl";

    public BeanShellTest( String name )
    {
        super( name );
    }

    public void testBshCommands() throws Exception
    {
        RuleBase ruleBase = new RuleBase();
        RuleSetLoader loader = new RuleSetLoader();

        //List ruleSets = loader.load( getClass().getResource( "BeanShellTest.drl" ) );
        List ruleSets = loader.load( new FileReader( BEAN_SHELL_TEST_RULES ) );

        assertNotNull( ruleSets );
        assertEquals( 1, ruleSets.size() );

        Iterator ruleSetIter = ruleSets.iterator();
        RuleSet eachRuleSet = null;

        while ( ruleSetIter.hasNext() )
        {
            eachRuleSet = (RuleSet) ruleSetIter.next();
            ruleBase.addRuleSet( eachRuleSet );
        }

        List list = new ArrayList();
        HashMap appData = new HashMap();
        appData.put( "rules.fired", list );

        WorkingMemory workingMemory = ruleBase.createWorkingMemory();
        workingMemory.setApplicationData( appData );

        workingMemory.assertObject( "testAssert" );

        assertEquals( 3, list.size() );
        assertEquals( "testAssert", list.get( 0 ) );
        assertEquals( "testModify", list.get( 1 ) );
        assertEquals( "testRetract", list.get( 2 ) );
    }

    public void testBshCommandsThroughJSR94() throws Exception
    {
        // First, setup the RuleServiceProvider and RuleAdministrator
        RuleServiceProviderManager.registerRuleServiceProvider( "http://drools.org/rules", RuleServiceProviderImpl.class );
        RuleServiceProvider ruleServiceProvider = RuleServiceProviderManager.getRuleServiceProvider( "http://drools.org/rules" );
        RuleAdministrator ruleAdministrator = ruleServiceProvider.getRuleAdministrator();

        // Load the rules
        LocalRuleExecutionSetProvider ruleSetProvider = ruleAdministrator.getLocalRuleExecutionSetProvider( null );
        Reader ruleReader = new FileReader( BEAN_SHELL_TEST_RULES );
        RuleExecutionSet ruleSet = ruleSetProvider.createRuleExecutionSet( ruleReader, null );

        // register the rule set
        ruleAdministrator.registerRuleExecutionSet( BEAN_SHELL_TEST_RULES, ruleSet, null );

        List list = new ArrayList();
        HashMap appData = new HashMap();
        appData.put( "rules.fired", list );

        // obtain the stateless rule session
        RuleRuntime ruleRuntime = ruleServiceProvider.getRuleRuntime();
        StatelessRuleSession session = (StatelessRuleSession) ruleRuntime.createRuleSession( BEAN_SHELL_TEST_RULES, appData, RuleRuntime.STATELESS_SESSION_TYPE );

        List inObjects = new ArrayList();
        inObjects.add( "testAssert" );

        // execute the rules
        List outList = session.executeRules( inObjects );

        session.release();

        assertEquals( 3, list.size() );
        assertEquals( "testAssert", list.get( 0 ) );
        assertEquals( "testModify", list.get( 1 ) );
        assertEquals( "testRetract", list.get( 2 ) );
    }
}
