package org.drools.decisiontable.parser;

/*
 * Copyright 2005 (C) The Werken Company. All Rights Reserved.
 *
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. The name "drools" must not be used to endorse or promote products derived
 * from this Software without prior written permission of The Werken Company.
 * For written permission, please contact bob@werken.com.
 *
 * 4. Products derived from this Software may not be called "drools" nor may
 * "drools" appear in their names without prior written permission of The Werken
 * Company. "drools" is a registered trademark of The Werken Company.
 *
 * 5. Due credit should be given to The Werken Company.
 * (http://drools.werken.com/).
 *
 * THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE WERKEN COMPANY OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.drools.decisiontable.model.Condition;
import org.drools.decisiontable.model.Consequence;
import org.drools.decisiontable.model.Duration;
import org.drools.decisiontable.model.Import;
import org.drools.decisiontable.model.Rule;
import org.drools.decisiontable.model.Ruleset;
import org.drools.decisiontable.model.Variable;
import org.drools.decisiontable.parser.xls.PropertiesSheetListener;

/**
 * @author <a href="mailto:shaun.addison@gmail.com"> Shaun Addison </a><a
 *         href="mailto:michael.neale@gmail.com"> Michael Neale </a>
 * 
 * Define a ruleset spreadsheet which contains one or more decision tables.
 * 
 * Stay calm, deep breaths... this is a little bit scary, its where it all happens.
 * 
 * A table is identifed by a cell beginning with the text "RuleTable". The first
 * row after the table identifier defines the column type: either a condition
 * ("C") or consequence ("A" for action), and so on.
 * 
 * The second row identifies the java code block associated with the condition
 * or consequence. This code block will include a parameter marker for the
 * attribute defined by that column.
 * 
 * The third row is a label for the attribute associated with that column.
 * 
 * All subsequent rows identify rules with the set.
 */
