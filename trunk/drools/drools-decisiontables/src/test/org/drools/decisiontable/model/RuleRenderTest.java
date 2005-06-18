/*
 * Created on 15/05/2005
 */
package org.drools.decisiontable.model;

import org.drools.decisiontable.model.Condition;
import org.drools.decisiontable.model.Consequence;
import org.drools.decisiontable.model.Parameter;
import org.drools.decisiontable.model.Rule;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale</a>
 * 
 * Tests how the rule parse tree renders itself to a rule XML fragment.
 */
public class RuleRenderTest extends TestCase
{

    public void testRuleRender()
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
        cons.setSnippet( "cons snippet;" );
        rule.addConsequence( cons );
        rule.addConsequence( cons );

        Parameter param = new Parameter( );
        param.setClassName( "param class name" );
        param.setComment( "param comment" );
        param.setIdentifier( "param identifier" );
        rule.addParameter( param );
        rule.addParameter( param );
        rule.addParameter( param );

        String xml = rule.toXML( );
        assertNotNull( xml );

        assertTrue( xml.indexOf( "cond snippet" ) != -1 );
        assertTrue( xml.indexOf( "cons snippet" ) != -1 );
        assertTrue( xml.indexOf( "salience=\"42\"" ) != -1 );
        assertTrue( xml.indexOf( "cons snippet;\n\t\tcons snippet;" ) != -1 );

    }

    public void testSalienceCalculator()
    {
        int rowNumber = 2;
        int salience = Rule.calcSalience( rowNumber );
        assertEquals( 65533,
                      salience );
    }

    public void testColNumToColName()
    {
        String colName = Rule.convertColNumToColName( 1 );
        assertEquals( "B",
                      colName );

        colName = Rule.convertColNumToColName( 10 );
        assertEquals( "K",
                      colName );

        colName = Rule.convertColNumToColName( 42 );
        assertEquals( "AQ",
                      colName );

        colName = Rule.convertColNumToColName( 27 );
        assertEquals( "AB",
                      colName );

        colName = Rule.convertColNumToColName( 53 );
        assertEquals( "BB",
                      colName );

    }

    public void testEscapeChars()
    {
        // not needed, as chars should NOT be escaped - using CDATA instead
        // so now am asserting that it is not escaped !
        Condition cond = new Condition( );
        cond.setSnippet( "a < b" );
        assertFalse( cond.toXML( ).indexOf( "a &lt; b" ) != -1 );

        Consequence cons = new Consequence( );
        cons.setSnippet( "a > b" );
        assertFalse( cons.toXML( ).indexOf( "a &gt; b" ) != -1 );
    }
    
    /**
     * This checks that if the rule has "nil" salience, then 
     * no salience value should be put in the rule definition.
     * This allows default salience to work as advertised.
     *
     */
    public void testNilSalience() {
        Rule rule = new Rule("MyRule", null);
        String xml = rule.toXML();
        int idx = xml.indexOf("salience");
        assertEquals(-1, idx);
        
        rule = new Rule("MyRule", new Integer(42));
        xml = rule.toXML();
        idx = xml.indexOf("salience");
        assertTrue(idx > -1);        
    }

}
