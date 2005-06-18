/*
 * Created on 11/06/2005
 */
package org.drools.decisiontable.parser;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.drools.decisiontable.parser.RuleSheetListener;
import org.drools.decisiontable.parser.xls.ExcelParser;

/**
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale</a>
 * 
 * TODO Document it !
 */
public class RulesheetUtil
{

    /**
     * Utility method showing how to get a rule sheet listener from a stream.
     */
    public static RuleSheetListener getRuleSheetListener(InputStream stream) throws IOException
    {
        Map listners = new HashMap( );
        RuleSheetListener listener = new RuleSheetListener( );
        listners.put( ExcelParser.DEFAULT_RULESHEET_NAME,
                      listener );
        ExcelParser parser = new ExcelParser( listners );
        parser.parseFile( stream );
        stream.close( );
        return listener;
    }
}
