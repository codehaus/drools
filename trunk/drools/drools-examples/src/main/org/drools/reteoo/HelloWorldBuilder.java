package org.drools.reteoo;

import org.drools.rule.RuleSet;
import org.drools.RuleBase;
import org.drools.rule.Rule;
import org.drools.rule.Declaration;
import org.drools.spi.Condition;
import org.drools.spi.ObjectType;
import org.drools.spi.ConflictResolver;
import org.drools.semantics.java.ClassObjectType;
import org.drools.conflict.DefaultConflictResolver;


/**
 * This simple Rete builder and it create a completely unoptimised Rete, ie not JoinNodes,
 * but is an ideal starting point to learn more about how Rete's are built.
 * org.drools.reteoo should be consultated to get a greater undertanding of optimisations
 */

public class HelloWorldBuilder
{
    Rete rete;
    Rule[] rules;
    ConflictResolver conflictResolver;

    public HelloWorldBuilder(RuleSet ruleSet)
    {
        this.rete             = new Rete();
        this.conflictResolver = DefaultConflictResolver.getInstance();
        this.rules = ruleSet.getRules();
    }

    public RuleBase buildRuleBase()
    {
        Declaration declaration;
        ObjectType objectType;
        Rule rule;
        ParameterNode parameterNode;
        ConditionNode conditionNode;
        for (int i = 0; i < rules.length; i++)
        {
            rule = rules[i];
            Declaration[] declarations = rule.getParameterDeclarations();
            for (int j = 0; j < declarations.length; j++)
            {
                declaration = declarations[j];
                objectType = declaration.getObjectType();
                ObjectTypeNode objectTypeNode = rete.getOrCreateObjectTypeNode(objectType);
                parameterNode = new ParameterNode( rule,
                                                   objectTypeNode,
                                                   declaration );

                Condition[] conditions = rule.getConditions();
                for (int k = 0; k < conditions.length; k++)
                {
                    conditionNode = new ConditionNode( parameterNode,
                                                       conditions[k] );
                    TerminalNode terminal = new TerminalNode( conditionNode,
                                                              rule );
                }
            }
        }
        RuleBase ruleBase = new RuleBaseImpl( this.rete, this.conflictResolver );
        return ruleBase;
    }
}