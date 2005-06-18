package org.drools.decisiontable;

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import org.drools.IntegrationException;
import org.drools.RuleBase;
import org.drools.io.RuleBaseLoader;
import org.xml.sax.SAXException;

/**
 * 
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale</a>
 * 
 * This class is a utility class for loading decision tables from a stream. Can
 * be used as an alternative to RuleBaseLoader, for decision tables.
 * 
 * Example usage: <code>
 * 	RuleBase rb = DecisionTableLoader.loadFromInputStream(streamToSpreadsheet);
 *  //typically you will want to cache the built RuleBase, for performance
 *  
 *  //to use the rulebase:
 * 	WorkingMemory engine = rb.newWorkingMemory();
 *  engine.assertObject(yourObject);
 *  engine.fireAllRules();
 *  
 *  //its that simple !
 * </code>
 */
public final class DecisionTableLoader
{

    /**
     * Make sure it is strictly a utility.
     */
    private DecisionTableLoader()
    {
        throw new UnsupportedOperationException( );
    }

    /**
     * Load a rulebase from a decision table spreadsheet. This will use the
     * first worksheet in the spreadsheet file as the source of all decision
     * tables.
     * 
     * @param streamToSpreadsheet
     *            Stream to a excel 97 compatable workbook (from Excel, or Open
     *            Office).
     * @return A compiled RuleBase, ready to
     * @throws IOException
     * @throws SAXException
     * @throws IntegrationException
     *             go.
     */
    public static RuleBase loadFromInputStream(InputStream streamToSpreadsheet) throws IntegrationException,
                                                                               SAXException,
                                                                               IOException
    {
        StringReader reader = getReader( streamToSpreadsheet );
        return RuleBaseLoader.loadFromReader( reader );
    }

    private static StringReader getReader(InputStream streamToSpreadsheet)
    {
        String generatedDrl = loadDRLFromStream( streamToSpreadsheet );
        StringReader reader = new StringReader( generatedDrl );
        return reader;
    }

    private static String loadDRLFromStream(InputStream streamToSpreadsheet)
    {
        SpreadsheetDRLConverter converter = new SpreadsheetDRLConverter( );
        String generatedDrl = converter.convertToDRL( streamToSpreadsheet );
        return generatedDrl;
    }

}
