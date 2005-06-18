/*
 * Created on 14/05/2005
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
package org.drools.decisiontable.model;

/**
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale </a>
 * 
 * 
 */
public class Consequence extends DRLElement
    implements
    DRLJavaEmitter
{

    private String _snippet;

    /**
     * @param _snippet
     *            The _snippet to set.
     */
    public void setSnippet(String snippet)
    {
        _snippet = snippet;
    }

    /*
     * (non-Javadoc)
     * 
     * @see mdneale.drools.xls.model.DRLJavaEmitter#toXML()
     */
    public String toXML()
    {
        String xml = "\t\t" + _snippet + "\n";
        return xml;
    }

    public String getSnippet()
    {
        return _snippet;
    }

}
