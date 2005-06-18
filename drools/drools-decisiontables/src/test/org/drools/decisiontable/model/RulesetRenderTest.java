/*
 * Created on 16/05/2005
 */
package org.drools.decisiontable.model;

import org.drools.decisiontable.model.Condition;
import org.drools.decisiontable.model.Consequence;
import org.drools.decisiontable.model.Import;
import org.drools.decisiontable.model.Parameter;
import org.drools.decisiontable.model.Rule;
import org.drools.decisiontable.model.Ruleset;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale</a>
 * 
 * Test rendering and running a whole sample ruleset, from the model classes
 * down.
 */
public class RulesetRenderTest extends TestCase
{

    public Rule buildRule()
    {
        Rule rule = new Rule( "myrule",
                              new Integer(42) );
        rule.setComment( "rule comments" );

        Condition cond = new Condition( );
        cond.setComment( "cond comment" );
        cond.setSnippet( "cond snippet" );
        rule.addCondition( cond );

        Consequence cons = new Consequence( );
        cons.setComment( "cons comment" );
        cons.setSnippet( "cons snippet" );
        rule.addConsequence( cons );

        Parameter param = new Parameter( );
        param.setClassName( "param class name" );
        param.setComment( "param comment" );
        param.setIdentifier( "param identifier" );
        rule.addParameter( param );
        rule.addParameter( param );
        rule.addParameter( param );

        return rule;
    }

    public void testRulesetRender()
    {
        Ruleset ruleSet = new Ruleset( "myruleset" );
        ruleSet.addRule( buildRule( ) );
        Import imp = new Import( );
        imp.setClassName( "clazz name" );
        imp.setComment( "import comment" );

        ruleSet.addImport( imp );
        String xml = ruleSet.toXML( );
        assertNotNull( xml );

    }

}
