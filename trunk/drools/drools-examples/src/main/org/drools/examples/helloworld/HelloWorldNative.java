package org.drools.examples.helloworld;

import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.event.DebugWorkingMemoryEventListener;
import org.drools.reteoo.Dumper;
import org.drools.reteoo.HelloWorldBuilder;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.semantics.java.ClassObjectType;
import org.drools.spi.Condition;
import org.drools.spi.ConditionException;
import org.drools.spi.Consequence;
import org.drools.spi.ConsequenceException;
import org.drools.spi.Tuple;

/**
 * This simple Hello World example demonstrates how to build a native RuleSet
 * without using one of the many Semantic Modules. It is an ideal examples to
 * learn what is happening under the hood. It is accompanied by
 * HelloWorldBuilder which is a simplified Rete builder to illustrate what
 * happens in org.drools.reteoo.Builder
 */

public class HelloWorldNative
{
    public static void main(String[] args) throws Exception
    {
        Rule helloRule = new Rule( "Hello World" );
        Rule goodbyeRule = new Rule( "Goodbye Cruel World" );

        /*
         * Reuse the java semantics ObjectType so drools can identify String
         */
        ClassObjectType StringType = new ClassObjectType( String.class );

        /*
         * Build the declaration and specify it as a parameter of the hello Rule
         */
        final Declaration helloDeclaration = new Declaration( StringType,
                                                              "hello" );
        helloRule.addParameterDeclaration( helloDeclaration );

        /*
         * Build the declaration and specify it as a parameter of the goodbye
         * Rule
         */
        final Declaration goodbyeDeclaration = new Declaration( StringType,
                                                                "goodbye" );
        goodbyeRule.addParameterDeclaration( goodbyeDeclaration );

        /*
         * Build and Add the Condition to the hello Rule
         */
        Condition conditionHello = new Condition( )
        {
            public boolean isAllowed(Tuple tuple) throws ConditionException
            {
                String hello = ( String ) tuple.get( helloDeclaration );
                if ( hello == null ) return false;
                if ( hello.equals( "Hello" ) ) return true;
                return false;
            }

            public Declaration[] getRequiredTupleMembers()
            {
                return new Declaration[]{helloDeclaration};
            }
        };
        helloRule.addCondition( conditionHello );

        /*
         * Build and Add the Condition to the goodbye Rule
         */
        Condition conditionGoodbye = new Condition( )
        {
            public boolean isAllowed(Tuple tuple) throws ConditionException
            {
                String goodbye = ( String ) tuple.get( goodbyeDeclaration );
                if ( goodbye == null ) return false;
                if ( goodbye.equals( "Goodbye" ) ) return true;
                return false;
            }

            public Declaration[] getRequiredTupleMembers()
            {
                return new Declaration[]{goodbyeDeclaration};
            }
        };
        goodbyeRule.addCondition( conditionGoodbye );

        /*
         * Build and Add the Consequence to the helo Rule
         */
        Consequence helloConsequence = new Consequence( )
        {
            public void invoke(Tuple tuple, WorkingMemory workingMemory) throws ConsequenceException
            {
                String hello = ( String ) tuple.get( helloDeclaration );
                System.out.println( hello + " World" );
            }
        };
        helloRule.setConsequence( helloConsequence );

        /*
         * Build and Add the Consequence to the goodbye Rule
         */
        Consequence goodbyeConsequence = new Consequence( )
        {
            public void invoke(Tuple tuple, WorkingMemory workingMemory) throws ConsequenceException
            {
                String goodbye = ( String ) tuple.get( goodbyeDeclaration );
                System.out.println( goodbye + " Cruel World" );

            }
        };
        goodbyeRule.setConsequence( goodbyeConsequence );

        RuleSet ruleSet = new RuleSet( "Hello World Example" );
        ruleSet.addRule( helloRule );
        ruleSet.addRule( goodbyeRule );
        HelloWorldBuilder builder = new HelloWorldBuilder( ruleSet );
        RuleBase ruleBase = builder.buildRuleBase( );

        System.out.println( "DUMP RETE" );
        System.out.println( "---------" );
        Dumper dumper = new Dumper( ruleBase );
        dumper.dumpRete( System.out );

        System.out.println( "FIRE RULES(Hello)" );
        System.out.println( "----------" );
        WorkingMemory workingMemory = ruleBase.newWorkingMemory( );
        workingMemory.addEventListener( new DebugWorkingMemoryEventListener( ) );
        workingMemory.assertObject( "Hello" );
        workingMemory.fireAllRules( );

        System.out.println( "\n" );

        System.out.println( "FIRE RULES(GoodBye)" );
        System.out.println( "----------" );
        workingMemory = ruleBase.newWorkingMemory( );
        workingMemory.addEventListener( new DebugWorkingMemoryEventListener( ) );
        workingMemory.assertObject( "Goodbye" );
        workingMemory.fireAllRules( );
    }
}