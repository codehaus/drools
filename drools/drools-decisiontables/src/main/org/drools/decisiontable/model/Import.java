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
 * Represents an import (nominally at the rule-set level). The idea of this can
 * be extended to other ruleset level settings.
 */
public class Import extends DRLElement
    implements
    DRLJavaEmitter
{

    private String className;

    /**
     * @return Returns the className.
     */
    public String getClassName()
    {
        return className;
    }

    /**
     * @param className
     *            The className to set.
     */
    public void setClassName(String clazz)
    {
        className = clazz;
    }

    /*
     * (non-Javadoc)
     * 
     * @see mdneale.drools.xls.model.DRLJavaEmitter#toXML()
     */
    public String toXML()
    {
        String xml = "<!-- " + getComment( ) + "-->\n \t<import>" + className + "</import>\n";
        return xml;

    }
}
