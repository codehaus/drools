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

import java.text.StringCharacterIterator;

/**
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale </a>
 * 
 * The LayerSupertype for this model/parse tree.
 */
public abstract class DRLElement
{

    private String _comment;

    public void setComment(String comment)
    {
        _comment = comment;
    }

    String getComment()
    {
        return _comment;
    }

    /**
     * This escapes plain snippets into an xml/drl safe format.
     * 
     * @param snippet
     * @return An escaped DRL safe string.
     */
    static String escapeSnippet(String snippet)
    {
        if ( snippet != null )
        {
            final StringBuffer result = new StringBuffer( );

            final StringCharacterIterator iterator = new StringCharacterIterator( snippet );
            char character = iterator.current( );
            while ( character != StringCharacterIterator.DONE )
            {
                if ( character == '<' )
                {
                    result.append( "&lt;" );
                }
                else if ( character == '>' )
                {
                    result.append( "&gt;" );
                } // What else really needs to be escaped? SAX parsers are
                // inconsistent here...
                /*
                 * else if (character == '\"') { result.append("&quot;"); } else
                 * if (character == '\'') { result.append("&#039;"); } else if
                 * (character == '\\') { result.append("&#092;"); }
                 */
                else if ( character == '&' )
                {
                    result.append( "&amp;" );
                }
                else
                {
                    // the char is not a special one
                    // add it to the result as is
                    result.append( character );
                }
                character = iterator.next( );
            }
            return result.toString( );
        }
        return null;
    }

}
