/*
 * Created on 21/08/2005
 */
package org.drools.decisiontable.parser;

import java.util.Map;

import org.drools.decisiontable.model.SnippetBuilder;

/**
 * Simple holder class identifying a condition or action column.
 * 
 * There are five types of columns relevant to a rule table.
 * @author <a href="mailto:Michael.Neale@gmail.com"> Michael Neale</a>
 */
public class ActionType
{

    public static final int CONDITION = 0;

    public static final int ACTION    = 1;

    // 08 - 16 - 2005 RIK: Define 3 new ActionType types
    // PRIORITY is used to set the salience parameter of a rule tag
    public static final int PRIORITY  = 2;

    // DURATION is used to set a duration tag inside a rule tag
    public static final int DURATION  = 3;

    // NAME is used to set the name parameter of a rule tag
    public static final int NAME      = 4;

    int                     type;

    String                  value;

    ActionType(int actionType,
               String cellValue)
    {
        type = actionType;
        value = cellValue;
    }

    String getSnippet(String cellValue)
    {
        SnippetBuilder builder = new SnippetBuilder( value );
        return builder.build( cellValue );
    }

    /**
     * Create a new action type that matches this cell, and add it to the map,
     * keyed on that column.
     */
    public static void addNewActionType(Map actionTypeMap,
                                 String value,
                                 int column,
                                 int row)
    {
        if ( value.toUpperCase( ).startsWith( "C" ) )
        {
            actionTypeMap.put( new Integer( column ),
                               new ActionType( ActionType.CONDITION,
                                               null ) );
        }
        else if ( value.toUpperCase( ).startsWith( "A" ) )
        {
            actionTypeMap.put( new Integer( column ),
                               new ActionType( ActionType.ACTION,
                                               null ) );
        }
        else if ( value.toUpperCase( ).startsWith( "P" ) ) // if the title cell
                                                            // value starts with
                                                            // "P" then put a
                                                            // ActionType.PRIORITY
                                                            // to the _actions
                                                            // list
        {
            actionTypeMap.put( new Integer( column ),
                               new ActionType( ActionType.PRIORITY,
                                               null ) );
        }
        else if ( value.toUpperCase( ).startsWith( "D" ) ) // if the title cell
                                                            // value starts with
                                                            // "D" then put a
                                                            // ActionType.DURATION
                                                            // to the _actions
                                                            // list
        {
            actionTypeMap.put( new Integer( column ),
                               new ActionType( ActionType.DURATION,
                                               null ) );
        }
        else if ( value.toUpperCase( ).startsWith( "N" ) ) // if the title cell
                                                            // value starts with
                                                            // "N" then put a
                                                            // ActionType.NAME
                                                            // to the _actions
                                                            // list
        {
            actionTypeMap.put( new Integer( column ),
                               new ActionType( ActionType.NAME,
                                               null ) );
        }
        else
        {
            throw new DecisionTableParseException( "Should be CONDITION or ACTION row:" + row + " column:" + column + " - does not contain a leading C or A identifer." );
        }
    }

}
