/*
 * Created on 24/05/2005
 */
package org.drools.decisiontable.parser.xls;

import org.drools.decisiontable.parser.xls.ExcelParser;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale</a>
 * 
 * Some unit tests for the corners of ExcelParser that are not explicitly
 * covered by integration tests.
 */
public class ExcelParserTest extends TestCase
{

    public void testRemoveTrailingZero()
    {
        String test = "1.0";
        assertEquals( "1",
                      ExcelParser.removeTrailingZero( test ) );

        test = "42.0";
        assertEquals( "42",
                      ExcelParser.removeTrailingZero( test ) );

        test = "42";
        assertEquals( "42",
                      ExcelParser.removeTrailingZero( test ) );

    }

}
