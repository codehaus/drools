package org.drools.examples.house;

import java.util.ArrayList;
import java.util.List;

import org.drools.rule.Declaration;
import org.drools.rule.InvalidRuleException;
import org.drools.rule.Rule;
import org.drools.semantics.base.ClassObjectType;
import org.drools.smf.ConditionFactory;
import org.drools.smf.Configuration;
import org.drools.smf.FactoryException;
import org.drools.spi.Condition;
import org.drools.spi.ConditionException;
import org.drools.spi.RuleBaseContext;
import org.drools.spi.Tuple;

public class HouseConditionFactory
    implements
    ConditionFactory
{
    public Condition[] newCondition(Rule rule,
                                    RuleBaseContext context,
                                    Configuration config) throws FactoryException
    {
        Configuration childConfig = null;
        Configuration[] configurations = config.getChildren( );
        List conditions = new ArrayList( );
        for ( int i = 0; i < configurations.length; i++ )
        {
            childConfig = configurations[i];
            if ( childConfig.getName( ).equals( "room" ) )
            {
                conditions.add( processRoom( rule,
                                             childConfig ) );
            }
        }

        return (Condition[]) conditions.toArray( new Condition[conditions.size( )] );
    }

    private Condition processRoom(Rule rule,
                                  Configuration config) throws FactoryException
    {
        Configuration childConfig = null;
        final String roomName = config.getAttribute( "name" );
        final Declaration roomDeclaration = getDeclaration( rule,
                                                            Room.class,
                                                            roomName );
        Condition condition = null;
        Configuration[] configurations = config.getChildren( );
        for ( int i = 0; i < configurations.length; i++ )
        {
            childConfig = configurations[i];
            if ( childConfig.getName( ).equals( "temperature" ) )
            {
                final Declaration[] declarations = new Declaration[]{roomDeclaration};

                childConfig = childConfig.getChildren( )[0];
                final int kelvin = getKelvin( childConfig.getAttribute( "scale" ),
                                              childConfig.getText( ) );

                if ( childConfig.getName( ).equals( "less-than" ) )
                {
                    condition = new Condition( )
                    {
                        public Declaration[] getRequiredTupleMembers()
                        {
                            return declarations;
                        }

                        public boolean isAllowed(Tuple tuple) throws ConditionException
                        {
                            Room room = (Room) tuple.get( roomDeclaration );
                            if (!roomName.equals(room.getName()))
                            {
                                return false;
                            }
                            if ( room.getTemperature( ) < kelvin )
                            {
                                return true;
                            }
                            else
                            {
                                return false;
                            }
                        }
                    };
                }
                else if ( childConfig.getName( ).equals( "greater-than" ) )
                {
                    condition = new Condition( )
                    {
                        public Declaration[] getRequiredTupleMembers()
                        {
                            return declarations;
                        }

                        public boolean isAllowed(Tuple tuple) throws ConditionException
                        {
                            Room room = (Room) tuple.get( roomDeclaration );
                            if (!roomName.equals(room.getName()))
                            {
                                 return false;
                            }                            
                            if ( room.getTemperature( ) > kelvin )
                            {
                                return true;
                            }
                            else
                            {
                                return false;
                            }
                        }
                    };
                }
            }
        }
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

    private int getKelvin(String scale,
                          String value)
    {
        int i = Integer.parseInt( value );
        if ( scale.equals( "F" ) )
        {
            i = (int) Math.round( (i - 32) / 1.8 + 273 );
        }

        if ( scale.equals( "C" ) )
        {
            i = i + 273;
        }

        return i;
    }
}
