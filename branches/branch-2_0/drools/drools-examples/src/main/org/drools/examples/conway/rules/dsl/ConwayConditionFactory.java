package org.drools.examples.conway.rules.dsl;

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

import java.util.ArrayList;
import java.util.List;

/**
 * @author <a href="mailto:brown_j@ociweb.com">Jeff Brown</a>
 * @version $Id: ConwayConditionFactory.java,v 1.1.2.1 2005-05-16 23:04:31 brownj Exp $
 */
public class ConwayConditionFactory
        implements
        ConditionFactory
{

    public Condition[] newCondition(Rule rule,
                                    RuleBaseContext context,
                                    final Configuration config) throws FactoryException
    {
        Configuration[] configurations = config.getChildren();
        List conditions = new ArrayList();
        final String cellName = config.getAttribute( "cellName" );
        final Declaration cellDeclaration = getDeclaration( rule,
                                                            Cell.class,
                                                            cellName );
        for ( int i = 0; i < configurations.length; i++ )
        {
            Configuration childConfig1 = null;
            childConfig1 = configurations[i];


                    if ( childConfig1.getName().equals( "cellIsAlive" ) )
                    {
                        conditions.add( new IsCellAliveCondition( cellDeclaration ) );
                    } else if ( childConfig1.getName().equals( "cellIsDead" ) )
                    {
                        conditions.add( new IsCellDeadCondition( cellDeclaration ) );
                    } else if ( childConfig1.getName().equals( "cellIsOverCrowded" ) )
                    {
                        conditions.add( new OvercrowdedCondition( cellDeclaration) );
                    } else if ( childConfig1.getName().equals( "cellIsLonely" ) )
                    {
                        conditions.add( new LonelyCondition( cellDeclaration ) );
                    } else if ( childConfig1.getName().equals( "cellIsRipeForBirth" ) )
                    {
                        conditions.add( new RipeForBirthCondition( cellDeclaration) );
                    }
        }

        return (Condition[]) conditions.toArray( new Condition[conditions.size()] );
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
                final FactoryException factoryException = new FactoryException( "Error occurred establishing parameter." );
                factoryException.initCause( e );
                throw factoryException;
            }
        }
        return declaration;
    }


}
