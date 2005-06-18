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
package org.drools.decisiontable.model;

/**
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale </a>
 * 
 * This utility class exists to convert rule script snippets to actual code. The
 * snippets contain place holders for values to be substituted into. See the
 * test case for how it really works !
 * 
 * Snippet template example: "something.getBlah($param)" $param is the "place
 * holder". This will get replaced with the "cellValue" that is passed in.
 */
public class SnippetBuilder
{

    private static final String PARAM_PREFIX       = "$";

    private static final String PARAM              = "\\" + PARAM_PREFIX + "param";

    private static final String PARAM_PREFIX_REGEX = "\\" + PARAM_PREFIX;

    private String              _template;

    /**
     * @param snippetTemplate
     *            The snippet including the "place holder" for a parameter. If
     *            no "place holder" is present,
     */
    public SnippetBuilder(String snippetTemplate)
    {
        _template = snippetTemplate;
    }

    /**
     * @param cellValue
     *            The value from the cell to populate the snippet with. If no
     *            place holder exists, will just return the snippet.
     * @return The final snippet.
     */
    public String build(String cellValue)
    {
        if ( _template.indexOf( PARAM_PREFIX + "1" ) > 0 )
        {
            return buildMulti( cellValue );
        }
        else
        {
            return buildSingle( cellValue );
        }
    }

    private String buildMulti(String cellValue)
    {
        String[] cellVals = cellValue.split( "," );
        String result = _template;

        for ( int paramNumber = 0; paramNumber < cellVals.length; paramNumber++ )
        {
            String regex = PARAM_PREFIX_REGEX + (paramNumber + 1);
            result = result.replaceAll( regex,
                                        cellVals[paramNumber].trim( ) );

        }
        return result;
    }

    /**
     * @param cellValue
     * @return
     */
    private String buildSingle(String cellValue)
    {
        return _template.replaceAll( PARAM,
                                     cellValue );
    }

}
