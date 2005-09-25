package org.drools.examples.house;

/*
 * $Id: HouseConditionFactory.java,v 1.2 2005-05-08 04:22:34 dbarnett Exp $
 *
 * Copyright 2005-2005 (C) The Werken Company. All Rights Reserved.
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
