package org.drools.reteoo;

import org.drools.RuleBase;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.spi.Condition;
import org.drools.spi.ObjectType;

import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

/**
 * This simple Rete builder and it create a completely unoptimised Rete, ie not
 * JoinNodes, but is an ideal starting point to learn more about how Rete's are
 * built. org.drools.reteoo should be consultated to get a greater undertanding
 * of optimisations
 */

public class HelloWorldBuilder
{
    Rete             rete;

    Rule[]           rules;

    public HelloWorldBuilder(RuleSet ruleSet)
    {
        this.rete = new Rete( );
        this.rules = ruleSet.getRules( );
    }

    public RuleBase buildRuleBase()
    {
        Declaration declaration;
        ObjectType objectType;
        Rule rule;
        ParameterNode parameterNode;
        ConditionNode conditionNode;
        for ( int i = 0; i < rules.length; i++ )
        {
            rule = rules[i];
            SortedSet declarations = rule.getParameterDeclarations( );
            for ( Iterator j = declarations.iterator(); j.hasNext(); )
            {
                declaration = ( Declaration ) j.next();
                objectType = declaration.getObjectType( );
                ObjectTypeNode objectTypeNode = rete.getOrCreateObjectTypeNode( objectType );
                parameterNode = new ParameterNode( rule, objectTypeNode, declaration );

                List conditions = rule.getConditions( );
                if ( 0 == conditions.size() ) {
                    TerminalNode terminal = new TerminalNode( parameterNode, rule );
                } else {
                    for ( int k = 0; k < conditions.size( ); k++ )
                    {
                        conditionNode = new ConditionNode( parameterNode, ( Condition ) conditions.get( k ), k );
                        TerminalNode terminal = new TerminalNode( conditionNode, rule );
                    }
                }
            }
        }
        RuleBase ruleBase = new RuleBaseImpl( this.rete );
        return ruleBase;
    }
}