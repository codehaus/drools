package org.drools.reteoo;

import org.drools.RuleBase;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.spi.Condition;

import java.util.Iterator;
import java.util.List;

/**
 * This simple Rete builder and it create a completely unoptimised Rete, ie not
 * JoinNodes, but is an ideal starting point to learn more about how Rete's are
 * built. org.drools.reteoo should be consultated to get a greater undertanding
 * of optimisations
 */

public class HelloWorldBuilder
{
    private final Rete      rete;
    private final Rule[]    rules;

    public HelloWorldBuilder(RuleSet ruleSet)
    {
        this.rete = new Rete( );
        this.rules = ruleSet.getRules( );
    }

    public RuleBase buildRuleBase()
    {
        Declaration declaration;
        Rule rule;
        ParameterNode parameterNode;
        for ( int i = 0; i < rules.length; i++ )
        {
            rule = rules[i];
            for ( Iterator j = rule.getParameterDeclarations( ).iterator( ); j.hasNext( ); )
            {
                declaration = ( Declaration ) j.next();
                parameterNode = new ParameterNode( rule,
                                                   rete.getOrCreateObjectTypeNode( declaration.getObjectType( ) ),
                                                   declaration );

                List conditions = rule.getConditions( );
                if ( conditions.isEmpty( ) )
                {
                    TerminalNode terminal = new TerminalNode( parameterNode,
                                                              rule );
                }
                else
                {
                    for ( int k = 0; k < conditions.size( ); k++ )
                    {
                        TerminalNode terminal = new TerminalNode( new ConditionNode( parameterNode,
                                                                                     ( Condition ) conditions.get( k ),
                                                                                     k ),
                                                                  rule );
                    }
                }
            }
        }

        return new RuleBaseImpl( this.rete );
    }
}