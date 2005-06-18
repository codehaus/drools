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
 * Represents a rule level parameter. This idea can be extended to other rule
 * level elements (such as duration).
 * 
 */
public class Parameter extends DRLElement
    implements
    DRLJavaEmitter
{

    private String _identifier;

    private String _className;

    /**
     * @return Returns the className.
     */
    public String getClassName()
    {
        return _className;
    }

    /**
     * @param name
     *            The className to set.
     */
    public void setClassName(String name)
    {
        _className = name;
    }

    /**
     * @return Returns the identifier.
     */
    public String getIdentifier()
    {
        return _identifier;
    }

    /**
     * @param identifier
     *            The identifier to set.
     */
    public void setIdentifier(String identifier)
    {
        this._identifier = identifier;
    }

    /*
     * (non-Javadoc)
     * 
     * @see mdneale.drools.xls.model.DRLJavaEmitter#toXML()
     */
    public String toXML()
    {
        String xml = "\t<!-- " + getComment( ) + "-->\n" + "\t<parameter identifier=\"" + _identifier + "\">\n" + "\t\t<class>" + _className + "</class>\n" + "\t</parameter>\n\n";
        return xml;
    }
}
