/*
 * Created on 12/05/2005
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
 * 
 */
package org.drools.decisiontable.parser.xls;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.eventusermodel.HSSFEventFactory;
import org.apache.poi.hssf.eventusermodel.HSSFListener;
import org.apache.poi.hssf.eventusermodel.HSSFRequest;
import org.apache.poi.hssf.record.BOFRecord;
import org.apache.poi.hssf.record.BoundSheetRecord;
import org.apache.poi.hssf.record.LabelSSTRecord;
import org.apache.poi.hssf.record.NumberRecord;
import org.apache.poi.hssf.record.Record;
import org.apache.poi.hssf.record.RowRecord;
import org.apache.poi.hssf.record.SSTRecord;
import org.apache.poi.hssf.record.BoolErrRecord;

import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.drools.decisiontable.parser.SheetListener;

/**
 * @author <a href="mailto:shaun.addison@gmail.com"> Shaun Addison </a><a
 *         href="mailto:michael.neale@gmail.com"> Michael Neale </a>
 * 
 * Parse an excel spreadsheet using the event model of POI. TODO: can not
 * resolve date format issues as yet.
 * 
 */
public class ExcelParser
    implements
    HSSFListener
{

    public static final String  DEFAULT_RULESHEET_NAME = "Decision Tables";

    private static final String WORKBOOK               = "Workbook";

    public static final String  DATE_TARGET_FORMAT     = "dd-MMM-yyyy";

    private Map                 _listners              = new HashMap( );

    private SheetListener       _currentSheetListener  = new NullSheetListener( );

    private List                _sheetNames            = new ArrayList( );

    private int                 _currentSheetNumber    = 0;

    private SSTRecord           _sstRec                = null;

    private boolean             _useFirstSheet         = false;

    /**
     * Define a map of sheet name to listner handlers.
     * 
     * @param sheetListners
     *            map of String to SheetListener
     */
    public ExcelParser(Map sheetListners)
    {
        _listners = sheetListners;
    }

    public ExcelParser(SheetListener listener)
    {
        _listners.put( DEFAULT_RULESHEET_NAME,
                       listener );
        _useFirstSheet = true;
    }

    /**
     * Initialise a new sheet and complete the previous.
     * 
     * @param record
     *            the new record
     */
    private void newSheet(String sheetName)
    {
        _currentSheetListener.finishSheet( );
        _currentSheetListener = new NullSheetListener( );

        if ( _useFirstSheet && _currentSheetNumber == 1 )
        {
            _currentSheetListener = getFirstSheetListener( );
        }
        else
        {
            if ( _listners.containsKey( sheetName ) )
            {
                _currentSheetListener = (SheetListener) _listners.get( sheetName );
            }
        }

        _currentSheetListener.startSheet( sheetName );
    }

    private SheetListener getFirstSheetListener()
    {
        SheetListener listener = (SheetListener) _listners.values( ).iterator( ).next( );
        if ( listener == null )
        {
            throw new IllegalStateException( "No default first sheet listener found !" );
        }
        return listener;
    }

    /**
     * Implement the HSSFListener method
     * 
     * This receives the events for the Excel file format scan. A bunch of
     * document data OLE style and the sheet content.
     */
    public void processRecord(Record record)
    {

        switch ( record.getSid( ) )
        {
        case BoundSheetRecord.sid :
            BoundSheetRecord bsr = (BoundSheetRecord) record;
            _sheetNames.add( bsr.getSheetname( ) );
            break;
        case BOFRecord.sid :
            BOFRecord bof = (BOFRecord) record;
            if ( bof.getType( ) == BOFRecord.TYPE_WORKSHEET )
            {
                newSheet( (String) _sheetNames.get( _currentSheetNumber++ ) );
            }
            break;
        case RowRecord.sid :
            RowRecord rowRec = (RowRecord) record;
            _currentSheetListener.newRow( rowRec.getRowNumber( ),
                                          rowRec.getLastCol( ) - rowRec.getFirstCol( ) + 1 );
            break;
        case SSTRecord.sid :
            _sstRec = (SSTRecord) record;
            break;
        case LabelSSTRecord.sid :
            handleText( record );
            break;

        case NumberRecord.sid :
            handleNumber( record );
            break;
        case BoolErrRecord.sid :
            handleBool( record );
            break;
        }
    }

    private void handleBool(Record record)
    {
        BoolErrRecord boolRec = (BoolErrRecord) record;
        _currentSheetListener.newCell( boolRec.getRow( ),
                                       boolRec.getColumn( ),
                                       "" + boolRec.getBooleanValue( ) );
    }

    private void handleText(Record record)
    {
        LabelSSTRecord labelRec = (LabelSSTRecord) record;
        String value = _sstRec.getString( labelRec.getSSTIndex( ) );
        _currentSheetListener.newCell( labelRec.getRow( ),
                                       labelRec.getColumn( ),
                                       value );
    }

    private void handleNumber(Record record)
    {
        // MN have to do with stupid numbers with ".0" as well as
        // freaking excel freaking dates freakin.
        NumberRecord numRec = (NumberRecord) record;

        String stringVal = null;

        /*
         * if (numRec.getXFIndex() == DATE_FORMATTED_CELL) { Date date =
         * HSSFDateUtil.getJavaDate(val); SimpleDateFormat format = new
         * SimpleDateFormat(DATE_TARGET_FORMAT); stringVal =
         * format.format(date); } else {
         */
        stringVal = "" + numRec.getValue( );
        stringVal = removeTrailingZero( stringVal );
        /* } */
        _currentSheetListener.newCell( numRec.getRow( ),
                                       numRec.getColumn( ),
                                       "" + stringVal );
    }

    static String removeTrailingZero(String stringVal)
    {
        if ( stringVal.endsWith( ".0" ) )
        {
            stringVal = stringVal.substring( 0,
                                             stringVal.length( ) - 2 );
        }
        return stringVal;
    }

    /**
     * Scan an Excel file stream.
     * 
     * @param inStream
     *            the ms-excel data stream
     */
    public void parseFile(InputStream inStream)
    {
        InputStream din = null;

        try
        {
            POIFSFileSystem poifs = new POIFSFileSystem( inStream );
            // get the Workbook (excel part) stream in a InputStream
            din = poifs.createDocumentInputStream( WORKBOOK );
            HSSFRequest req = new HSSFRequest( );
            // lazy listen for ALL records with the listener shown above
            req.addListenerForAllRecords( this );
            HSSFEventFactory factory = new HSSFEventFactory( );
            factory.processEvents( req,
                                   din );
        }
        catch ( IOException ex )
        {
            throw new RuntimeException( "Failed to open Excel stream",
                                        ex );
        }
    }

}
