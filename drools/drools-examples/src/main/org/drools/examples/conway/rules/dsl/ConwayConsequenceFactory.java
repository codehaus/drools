package org.drools.examples.conway.rules.dsl;

import org.drools.examples.conway.Cell;
import org.drools.rule.Declaration;
import org.drools.rule.InvalidRuleException;
import org.drools.rule.Rule;
import org.drools.semantics.base.ClassObjectType;
import org.drools.smf.Configuration;
import org.drools.smf.ConsequenceFactory;
import org.drools.smf.FactoryException;
import org.drools.spi.Consequence;
import org.drools.spi.RuleBaseContext;

/**
 * @author <a href="mailto:brown_j@ociweb.com">Jeff Brown</a>
 * @version $Id: ConwayConsequenceFactory.java,v 1.1.2.2 2005-05-16 23:08:22 brownj Exp $
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
        Configuration[] configurations = config.getChildren();
        Consequence consequence = null;
        final String cellName = config.getAttribute( "cellName" );
        final Declaration cellDeclaration = getDeclaration( rule,
                                                            Cell.class,
                                                            cellName );
        for ( int i = 0; i < configurations.length; i++ )
        {
            childConfig = configurations[i];

            if ( childConfig.getName().equals( "giveBirthToCell" ) )
            {
                consequence = new GiveBirthConsequence( cellDeclaration );
            } else if ( childConfig.getName().equals( "killCell" ) )
            {
                consequence = new KillCellConsequence( cellDeclaration );
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
                final FactoryException factoryException = new FactoryException( "Error occurred establishing parameter." );
                factoryException.initCause( e );
                throw factoryException;
            }
        }
        return declaration;
    }

}