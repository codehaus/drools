/*
 * Created on 2/06/2005
 */
package org.drools.decisiontable;

import java.io.InputStream;

import junit.framework.TestCase;

/**
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale</a>
 * 
 * Some basic unit tests for converter utility.
 */
public class SpreadsheetDRLConverterUnitTest extends TestCase
{

    public void testLoadFromClassPath()
    {
        SpreadsheetDRLConverter converter = new SpreadsheetDRLConverter( );
        String drl = converter.convertToDRL( "/data/MultiSheetDST.xls" );
        assertNotNull( drl );
    }

    public void testLoadSpecificWorksheet()
    {
        SpreadsheetDRLConverter converter = new SpreadsheetDRLConverter( );
        InputStream stream = this.getClass( ).getResourceAsStream( "/data/MultiSheetDST.xls" );
        String drl = converter.convertToDRL( stream,
                                             "Another Sheet" );
        assertNotNull( drl );
    }

}
