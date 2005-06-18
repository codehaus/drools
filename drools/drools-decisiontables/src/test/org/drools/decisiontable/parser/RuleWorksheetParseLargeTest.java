/*
 * Created on 25/05/2005
 */
package org.drools.decisiontable.parser;

import java.io.InputStream;

import org.drools.decisiontable.model.Ruleset;
import org.drools.decisiontable.parser.RuleSheetListener;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale</a>
 * 
 * A special test for parsing a large workbook, to see how it scales.
 * 
 */
public class RuleWorksheetParseLargeTest extends TestCase
{

    private long startTimer;

    private long endTimer;

    /**
     * Tests parsing a large spreadsheet into an in memory ruleset. This doesn't
     * really do anything much at present. Takes a shed-load of memory to dump
     * out this much XML as a string, so really should think of using a stream
     * in some cases... (tried StringWriter, but is still in memory, so doesn't
     * help).
     * 
     * Stream to a temp file would work: return a stream from that file
     * (decorate FileInputStream such that when you close it, it deletes the
     * temp file).... must be other options.
     * 
     * @throws Exception
     */
    public void testLargeWorkSheetParseToRuleset() throws Exception
    {
        InputStream stream = RuleWorksheetParseLargeTest.class.getResourceAsStream( "/data/VeryLargeWorkbook.xls" );

        startTimer( );
        RuleSheetListener listener = RuleWorksheetParseTest.getRuleSheetListener( stream );
        stopTimer( );

        System.out.println( "Time to parse large table : " + getTime( ) );
        Ruleset ruleset = listener.getRuleSet( );
        assertNotNull( ruleset );
        /*
         * System.out.println("Time taken for 20K rows parsed: " + getTime());
         * 
         * startTimer(); String xml = listener.getRuleSet().toXML();
         * stopTimer(); System.out.println("Time taken for rendering to XML: " +
         * getTime());
         */
    }

    private void startTimer()
    {
        startTimer = System.currentTimeMillis( );
    }

    private void stopTimer()
    {
        endTimer = System.currentTimeMillis( );
    }

    private long getTime()
    {
        return endTimer - startTimer;
    }

}
