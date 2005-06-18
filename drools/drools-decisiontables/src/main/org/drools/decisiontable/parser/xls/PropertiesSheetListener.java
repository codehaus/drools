/*
 * Created on 22/05/2005
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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;

import org.drools.decisiontable.parser.SheetListener;

/**
 * Reads an Excel sheet as key-value properties.
 * 
 * Treats the first non-empty cell on a row as a key and any subsequent
 * non-empty cell as a value. Any cells defined after the second cell are
 * ignored as comments.
 * 
 * Could be easily adapted to accept multiple values per key but the semantics
 * were kept in line with Properties.
 * 
 * @author <a href="mailto:shaun.addison@gmail.com"> Shaun Addison </a>
 * 
 */
public class PropertiesSheetListener
    implements
    SheetListener
{

    private static final String EMPTY_STRING   = "";

    private Map                 _rowProperties = new HashMap( );

    private Properties          _properties    = new Properties( );

    /**
     * Return the key value pairs. If this is called before the sheet is
     * finished, then it will build the properties map with what is known.
     * Subsequent calls will update the properties map.
     * 
     * @return properties
     */
    public Properties getProperties()
    {
        finishSheet( ); // MN allows this to be called before the sheet is
        // finished, as
        // some properties are used whilst the sheet is being parsed.
        return _properties;
    }

    /*
     * (non-Javadoc)
     * 
     * @see my.hssf.util.SheetListener#startSheet(java.lang.String)
     */
    public void startSheet(String name)
    {
    }

    /*
     * (non-Javadoc)
     * 
     * @see my.hssf.util.SheetListener#finishSheet()
     */
    public void finishSheet()
    {
        for ( Iterator iter = _rowProperties.keySet( ).iterator( ); iter.hasNext( ); )
        {
            String[] keyValue = (String[]) _rowProperties.get( iter.next( ) );
            _properties.setProperty( keyValue[0],
                                     keyValue[1] );
        }
    }

    /**
     * Enter a new row. This is ignored.
     * 
     * @param rowNumber
     *            The row number.
     * @param columns
     *            The Colum number.
     */
    public void newRow(int rowNumber,
                       int columns)
    {
        // nothing to do.
    }

    /*
     * (non-Javadoc)
     * 
     * @see my.hssf.util.SheetListener#newCell(int, int, java.lang.String)
     */
    public void newCell(int row,
                        int column,
                        String value)
    {
        Integer rowInt = new Integer( row );
        if ( _rowProperties.containsKey( rowInt ) )
        {
            String[] keyValue = (String[]) _rowProperties.get( rowInt );

            if ( keyValue[1] == EMPTY_STRING )
            {
                keyValue[1] = value;
            }
        }
        else
        {
            String[] keyValue = {value, EMPTY_STRING};
            _rowProperties.put( rowInt,
                                keyValue );
        }
    }

}
