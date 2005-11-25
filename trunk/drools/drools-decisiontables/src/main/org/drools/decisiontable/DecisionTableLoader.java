package org.drools.decisiontable;

/*
 * Copyright 2005 (C) The Werken Company. All Rights Reserved.
 *
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. The name "drools" must not be used to endorse or promote products derived
 * from this Software without prior written permission of The Werken Company.
 * For written permission, please contact bob@werken.com.
 *
 * 4. Products derived from this Software may not be called "drools" nor may
 * "drools" appear in their names without prior written permission of The Werken
 * Company. "drools" is a registered trademark of The Werken Company.
 *
 * 5. Due credit should be given to The Werken Company.
 * (http://drools.werken.com/).
 *
 * THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE WERKEN COMPANY OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */

import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import org.drools.IntegrationException;
import org.drools.RuleBase;
import org.drools.decisiontable.parser.DecisionTableParseException;
import org.drools.io.RuleBaseLoader;
import org.drools.io.RuleSetLoader;
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
     * @return A drools RuleBase, ready to go.
     * @throws IOException
     * @throws SAXException
     * @throws IntegrationException
     * 
     * This may also throw a DecisionTableParseException if there is some
     * problem parsing the decision table (before building the rulebase).
     */
    public static RuleBase loadFromInputStream(InputStream streamToSpreadsheet) throws IntegrationException,
                                                                               SAXException,
                                                                               IOException
    {
        return loadFromInputStream( streamToSpreadsheet,
                                    InputType.XLS );
    }

    /**
     * Load a rulebase from a decision table spreadsheet in CSV format.
     * 
     * @param streamToCSV
     *          Stream to CSV input.
     * @return A drools RuleBase, ready to go.
     * @throws IOException
     * @throws SAXException
     * @throws IntegrationException
     * 
     * This may also throw a DecisionTableParseException if there is some
     * problem parsing the decision table (before building the rulebase).
     */
    public static RuleBase loadFromCSVInputStream(InputStream streamToCSV) throws IntegrationException,
                                                                               SAXException,
                                                                               IOException
    {
        return loadFromInputStream( streamToCSV,
                                    InputType.CSV );
    }

    /**
     * Load a rulebase from a decision table spreadsheet.
     * 
     * @param streamToSpreadsheet
     *            Format is as indicated (CSV or XLS 97 format).
     * @return A drools RuleBase, ready to go.
     * @throws IOException
     * @throws SAXException
     * @throws IntegrationException
     * 
     * This may also throw a DecisionTableParseException if there is some
     * problem parsing the decision table (before building the rulebase).
     */
    public static RuleBase loadFromInputStream(InputStream streamToSpreadsheet,
                                               InputType inputType) throws IntegrationException,
                                                                   SAXException,
                                                                   IOException
    {
        StringReader reader = getReader( streamToSpreadsheet,
                                         inputType );
        
        
        RuleSetLoader ruleSetLoader = new RuleSetLoader();           
        ruleSetLoader.addFromReader( reader);            
        
        RuleBaseLoader ruleBaseLoader = new RuleBaseLoader();
        ruleBaseLoader.addFromRuleSetLoader(ruleSetLoader);
        return ruleBaseLoader.buildRuleBase();                           
    }

    private static StringReader getReader(InputStream streamToSpreadsheet,
                                          InputType inputType)
    {
        String generatedDrl = loadDRLFromStream( streamToSpreadsheet,
                                                 inputType );
        StringReader reader = new StringReader( generatedDrl );
        return reader;
    }

    private static String loadDRLFromStream(InputStream streamToSpreadsheet,
                                            InputType inputType)
    {
        SpreadsheetDRLConverter converter = new SpreadsheetDRLConverter( );
        try
        {
            String generatedDrl = converter.convertToDRL( streamToSpreadsheet,
                                                          inputType );
            return generatedDrl;

        }
        catch ( RuntimeException e )
        {
            throw new DecisionTableParseException( "An error occurred processing the decision table.",
                                                   e );
        }
    }

}
