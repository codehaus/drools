/*
 * Created on 23/05/2005
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

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.StringTokenizer;

import org.drools.decisiontable.model.Import;
import org.drools.decisiontable.model.Parameter;

/**
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale </a>
 * 
 * Parking lot for utility methods that don't belong anywhere else.
 */
public class RuleSheetParserUtil
{

    private RuleSheetParserUtil()
    {
        // strictly util
    }

    public static String getRuleName(String ruleRow)
    {
        int left = ruleRow.indexOf( RuleSheetListener.RULE_TABLE_TAG );
        int right = ruleRow.indexOf( '(' );
        if ( left == -1 || right == -1 )
        {
            invalidRuleTableDef( ruleRow );
        }
        return ruleRow.substring( left + RuleSheetListener.RULE_TABLE_TAG.length( ),
                                  right ).trim( );
    }

    private static void invalidRuleTableDef(String ruleRow)
    {
        throw new IllegalArgumentException( "Invalid rule table header cell, must have some parameters. " + "Should be in the format of 'RuleTable some name (Type paramName, Type, paramName2)'. " + "It was: \n [" + ruleRow + "] \n" );
    }

    /**
     * 
     * @param ruleRow
     * @return Return a list of Parameters from the rule row string.
     */
    public static List getParameterList(String ruleRow)
    {
        List paramList = new ArrayList( );
        int left = ruleRow.indexOf( '(' );
        int right = ruleRow.indexOf( ')' );
        if ( left == -1 || right == -1 )
        {
            invalidRuleTableDef( ruleRow );
        }
        String params = ruleRow.substring( left + 1,
                                           right );
        StringTokenizer tokens = new StringTokenizer( params,
                                                      "," );
        while ( tokens.hasMoreTokens( ) )
        {
            String token = tokens.nextToken( );
            Parameter param = new Parameter( );
            StringTokenizer paramTokens = new StringTokenizer( token,
                                                               " " );
            param.setClassName( paramTokens.nextToken( ) );
            param.setIdentifier( paramTokens.nextToken( ) );
            paramList.add( param );
        }
        if ( paramList.size( ) == 0 ) throw new IllegalArgumentException( "No parameters found in RuleTable definition cell." );
        return paramList;

    }

    /**
     * 
     * @param importCell
     *            The cell text for all the classes to import.
     * @return A list of Import classes, which can be added to the ruleset.
     */
    public static List getImportList(String importCell)
    {
        List importList = new LinkedList( );
        if ( importCell == null )
        {
            return importList;
        }
        StringTokenizer tokens = new StringTokenizer( importCell,
                                                      "," );
        while ( tokens.hasMoreTokens( ) )
        {
            Import imp = new Import( );
            imp.setClassName( tokens.nextToken( ).trim( ) );
            importList.add( imp );
        }
        return importList;
    }

    /**
     * @return true is the String could possibly mean true. False otherwise !
     */
    public static boolean isStringMeaningTrue(String property)
    {
        if ( property == null )
        {
            return false;
        }
        else
        {
            property = property.trim( );
            if ( property.equalsIgnoreCase( "true" ) )
            {
                return true;
            }
            else if ( property.startsWith( "Y" ) )
            {
                return true;
            }
            else if ( property.startsWith( "y" ) )
            {
                return true;
            }
            else if ( property.equalsIgnoreCase( "on" ) )
            {
                return true;
            }
            else
            {
                return false;
            }
        }
    }

}
