package org.drools.examples.conway.rules;

import org.drools.examples.conway.Cell;
import org.drools.rule.Declaration;
import org.drools.rule.InvalidRuleException;
import org.drools.rule.Rule;
import org.drools.semantics.base.ClassObjectType;
import org.drools.smf.ConditionFactory;
import org.drools.smf.Configuration;
import org.drools.smf.FactoryException;
import org.drools.spi.Condition;
import org.drools.spi.RuleBaseContext;
import org.drools.spi.Tuple;

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:brown_j@ociweb.com">Jeff Brown</a>
 * @version $Id: ConwayConditionFactory.java,v 1.2 2005/04/13 00:02:54 brown_j
 *          Exp $
 */
public class ConwayConditionFactory
    implements
    ConditionFactory
{

    public Condition[] newCondition(Rule rule,
                                    RuleBaseContext context,
                                    final Configuration config) throws FactoryException
    {
        Configuration childConfig = null;
        Configuration[] configurations = config.getChildren( );
        List conditions = new ArrayList( );
        for ( int i = 0; i < configurations.length; i++ )
        {
            childConfig = configurations[i];
            if ( childConfig.getName( ).equals( "cell" ) )
            {
                conditions.add( processCell( rule,
                                             childConfig ) );
            }
        }

        return (Condition[]) conditions.toArray( new Condition[conditions.size( )] );
    }

    private Condition processCell(Rule rule,
                                  Configuration config) throws FactoryException
    {
        Condition condition = null;
        Configuration[] children = config.getChildren( );
        for ( int i = 0; i < children.length; i++ )
        {
            Configuration childConfig = children[i];
            final String cellName = config.getAttribute( "name" );

            final Declaration cellDeclaration = getDeclaration( rule,
                                                                Cell.class,
                                                                cellName );
            final Declaration[] declarations = new Declaration[]{cellDeclaration};
            final int numberOfNeighbors = Integer.parseInt( childConfig.getText( ) );

            if ( childConfig.getName( ).equals( "liveNeighborCountGreaterThan" ) )
            {
                condition = new Condition( ) {
                    public Declaration[] getRequiredTupleMembers()
                    {
                        return declarations;
                    }

                    public boolean isAllowed(Tuple tuple)
                    {

                        Cell cell = (Cell) tuple.get( cellDeclaration );
                        int n = cell.getNumberOfLiveNeighbors( );
                        boolean isAllowed = n > numberOfNeighbors;
                        return isAllowed;
                    }
                };
            }
            else if ( childConfig.getName( ).equals( "liveNeighborCountLessThan" ) )
            {
                condition = new Condition( ) {
                    public Declaration[] getRequiredTupleMembers()
                    {
                        return declarations;
                    }

                    public boolean isAllowed(Tuple tuple)
                    {

                        Cell cell = (Cell) tuple.get( cellDeclaration );
                        int n = cell.getNumberOfLiveNeighbors( );
                        boolean isAllowed = n < numberOfNeighbors;
                        return isAllowed;
                    }
                };
            }
            else if ( childConfig.getName( ).equals( "liveNeighborCountEquals" ) )
            {
                condition = new Condition( ) {
                    public Declaration[] getRequiredTupleMembers()
                    {
                        return declarations;
                    }

                    public boolean isAllowed(Tuple tuple)
                    {

                        Cell cell = (Cell) tuple.get( cellDeclaration );
                        int n = cell.getNumberOfLiveNeighbors( );
                        boolean isAllowed = n == numberOfNeighbors;
                        return isAllowed;
                    }
                };
            }
        }
        // final Configuration childConfig = config.getChildren()[0];
        return condition;
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
