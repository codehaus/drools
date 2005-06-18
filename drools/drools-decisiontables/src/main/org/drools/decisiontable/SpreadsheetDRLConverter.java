/*
 * Created on 5/05/2005
 * 	 Copyright 2005 (C) Michael D Neale. All Rights Reserved.
 *
 *	 Redistribution and use of this software and associated documentation
 *	 ("Software"), with or without modification, are permitted provided
 *	 that the following conditions are met:
 *
 *	 1. Redistributions of source code must retain copyright
 *	    statements and notices.  Redistributions must also contain a
 *	    copy of this document.
 *	 2. Due credit should be given to Michael D Neale.	     
 */
package org.drools.decisiontable;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.drools.decisiontable.model.Ruleset;
import org.drools.decisiontable.parser.RuleSheetListener;
import org.drools.decisiontable.parser.xls.ExcelParser;

/**
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale </a>
 * 
 * This class handles the input XLS and extracts the rule DRL, ready for pumping
 * into drools.
 */
public class SpreadsheetDRLConverter
{

    /**
     * Generates DRL from the input stream containing the spreadsheet.
     * 
     * @param xlsStream
     *            The stream to the spreadsheet. Uses the first worksheet found
     *            for the decision tables, ignores others.
     * @return DRL xml, ready for use in drools.
     */
    public String convertToDRL(InputStream xlsStream)
    {
        RuleSheetListener listener = getRuleSheetListener( xlsStream );
        Ruleset ruleset = listener.getRuleSet( );
        return ruleset.toXML( );
    }

    /**
     * Convenience implementation, taking rules from the classpath. It is
     * recommended to use the stream version, as you can then change rules
     * dynamically. (that is a lot of the benefit of rule engines !).
     * 
     * @param classPathResource
     *            full class path to the spreadsheet you wish to convert to DRL.
     *            Uses the first worksheet for the decision tables.
     * @return DRL.
     */
    public String convertToDRL(String classPathResource)
    {
        InputStream stream = this.getClass( ).getResourceAsStream( classPathResource );
        try
        {
            String drl = convertToDRL( stream );
            return drl;
        }
        finally
        {
            closeStream( stream );
        }
    }

    /**
     * Looks for a named worksheet to find the decision tables on.
     * 
     * @param stream
     *            The stream of the decision tables (spreadsheet).
     * @param worksheetName
     *            The name of the worksheet that the decision tables live on.
     * @return DRL, ready to go.
     */
    public String convertToDRL(InputStream stream,
                               String worksheetName)
    {
        RuleSheetListener listener = getRuleSheetListener( stream,
                                                           worksheetName );
        Ruleset ruleset = listener.getRuleSet( );
        return ruleset.toXML( );
    }

    private RuleSheetListener getRuleSheetListener(InputStream stream)
    {
        RuleSheetListener listener = new RuleSheetListener( );
        ExcelParser parser = new ExcelParser( listener );
        parser.parseFile( stream );
        return listener;
    }

    private RuleSheetListener getRuleSheetListener(InputStream stream,
                                                   String worksheetName)
    {
        RuleSheetListener listener = new RuleSheetListener( );
        Map listeners = new HashMap( );
        listeners.put( worksheetName,
                       listener );
        ExcelParser parser = new ExcelParser( listeners );
        parser.parseFile( stream );
        return listener;
    }

    private void closeStream(InputStream stream)
    {
        try
        {
            stream.close( );
        }
        catch ( Exception e )
        {
            // ignore
        }
    }

}
