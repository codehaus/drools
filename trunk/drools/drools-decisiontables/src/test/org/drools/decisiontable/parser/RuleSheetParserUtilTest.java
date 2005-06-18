/*
 * Created on 23/05/2005
 */
package org.drools.decisiontable.parser;

import java.util.List;

import org.drools.decisiontable.model.Import;
import org.drools.decisiontable.model.Parameter;
import org.drools.decisiontable.parser.RuleSheetParserUtil;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale</a>
 * 
 * Nuff said...
 */
public class RuleSheetParserUtilTest extends TestCase
{

    public void testRuleName()
    {
        String row = "RuleTable       This is my rule name     (com.something a, com.something b)";
        String result = RuleSheetParserUtil.getRuleName( row );
        assertEquals( "This is my rule name",
                      result );
    }

    public void testGetParameterList()
    {
        String row = "RuleTable       This is my rule name     (com.something a1, com.somethingelse b1)";
        List list = RuleSheetParserUtil.getParameterList( row );
        assertNotNull( list );
        assertEquals( 2,
                      list.size( ) );
        Parameter param = (Parameter) list.get( 0 );
        assertEquals( "com.something",
                      param.getClassName( ) );
        assertEquals( "a1",
                      param.getIdentifier( ) );

        param = (Parameter) list.get( 1 );
        assertEquals( "com.somethingelse",
                      param.getClassName( ) );
        assertEquals( "b1",
                      param.getIdentifier( ) );

        row = "RuleTable       This is my rule name     (com.something a1)";
        list = RuleSheetParserUtil.getParameterList( row );
        assertEquals( 1,
                      list.size( ) );
        param = (Parameter) list.get( 0 );
        assertEquals( "com.something",
                      param.getClassName( ) );
        assertEquals( "a1",
                      param.getIdentifier( ) );

    }

    public void testIsSequential()
    {
        assertTrue( RuleSheetParserUtil.isStringMeaningTrue( "true" ) );
        assertTrue( RuleSheetParserUtil.isStringMeaningTrue( "TRUE" ) );
        assertTrue( RuleSheetParserUtil.isStringMeaningTrue( "yes" ) );
        assertTrue( RuleSheetParserUtil.isStringMeaningTrue( "oN" ) );

        assertFalse( RuleSheetParserUtil.isStringMeaningTrue( "no" ) );
        assertFalse( RuleSheetParserUtil.isStringMeaningTrue( "false" ) );
        assertFalse( RuleSheetParserUtil.isStringMeaningTrue( null ) );
    }

    public void testListImports()
    {
        String cellVal = null;
        List list = RuleSheetParserUtil.getImportList( cellVal );
        assertNotNull( list );
        assertEquals( 0,
                      list.size( ) );

        assertEquals( 0,
                      RuleSheetParserUtil.getImportList( "" ).size( ) );

        cellVal = "com.something.Yeah, com.something.No,com.something.yeah.*";
        list = RuleSheetParserUtil.getImportList( cellVal );
        assertEquals( 3,
                      list.size( ) );
        assertEquals( "com.something.Yeah",
                      ((Import) list.get( 0 )).getClassName( ) );
        assertEquals( "com.something.No",
                      ((Import) list.get( 1 )).getClassName( ) );
        assertEquals( "com.something.yeah.*",
                      ((Import) list.get( 2 )).getClassName( ) );
    }
}
