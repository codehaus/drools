package org.drools.examples.helloworld;

/*
 * $Id: HelloWorldNative.java,v 1.7 2004-12-16 19:17:30 dbarnett Exp $
 *
 * Copyright 2004 (C) The Werken Company. All Rights Reserved.
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
 * Company. "drools" is a trademark of The Werken Company.
 *
 * 5. Due credit should be given to The Werken Company. (http://werken.com/)
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

import org.drools.DroolsException;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.event.DebugWorkingMemoryEventListener;
import org.drools.reteoo.Dumper;
import org.drools.reteoo.HelloWorldBuilder;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.semantics.base.ClassObjectType;
import org.drools.spi.Condition;
import org.drools.spi.Consequence;
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
    public static void main( String[] args ) throws DroolsException
    {
        Rule helloRule = new Rule( "Hello World" );
        Rule goodbyeRule = new Rule( "Goodbye Cruel World" );
        Rule debugRule = new Rule( "Debug" );

        /*
         * Reuse the Java semantics ObjectType
         * so Drools can identify String and Object
         */
        ClassObjectType StringType = new ClassObjectType( String.class );
        ClassObjectType ObjectType = new ClassObjectType( Object.class );

        /*
         * Build the declaration and specify it
         * as a parameter of the hello Rule
         */
        final Declaration helloDeclaration =
            helloRule.addParameterDeclaration( "hello", StringType );

        /*
         * Build the declaration and specify it
         * as a parameter of the goodbye Rule
         */
        final Declaration goodbyeDeclaration =
            goodbyeRule.addParameterDeclaration( "goodbye",  StringType );

        /*
         * Build the declaration and specify it
         * as a parameter of the debug Rule
         */
        final Declaration debugDeclaration =
            debugRule.addParameterDeclaration( "object",  ObjectType );

        /*
         * Build and Add the Condition to the hello Rule
         */
        Condition conditionHello = new Condition( )
        {
            public boolean isAllowed(Tuple tuple)
            {
                String hello = ( String ) tuple.get( helloDeclaration );
                if ( hello == null ) return false;
                if ( hello.equals( "Hello" ) ) return true;
                return false;
            }

            public Declaration[] getRequiredTupleMembers()
            {
                return new Declaration[] { helloDeclaration };
            }
        };
        helloRule.addCondition( conditionHello );
        
        /*
         * Build and Add the Condition to the goodbye Rule
         */
        Condition conditionGoodbye = new Condition( )
        {
            public boolean isAllowed(Tuple tuple)
            {
                String goodbye = ( String ) tuple.get( goodbyeDeclaration );
                if ( goodbye == null ) return false;
                if ( goodbye.equals( "Goodbye" ) ) return true;
                return false;
            }

            public Declaration[] getRequiredTupleMembers()
            {
                return new Declaration[] { goodbyeDeclaration };
            }
        };
        goodbyeRule.addCondition( conditionGoodbye );

        // No condition to build and add to the debug Rule

        /*
         * Build and Add the Consequence to the hello Rule
         */
        Consequence helloConsequence = new Consequence( )
        {
            public void invoke( Tuple tuple, WorkingMemory workingMemory )
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
            public void invoke( Tuple tuple, WorkingMemory workingMemory )
            {
                String goodbye = ( String ) tuple.get( goodbyeDeclaration );
                System.out.println( goodbye + " Cruel World" );
            }
        };
        goodbyeRule.setConsequence( goodbyeConsequence );

        /*
         * Build and Add the Consequence to the debug Rule
         */
        Consequence debugConsequence = new Consequence( )
        {
            public void invoke( Tuple tuple, WorkingMemory workingMemory )
            {
                Object object = tuple.get( debugDeclaration );
                System.out.println( "Asserted object: " + object );
            }
        };
        debugRule.setConsequence( debugConsequence );

        RuleSet ruleSet = new RuleSet( "Hello World Example" );
        ruleSet.addRule( helloRule );
        ruleSet.addRule( goodbyeRule );
        ruleSet.addRule( debugRule );
        HelloWorldBuilder builder = new HelloWorldBuilder( ruleSet );
        RuleBase ruleBase = builder.buildRuleBase( );

        System.out.println( "DUMP RETE" );
        System.out.println( "---------" );
        Dumper dumper = new Dumper( ruleBase );
        dumper.dumpRete( System.out );

        System.out.println( "FIRE RULES(Hello)" );
        System.out.println( "----------" );
        WorkingMemory workingMemory = ruleBase.newWorkingMemory( );
        workingMemory.addEventListener(
            new DebugWorkingMemoryEventListener( ) );
        workingMemory.assertObject( "Hello" );
        workingMemory.fireAllRules( );

        System.out.println( "\n" );

        System.out.println( "FIRE RULES(GoodBye)" );
        System.out.println( "----------" );
        workingMemory = ruleBase.newWorkingMemory( );
        workingMemory.addEventListener(
            new DebugWorkingMemoryEventListener( ) );
        workingMemory.assertObject( "Goodbye" );
        workingMemory.fireAllRules( );
    }
}
