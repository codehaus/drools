/*
 * Created on 16/05/2005
 */
package org.drools.decisiontable;

import java.io.StringReader;

import org.drools.RuleBase;
import org.drools.decisiontable.model.Condition;
import org.drools.decisiontable.model.Consequence;
import org.drools.decisiontable.model.Parameter;
import org.drools.decisiontable.model.Rule;
import org.drools.decisiontable.model.Ruleset;
import org.drools.io.RuleBaseLoader;
import org.xml.sax.SAXParseException;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale</a>
 * 
 * Basic test to cover using ruleset to DRL conversion.
 */
public class RulesetToDRLTest extends TestCase
{

    public void testBuildRuleBaseBasicParsing() throws Exception
    {
        String ruleText = getTestRuleSet( ).toXML( );
        RuleBase ruleBase = RuleBaseLoader.loadFromReader( new StringReader( ruleText ) );
        assertNotNull( ruleBase );
    }

    public static Ruleset getTestRuleSet()
    {
        Ruleset ruleSet = new Ruleset( "name" ); //$NON-NLS-1$
        Rule rule = new Rule( "rule", new Integer(1) ); //$NON-NLS-1$
        rule.setComment( "a comment" ); //$NON-NLS-1$

        Condition cond = new Condition( );
        cond.setSnippet( "System.currentTimeMillis() > 42 /* comment here */" );
        rule.addCondition( cond );

        Consequence cons = new Consequence( );
        cons.setSnippet( "System.out.println(\"yeah\");" );
        rule.addConsequence( cons );

        Parameter param = new Parameter( );
        param.setClassName( "java.lang.String" ); //$NON-NLS-1$
        param.setIdentifier( "s" );
        param.setComment( "param comment" ); //$NON-NLS-1$

        rule.addParameter( param );
        ruleSet.addRule( rule );

        return ruleSet;
    }

    public void testBuildRuleBaseBadFormedXML() throws Exception
    {
        Rule rule = new Rule( "rule",
                              new Integer(1) );
        rule.setComment( "a comment" );
        Condition cond = new Condition( );

        cond.setSnippet( "<badformedxml>" );
        rule.addCondition( cond );

        Ruleset ruleset = new Ruleset( "xyz" );
        ruleset.addRule( rule );
        try
        {
            RuleBaseLoader.loadFromReader( new StringReader( ruleset.toXML( ) ) );
            fail( );
        }
        catch ( SAXParseException e )
        {
            System.out.println( e.getMessage( ) );
        }
    }

    public void testBuildRuleBaseBadSnippet()
    {
        Rule rule = new Rule( "rule", new Integer(1) ); //$NON-NLS-1$
        rule.setComment( "a comment" ); //$NON-NLS-1$
        Condition cond = new Condition( );

        // TODO: find out why run out of memory when use the // comment notation
        cond.setSnippet( "this.is.not.java /*this.is.not.java*/" ); //$NON-NLS-1$
        rule.addCondition( cond );

        Ruleset ruleset = new Ruleset( "xyz" ); //$NON-NLS-1$
        ruleset.addRule( rule );

        try
        {
            RuleBaseLoader.loadFromReader( new StringReader( ruleset.toXML( ) ) );
            fail( );
        }
        catch ( Exception e )
        {
            System.out.println( e.toString( ) );
        }
    }

}
