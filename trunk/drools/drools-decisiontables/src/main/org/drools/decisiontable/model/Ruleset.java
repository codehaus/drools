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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale </a>
 * 
 * This is the top of the parse tree. Represents a ruleset once it has been
 * parsed from excel. Also is the launching point for dumping out the DRL.
 */
public class Ruleset
    implements
    DRLJavaEmitter
{

    private String _name;

    private List   _imports;

    private List   _rules;

    public Ruleset(String name)
    {
        _name = name;
        _imports = new LinkedList( );
        _rules = new LinkedList( );
    }

    public void addImport(Import imp)
    {
        _imports.add( imp );
    }

    public void addRule(Rule rule)
    {
        _rules.add( rule );
    }

    /*
     * (non-Javadoc)
     * 
     * @see mdneale.drools.xls.model.DRLJavaEmitter#toXML()
     */
    public String toXML()
    {
        String xml = "<?xml version=\"1.0\"?> " + "<rule-set name=\"" + _name + "\" " + "xmlns=\"http://drools.org/rules\" " + "xmlns:java=\"http://drools.org/semantics/java\" " + "xmlns:xs=\"http://www.w3.org/2001/XMLSchema-instance\" "
                     + "xs:schemaLocation=\"http://drools.org/rules rules.xsd http://drools.org/semantics/java java.xsd\">";
        xml = xml + "\n\n\n";
        xml = xml + generateXml( _imports );
        xml = xml + generateXml( _rules );
        xml = xml + "\n\n\n</rule-set>";
        return xml.trim( );
    }

    /**
     * Uses the JavaEmitter interface to generate the xml.
     */
    private String generateXml(List list)
    {
        StringBuffer buf = new StringBuffer( );
        for ( Iterator it = list.iterator( ); it.hasNext( ); )
        {
            DRLJavaEmitter emitter = (DRLJavaEmitter) it.next( );
            buf.append( emitter.toXML( ) );
        }
        return buf.toString( );
    }

    public String getName()
    {
        return _name;
    }

    public List getImports()
    {
        return _imports;
    }

    public List getRules()
    {
        return _rules;
    }

}