public class RuleSheetListener
    implements
    SheetListener
{

    public static final String      FUNCTIONS_TAG          = "Functions";

    public static final String      IMPORT_TAG             = "Import";

    public static final String      SEQUENTIAL_FLAG        = "Sequential";

    public static final String      VARIABLES_TAG         = "Variables";

    public static final String      RULE_TABLE_TAG         = "RuleTable";

    public static final String      RULESET_TAG            = "RuleSet";

    private static final int        ACTION_ROW             = 1;

    private static final int        CODE_ROW               = 2;

    private static final int        LABEL_ROW              = 3;

    private boolean                 _isInRuleTable         = false;

    private int                     _ruleRow;

    private int                     _ruleStartColumn;

    private int                     _ruleStartRow;

    private Map                     _actions;

    private List                    _ruleList              = new LinkedList( ); 
    
    private Rule                    _currentRule;

    private List                    _currentParameters;

    private String                  _currentRulePrefix;

    private boolean                 _currentSequentialFlag = false;

    private PropertiesSheetListener _propertiesListner     = new PropertiesSheetListener( );

    /**
     * Return the rule sheet properties
     */
    public Properties getProperties()
    {
        return _propertiesListner.getProperties( );
    }

    /**
     * Build the final ruleset as parsed.
     */
    public Ruleset getRuleSet()
    {
        if ( _ruleList.isEmpty( ) )
        {
            throw new DecisionTableParseException( "No RuleTable's were found in spreadsheet." );
        }
        Ruleset ruleset = buildRuleSet( );
        return ruleset;
    }

    private Ruleset buildRuleSet()
    {
        String rulesetName = getProperties( ).getProperty( RULESET_TAG );
        Ruleset ruleset = new Ruleset( rulesetName );
        for ( Iterator it = _ruleList.iterator( ); it.hasNext( ); )
        {
            ruleset.addRule( (Rule) it.next( ) );
        }
        List importList = RuleSheetParserUtil.getImportList( getProperties( ).getProperty( IMPORT_TAG ) );
        for ( Iterator it = importList.iterator( ); it.hasNext( ); )
        {
            ruleset.addImport( (Import) it.next( ) );
        }
        List variableList = RuleSheetParserUtil.getVariableList( getProperties( ).getProperty( VARIABLES_TAG ) ); // Set the list of variables to be added to the application-data tags
        for ( Iterator it = variableList.iterator( ); it.hasNext( ); )
        {
            ruleset.addVariable( (Variable) it.next( ) );
        }

        String functions = getProperties( ).getProperty( FUNCTIONS_TAG );
        ruleset.addFunctions( functions );
        return ruleset;
    }

    /*
     * (non-Javadoc)
     * 
     * @see my.hssf.util.SheetListener#startSheet(java.lang.String)
     */
    public void startSheet(String name)
    {
        // nothing to see here... move along..
    }

    /*
     * (non-Javadoc)
     * 
     * @see my.hssf.util.SheetListener#finishSheet()
     */
    public void finishSheet()
    {
        _propertiesListner.finishSheet( );
        finishRuleTable( );
    }

    /*
     * (non-Javadoc)
     * 
     * @see my.hssf.util.SheetListener#newRow()
     */
    public void newRow(int rowNumber,
                       int columns)
    {
        // nothing to see here... these aren't the droids your looking for..
        // move along...
    }

    /*
     * (non-Javadoc)
     * 
     * @see my.hssf.util.SheetListener#newCell(int, int, java.lang.String)
     */
    public void newCell(int row,
                        int column,
                        String value)
    {
        if (isCellValueEmpty(value)) {
            return;
        }
        if ( _isInRuleTable )
        {
            processRuleCell( row,
                             column,
                             value );
        }
        else
        {
            processNonRuleCell( row,
                                column,
                                value );
        }
    }

    /**
     * This gets called each time a "new" rule table is found.
     */
    private void initRuleTable(int row,
                               int column,
                               String value)
    {
        
        _isInRuleTable = true;
        _actions = new HashMap( );
        _ruleStartColumn = column;
        _ruleStartRow = row;
        _ruleRow = row + LABEL_ROW + 1;

        // setup stuff for the rules to come.. (the order of these steps are
        // important !)
        _currentRulePrefix = RuleSheetParserUtil.getRuleName( value );
        _currentParameters = RuleSheetParserUtil.getParameterList( value );
        _currentSequentialFlag = getSequentialFlag( );

        _currentRule = createNewRuleForRow( _ruleRow,
                                            _currentParameters );

        _ruleList.add( _currentRule );

    }

    private boolean getSequentialFlag()
    {
        String seqFlag = getProperties( ).getProperty( SEQUENTIAL_FLAG );
        return RuleSheetParserUtil.isStringMeaningTrue( seqFlag );
    }

    private void finishRuleTable()
    {
        if ( _isInRuleTable )
        {
            _currentSequentialFlag = false;
            _currentParameters = new LinkedList( );
            _isInRuleTable = false;
        }
    }

    private void processNonRuleCell(int row,
                                    int column,
                                    String value)
    {
        if ( value.startsWith( RULE_TABLE_TAG ) )
        {
            initRuleTable( row,
                           column,
                           value );
        }
        else
        {
            _propertiesListner.newCell( row,
                                        column,
                                        value );
        }
    }

    private void processRuleCell(int row,
                                 int column,
                                 String value)
    {
        if ( value.startsWith( RULE_TABLE_TAG ) )
        {
            finishRuleTable( );
            initRuleTable( row,
                           column,
                           value );
            return;
        }

        // Ignore any comments cells preceeding the first rule table column
        if ( column < _ruleStartColumn )
        {
            return;
        }
        
        // Ignore any further cells from the rule def row
        if ( row == _ruleStartRow) 
        {
            return;
        }
        
        switch ( row - _ruleStartRow )
        {
        case ACTION_ROW :
            ActionType.addNewActionType(_actions, value, column, row);            
            break;
        case CODE_ROW :
            codeRow( row,
                     column,
                     value );
            break;
        case LABEL_ROW :
            break;
        default :
            nextRule( row,
                      column,
                      value );
            break;
        }
    }

    private void codeRow(int row,
                         int column,
                         String value)
    {
        ActionType actionType = getActionForColumn( row,
                column );

        if ( value.trim( ).equals( "" ) && (actionType.type == ActionType.ACTION || actionType.type == ActionType.CONDITION))
        {
            throw new DecisionTableParseException( "Code description - row:" + (row + 1) + 
                                                   " cell number:" + (column + 1) + " - does not contain any code specification. It should !" );
        }

        actionType.value = value;
    }

    private ActionType getActionForColumn(int row,
                                          int column)
    {
        ActionType actionType = (ActionType) _actions.get( new Integer( column ) );

        if ( actionType == null )
        {
            throw new DecisionTableParseException( "Code description - row number:" + (row + 1) 
                                                   + " cell number:" + (column + 1) + 
                                                   " - does not have an 'ACTION' or 'CONDITION' column header." );
        }

        return actionType;
    }

    private void nextRule(int row,
                          int column,
                          String value)
    {     
        ActionType actionType = getActionForColumn( row,
                                                    column );

        if ( row - _ruleRow > 1 )
        {
            // Encountered a row gap from the last rule.
            // This is not part of the ruleset.
            finishRuleTable( );
            processNonRuleCell( row,
                                column,
                                value );
            return;
        }

        if ( row > _ruleRow )
        {
            // In a new row/rule
            _currentRule = createNewRuleForRow( row,
                                                _currentParameters );

            _ruleList.add( _currentRule );
            _ruleRow++;
        }
        

        
        if (actionType.type == ActionType.PRIORITY && !_currentSequentialFlag) // if the rule set is not sequential and the actionType type is PRIORITY then set the current Rule's salience paramenter with the value got from the cell
        {
        	_currentRule.setSalience( new Integer(value) );
        }
        else if (actionType.type == ActionType.NAME) // if the actionType type is PRIORITY then set the current Rule's name paramenter with the value got from the cell
        {
        	_currentRule.setName( value );
        }
        else if (actionType.type == ActionType.DURATION) // if the actionType type is DURATION then creates a new duration tag with the value got from the cell
        {
        	createDuration( column, 
        					value, 
        					actionType );
        }        
        else if ( actionType.type == ActionType.CONDITION )
        {
            createCondition( column,
                             value,
                             actionType );
        }
        else if ( actionType.type == ActionType.ACTION )
        {
            createConsequence( column,
                               value,
                               actionType );
        }


    }

    private Rule createNewRuleForRow(int row,
                                     List parameters)
    {
        String name = _currentRulePrefix + "_" + row;
        Integer salience = null;
        if ( _currentSequentialFlag )
        {
            salience = new Integer( Rule.calcSalience( row ) );
        }
        Rule rule = new Rule( name,
                              salience );
        rule.setComment( "From row number: " + (row + 1) );
        rule.setParameters( parameters );
        return rule;
    }

    private void createCondition(int column,
                                 String value,
                                 ActionType actionType)
    {

        Condition cond = new Condition( );
        cond.setSnippet( actionType.getSnippet( value ) );
        cond.setComment( cellComment( column ) );
        _currentRule.addCondition( cond );
    }

    
    // 08 - 16 - 2005 RIK: This function creates a new DURATION TAG if apply.
    // The value in the cell must be made with the first character of the parameter and the value next to it, separated by ":" character
    // Examples: w1:d3:h4 mean weeks="1" days="3" hours="4", m=1:s=45 means minutes="1" seconds="45"
    
    private void createDuration(int column,
            String value,
            ActionType actionType)
	{

		Duration dur = new Duration( );
		dur.setSnippet( value );
		dur.setComment( cellComment( column ) );
		_currentRule.setDuration( dur );
	}

    private void createConsequence(int column,
                                   String value,
                                   ActionType actionType)
    {

        Consequence cons = new Consequence( );
        cons.setSnippet( actionType.getSnippet( value ) );
        cons.setComment( cellComment( column ) );
        _currentRule.addConsequence( cons );
    }

    private boolean isCellValueEmpty(String value)
    {
        return value == null || "".equals( value.trim( ) );
    }

    private String cellComment(int column)
    {
        return "From column: " + Rule.convertColNumToColName( column );
    }

}
