package org.drools.reteoo;

/*
 * $Id: EventTest.java,v 1.4.2.1 2005-06-03 00:21:34 memelet Exp $
 *
 * Copyright 2004-2005 (C) The Werken Company. All Rights Reserved.
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

import org.drools.DroolsTestCase;
import org.drools.FactHandle;
import org.drools.RuleBase;
import org.drools.TestWorkingMemoryEventListener;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.spi.InstrumentedCondition;
import org.drools.spi.MockObjectType;

/**
 *
 * @author mproctor
 *
 * @todo remove schedule items
 * @todo add/remove schedule items
 *
 */

public class EventTest //extends DroolsTestCase
{
//    public void testAddToAgenda() throws Exception
//    {
//        RuleSet ruleSet = new RuleSet( "test rule-set" );
//        Rule rule = new Rule( "test-rule" );
//
//        // rule.addParameterDeclaration( intDecl );
//        Declaration stringDecl1 = rule.addParameterDeclaration( "stringVar1",
//                                                                new MockObjectType( String.class ) );
//        Declaration stringDecl2 = rule.addParameterDeclaration( "stringVar2",
//                                                                new MockObjectType( String.class ) );
//        Declaration intDecl = rule.addParameterDeclaration( "intVar",
//                                                            new MockObjectType( Integer.class ) );
//
//        // add consequence
//        rule.setConsequence( new org.drools.spi.Consequence( )
//        {
//            public void invoke(org.drools.spi.Tuple tuple)
//            {
//                // Agenda agenda =
//                // ((WorkingMemoryImpl)workingMemory).getAgenda();
//                // agenda.addToAgenda( ( ReteTuple ) tuple, tuple.getRule( ) );
//                // System.err.println("fire");
//            }
//        } );
//        // add condition
//        InstrumentedCondition c1 = new InstrumentedCondition( );
//        c1.addDeclaration( stringDecl1 );
//        c1.isAllowed( true );
//        InstrumentedCondition c2 = new InstrumentedCondition( );
//        c2.addDeclaration( stringDecl2 );
//        c2.isAllowed( true );
//        InstrumentedCondition c3 = new InstrumentedCondition( );
//        c3.addDeclaration( intDecl );
//        c3.isAllowed( true );
//        rule.addCondition( c1 );
//        rule.addCondition( c2 );
//        rule.addCondition( c3 );
//
//        // rule.addCondition( new org.drools.spi.InstrumentedCondition( ) );
//        ruleSet.addRule( rule );
//        Builder builder = new Builder( );
//        builder.addRuleSet( ruleSet );
//        RuleBase ruleBase = builder.buildRuleBase( );
//
//        WorkingMemoryImpl workingMemory = (WorkingMemoryImpl) ruleBase.newWorkingMemory( );
//        TestWorkingMemoryEventListener listener = new TestWorkingMemoryEventListener( );
//        workingMemory.addEventListener( listener );
//
//        /*
//         * This is not recursive so a rule should not be able to activate itself
//         */
//
//        assertEquals( 0,
//                      listener.asserted );
//        assertEquals( 0,
//                      listener.tested );
//        FactHandle stringFact1 = workingMemory.assertObject( "cheddar" );
//        FactHandle stringFact2 = workingMemory.assertObject( "brie" );
//        FactHandle intFact = workingMemory.assertObject( new Integer( 5 ) );
//        assertEquals( 3,
//                      listener.asserted );
//        assertEquals( 5,
//                      listener.tested );
//        assertEquals( 4,
//                      listener.created );
//        assertEquals( 4,
//                      workingMemory.getAgenda( ).size( ) );
//
//        assertEquals( 0,
//                      listener.fired );
//
//        workingMemory.modifyObject( stringFact1,
//                                    "stilton" );
//
//        workingMemory.fireAllRules( );
//
//        assertEquals( 4,
//                      listener.fired );
//        assertEquals( 0,
//                      workingMemory.getAgenda( ).size( ) );
//
//        assertEquals( 1,
//                      listener.modified );
//        assertEquals( 0,
//                      listener.cancelled );
//
//        workingMemory.modifyObject( stringFact1,
//                                    "gouda" );
//
//        assertEquals( 3,
//                      workingMemory.getAgenda( ).size( ) );
//        assertEquals( 0,
//                      listener.cancelled );
//        assertEquals( 2,
//                      listener.modified );
//        assertEquals( 9,
//                      listener.tested );
//        assertEquals( 7,
//                      listener.created );
//        assertEquals( 0,
//                      listener.cancelled );
//        workingMemory.clearAgenda( );
//        assertEquals( 3,
//                      listener.cancelled );
//
//        assertEquals( 0,
//                      listener.retracted );
//        workingMemory.retractObject( stringFact2 );
//        assertEquals( 1,
//                      listener.retracted );
//    }
}
