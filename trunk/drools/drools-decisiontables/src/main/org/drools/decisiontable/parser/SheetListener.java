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
package org.drools.decisiontable.parser;

/**
 * @author <a href="mailto:shaun.addison@gmail.com"> Shaun Addison </a>
 * 
 * Callback interface for scanning an spreadsheet.
 */
public interface SheetListener
{

    /**
     * Start a new sheet
     * 
     * @param name
     *            the sheet name
     */
    public void startSheet(String name);

    /**
     * Come to the end of the sheet.
     */
    public void finishSheet();

    /**
     * Enter a new row
     * 
     * @param rowNumber
     *            TODO
     * @param columns
     *            TODO
     */
    public void newRow(int rowNumber,
                       int columns);

    /**
     * Enter a new cell
     * 
     * @param row
     *            the row number
     * @param column
     *            the column alpha character label
     * @param value
     *            the string value of the cell
     */
    public void newCell(int row,
                        int column,
                        String value);

}
