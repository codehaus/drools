/*
 * Created on 14/05/2005
 *
 */
package org.drools.decisiontable.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import junit.framework.TestCase;

import org.drools.decisiontable.model.Condition;
import org.drools.decisiontable.model.Consequence;
import org.drools.decisiontable.model.Import;
import org.drools.decisiontable.model.Parameter;
import org.drools.decisiontable.model.Rule;
import org.drools.decisiontable.model.Ruleset;
import org.drools.decisiontable.parser.RuleSheetListener;

/**
 * @author Shaun Addison, Michael Neale
 * 
 * Test an excel file.
 * 
 * Assumes it has a sheet called "Decision Tables" with a rule table identified
 * by a "RuleTable" cell
 */
public class RuleWorksheetParseTest extends TestCase
{

    public void testBasicWorkbookProperties() throws Exception
    {

        InputStream stream = RuleWorksheetParseTest.class.getResourceAsStream( "/data/BasicWorkBook.xls" );

        RuleSheetListener listener = getRuleSheetListener( stream );

        Properties props = listener.getProperties( );
        assertNotNull( props );
        assertEquals( "myruleset",
                      props.getProperty( "RuleSet" ) );
        assertEquals( "someMisc",
                      props.getProperty( "misc" ) );
        /*
         * System.out.println("Here are the global properties...");
         * listener.getProperties().list(System.out);
         */
    }

    public void testComplexWorkbookProperties() throws Exception
    {

        InputStream stream = RuleWorksheetParseTest.class.getResourceAsStream( "/data/ComplexWorkbook.xls" );
        RuleSheetListener listener = getRuleSheetListener( stream );

        Properties props = listener.getProperties( );
        assertNotNull( props );
        String ruleSetName = props.getProperty( "RuleSet" );
        assertEquals( "ruleSetName",
                      ruleSetName );

    }

    public void testWorkbookParse() throws Exception
    {
        InputStream stream = RuleWorksheetParseTest.class.getResourceAsStream( "/data/BasicWorkBook.xls" );
        RuleSheetListener listener = getRuleSheetListener( stream );

        Ruleset ruleset = listener.getRuleSet( );
        assertNotNull( ruleset );

        Rule firstRule = (Rule) ruleset.getRules( ).get( 0 );
        assertNotNull(firstRule.getSalience( ));
        assertTrue(firstRule.getSalience( ).intValue() > 0);

        // System.out.println(ruleset.toXML());

        assertEquals( "myruleset",
                      ruleset.getName( ) );
        assertEquals( 2,
                      ruleset.getImports( ).size( ) );
        assertEquals( 6,
                      ruleset.getRules( ).size( ) );

        // check imports
        Import imp = (Import) ruleset.getImports( ).get( 0 );
        assertEquals( "blah.class1",
                      imp.getClassName( ) );
        imp = (Import) ruleset.getImports( ).get( 1 );
        assertEquals( "blah.class2",
                      imp.getClassName( ) );

        // check rules
        Rule rule = (Rule) ruleset.getRules( ).get( 0 );
        Parameter param = (Parameter) rule.getParameters( ).get( 1 );
        assertEquals( "au.com.nothing.ParamClass2",
                      param.getClassName( ) );
        Condition cond = (Condition) rule.getConditions( ).get( 0 );
        assertEquals( "myObject.getColour().equals(red)",
                      cond.getSnippet( ) );

        Consequence cons = (Consequence) rule.getConsequences( ).get( 0 );
        assertNotNull( cons );
        assertEquals( "myObject.setIsValid(Y)",
                      cons.getSnippet( ) );

        rule = (Rule) ruleset.getRules( ).get( 5 );
        cond = (Condition) rule.getConditions( ).get( 1 );
        assertEquals( "myObject.size () > 7",
                      cond.getSnippet( ) );
        cons = (Consequence) rule.getConsequences( ).get( 0 );
        assertEquals( "myObject.setIsValid(10-Jul-1974)",
                      cons.getSnippet( ) );

    }

    /**
     * See if it can cope with odd shaped rule table, including missing
     * conditions. Also is not "sequential".
     */
    public void testComplexWorksheetMissingConditions() throws Exception
    {
        InputStream stream = RuleWorksheetParseTest.class.getResourceAsStream( "/data/ComplexWorkbook.xls" );
        RuleSheetListener listener = getRuleSheetListener( stream );

        Ruleset ruleset = listener.getRuleSet( );
        assertEquals( 6,
                      ruleset.getRules( ).size( ) );
        assertEquals( 0,
                      ruleset.getImports( ).size( ) );

        Rule rule = (Rule) ruleset.getRules( ).get( 0 );
        assertEquals( 3,
                      rule.getConditions( ).size( ) );
        assertEquals( 2,
                      rule.getConsequences( ).size( ) );
        Consequence cons = (Consequence) rule.getConsequences( ).get( 1 );
        assertEquals( "myObject.setIsValid(1, 2)",
                      cons.getSnippet( ) );
        Condition con = (Condition) rule.getConditions( ).get( 2 );
        assertEquals( "myObject.size() < 3",
                      con.getSnippet( ) );

        rule = (Rule) ruleset.getRules( ).get( 4 );

        // this should have less conditions
        assertEquals( 1,
                      rule.getConditions( ).size( ) );

        rule = (Rule) ruleset.getRules( ).get( 5 );
        assertEquals( 2,
                      rule.getConditions( ).size( ) );
        assertEquals( 1,
                      rule.getConsequences( ).size( ) );

    }

    /**
     * Utility method showing how to get a rule sheet listener from a stream.
     */
    public static RuleSheetListener getRuleSheetListener(InputStream stream) throws IOException
    {
        return RulesheetUtil.getRuleSheetListener( stream );
    }

}
