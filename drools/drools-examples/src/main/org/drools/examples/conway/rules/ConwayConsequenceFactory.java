package org.drools.examples.conway.rules;

import org.drools.examples.conway.Cell;
import org.drools.examples.conway.CellState;
import org.drools.rule.Declaration;
import org.drools.rule.InvalidRuleException;
import org.drools.rule.Rule;
import org.drools.semantics.base.ClassObjectType;
import org.drools.smf.Configuration;
import org.drools.smf.ConsequenceFactory;
import org.drools.smf.FactoryException;
import org.drools.spi.Consequence;
import org.drools.spi.RuleBaseContext;
import org.drools.spi.Tuple;

/**
 * @author <a href="mailto:brown_j@ociweb.com">Jeff Brown</a>
 * @version $Id: ConwayConsequenceFactory.java,v 1.2 2005/04/13 00:03:26 brown_j
 *          Exp $
 */
public class ConwayConsequenceFactory
    implements
    ConsequenceFactory
{
    public Consequence newConsequence(Rule rule,
                                      RuleBaseContext context,
                                      Configuration config) throws FactoryException
    {

        Configuration childConfig = null;
        Configuration[] configurations = config.getChildren( );
        Consequence consequence = null;
        for ( int i = 0; i < configurations.length; i++ )
        {
            childConfig = configurations[i];
            if ( childConfig.getName( ).equals( "cell" ) )
            {
                consequence = processCell( rule,
                                           childConfig );
            }
        }

        return consequence;
    }

    private Consequence processCell(Rule rule,
                                    Configuration config) throws FactoryException
    {
        Configuration childConfig = null;
        final String cellName = config.getAttribute( "name" );

        Consequence consequence = null;
        Configuration[] configurations = config.getChildren( );
        for ( int i = 0; i < configurations.length; i++ )
        {
            childConfig = configurations[i];
            if ( childConfig.getName( ).equals( "queueState" ) )
            {
                final Declaration cellDeclaration = getDeclaration( rule,
                                                                    Cell.class,
                                                                    cellName );

                if ( childConfig.getText( ).equals( "live" ) )
                {
                    consequence = new Consequence( ) {
                        public void invoke(Tuple tuple)
                        {
                            Cell cell = (Cell) tuple.get( cellDeclaration );
                            cell.queueNextCellState( CellState.LIVE );
                        }
                    };
                }
                else if ( childConfig.getText( ).equals( "dead" ) )
                {
                    consequence = new Consequence( ) {
                        public void invoke(Tuple tuple)
                        {
                            Cell cell = (Cell) tuple.get( cellDeclaration );
                            cell.queueNextCellState( CellState.DEAD );
                        }
                    };
                }
            }
        }
        return consequence;
    }

    private Declaration getDeclaration(Rule rule,
                                       Class clazz,
                                       String identifier) throws FactoryException
    {
        Declaration declaration = rule.getParameterDeclaration( identifier );
        if ( declaration == null )
        {
            ClassObjectType type = new ClassObjectType( clazz );
            try
            {
                declaration = rule.addParameterDeclaration( identifier,
                                                            type );
            }
            catch ( InvalidRuleException e )
            {
                throw new FactoryException( "xxx" );
            }
        }
        return declaration;
    }

}