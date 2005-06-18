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

import java.io.UnsupportedEncodingException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale </a>
 * 
 * Represents a rule.
 */
public class Rule extends DRLElement
    implements
    DRLJavaEmitter
{

    private static final int MAX_ROWS = 65535;

    private Integer              _salience;

    private String           _name;

    private List             parameters;

    private List             conditions;

    private List             consequences;

    public Rule(String name,
                Integer salience)
    {
        _name = name;
        _salience = salience;
        parameters = new LinkedList( );
        conditions = new LinkedList( );
        consequences = new LinkedList( );
    }

    public void addParameter(Parameter param)
    {
        parameters.add( param );
    }

    public void setParameters(List parameterList)
    {
        parameters = parameterList;
    }

    public void addCondition(Condition con)
    {
        conditions.add( con );
    }

    public void addConsequence(Consequence con)
    {
        consequences.add( con );
    }

    /*
     * (non-Javadoc)
     * 
     * @see mdneale.drools.xls.model.DRLJavaEmitter#toXML()
     */
    public String toXML()
    {
        String xml = "<!--" + getComment( ) + "-->\n";
        xml = xml + "<rule name=\"" + _name + "\"";
        if (_salience != null) {
            xml = xml + " salience=\"" + _salience.toString() + "\"";
        }
        xml = xml + ">\n";
        xml = xml + generateXml( parameters );
        xml = xml + generateXml( conditions );
        xml = xml + "\t<java:consequence><![CDATA[ \n";
        xml = xml + generateXml( consequences );
        xml = xml + "\t ]]></java:consequence>";
        xml = xml + "\n" + "</rule>\n\n";
        return xml;
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

    public static int calcSalience(int rowNumber)
    {
        if ( rowNumber > MAX_ROWS )
        {
            throw new IllegalArgumentException( "That row number is above the max: " + MAX_ROWS );
        }
        return MAX_ROWS - rowNumber;
    }

    /**
     * @param col -
     *            the column number. Start with zero.
     * @return The spreadsheet name for this col number, such as "AA" or "AB" or
     *         "A" and such and such.
     */
    public static String convertColNumToColName(int i)
    {

        String result;
        int div = i / 26;
        int mod = i % 26;

        if ( div == 0 )
        {
            byte[] c = new byte[1];
            c[0] = (byte) (mod + 65);
            result = byteToString( c );
        }
        else
        {
            byte[] firstChar = new byte[1];
            firstChar[0] = (byte) ((div - 1) + 65);

            byte[] secondChar = new byte[1];
            secondChar[0] = (byte) (mod + 65);
            String first = byteToString( firstChar );
            String second = byteToString( secondChar );
            result = first + second;
        }
        return result;

    }

    private static String byteToString(byte[] secondChar)
    {
        try
        {
            return new String( secondChar,
                               "UTF-8" );
        }
        catch ( UnsupportedEncodingException e )
        {
            throw new RuntimeException( "Unable to convert char to string.",
                                        e );
        }
    }

    public List getConditions()
    {
        return conditions;
    }

    public List getConsequences()
    {
        return consequences;
    }

    public List getParameters()
    {
        return parameters;
    }

    public Integer getSalience()
    {
        return _salience;
    }

}
