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
 */
package org.drools.decisiontable.parser.xls;

import org.drools.decisiontable.parser.SheetListener;

/**
 * @author <a href="mailto:shaun.addison@gmail.com"> Shaun Addison </a>
 * 
 * Null listner.
 */
public class NullSheetListener
    implements
    SheetListener
{

    public void startSheet(String name)
    {
    }

    public void finishSheet()
    {
    }

    public void newRow(int rowNumber,
                       int columns)
    {
    }

    public void newCell(int row,
                        int column,
                        String value)
    {
    }

}
