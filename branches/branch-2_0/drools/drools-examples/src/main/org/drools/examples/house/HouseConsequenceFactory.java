package org.drools.examples.house;

import org.drools.rule.Declaration;
import org.drools.rule.InvalidRuleException;
import org.drools.rule.Rule;
import org.drools.semantics.base.ClassObjectType;
import org.drools.smf.Configuration;
import org.drools.smf.ConsequenceFactory;
import org.drools.smf.FactoryException;
import org.drools.spi.Consequence;
import org.drools.spi.ConsequenceException;
import org.drools.spi.RuleBaseContext;
import org.drools.spi.Tuple;

public class HouseConsequenceFactory
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
            if ( childConfig.getName( ).equals( "room" ) )
            {
                consequence = processRoom( rule,
                                           childConfig );
            }
        }

        return consequence;
    }

    private Consequence processRoom(Rule rule,
                                    Configuration config) throws FactoryException
    {
        Configuration childConfig = null;
        final String room = config.getAttribute( "name" );

        Consequence consequence = null;
        Configuration[] configurations = config.getChildren( );
        for ( int i = 0; i < configurations.length; i++ )
        {
            childConfig = configurations[i];
            if ( childConfig.getName( ).equals( "heating" ) )
            {
                final Declaration heatingDeclaration = getDeclaration( rule,
                                                                       Heating.class,
                                                                       "heating" );

                if ( childConfig.getText( ).equals( "on" ) )
                {
                    consequence = new Consequence( )
                    {
                        public void invoke(Tuple tuple) throws ConsequenceException
                        {
                            Heating heating = (Heating) tuple.get( heatingDeclaration );
                            heating.heatingOn( room );
                        }
                    };
                }
                else if ( childConfig.getText( ).equals( "off" ) )
                {
                    consequence = new Consequence( )
                    {
                        public void invoke(Tuple tuple) throws ConsequenceException
                        {
                            Heating heating = (Heating) tuple.get( heatingDeclaration );
                            heating.heatingOff( room );
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
