package org.drools.reteoo;

/*
 * $Id: AgendaTest.java,v 1.15 2005-09-07 11:11:22 michaelneale Exp $
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
import org.drools.MockFactHandle;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spi.Activation;
import org.drools.spi.AgendaFilter;
import org.drools.spi.MockObjectType;

/**
 * @author mproctor
 */

public class AgendaTest extends DroolsTestCase
{
    public void testAddToAgenda() throws Exception
    {
        RuleBase ruleBase = new RuleBaseImpl( new Rete( ) );

        WorkingMemoryImpl workingMemory = (WorkingMemoryImpl) ruleBase.newWorkingMemory( );
        final Agenda agenda = workingMemory.getAgenda( );

        final Rule rule = new Rule( "test-rule" );

        rule.addParameterDeclaration( "paramVar",
                                      new MockObjectType( true ) );

        // add consequence
        rule.setConsequence( new org.drools.spi.Consequence( )
        {
            public void invoke(org.drools.spi.Tuple tuple)
            {             
                agenda.addToAgenda( (ReteTuple) tuple,
                                    rule );
            }
        } );

        // add condition
        rule.addCondition( new org.drools.spi.InstrumentedCondition( ) );

        // add condition
        rule.addCondition( new org.drools.spi.InstrumentedCondition( ) );

        ReteTuple tuple = new ReteTuple( workingMemory );

        assertEquals( 0,
                      agenda.size( ) );

        /*
         * This is not recursive so a rule should not be able to activate itself
         */
        rule.setNoLoop( true );
        agenda.addToAgenda( tuple,
                            rule );
        assertEquals( 1,
                      agenda.size( ) );
        agenda.fireNextItem( null );
        assertEquals( 0,
                      agenda.size( ) );

        /*
         * This is recursive so a rule should be able to activate itself
         */
        rule.setNoLoop( false );
        agenda.addToAgenda( tuple,
                            rule );
        assertEquals( 1,
                      agenda.size( ) );
        agenda.fireNextItem( null );
        assertEquals( 1,
                      agenda.size( ) );
        agenda.clearAgenda( );
    }
    
    /**
     * Test for the case of a Rule trying to add itself to the agenda.
     * In this case it *should* work as it is a differen tuple.
     * No-loop is only meant to work for the same Rule/TupleKey combination.
     */
    public void testNoLoopDifferentTuple() throws Exception {
        RuleBase ruleBase = new RuleBaseImpl( new Rete( ) );

        final WorkingMemoryImpl workingMemory = (WorkingMemoryImpl) ruleBase.newWorkingMemory( );
        final Agenda agenda = workingMemory.getAgenda( );

        final Rule rule = new Rule( "test-rule" );

        rule.addParameterDeclaration( "paramVar",
                                      new MockObjectType( true ) );

        // add consequence
        rule.setConsequence( new org.drools.spi.Consequence( )
        {
            public void invoke(org.drools.spi.Tuple tuple)
            {        
                //throw in a different tuple, to check the agenda doesn't stop the rule/tuple combo
                Declaration dec = new Declaration("paramVar", new MockObjectType(true), 1);
                FactHandle fact = new FactHandleImpl(42);
                ReteTuple different = new ReteTuple(workingMemory, dec, fact);
                agenda.addToAgenda( different,
                                    rule );
            }
        } );

        ReteTuple tuple = new ReteTuple( workingMemory );
        
        /*
         * This is not recursive but different fact so should activate another rule
         */
        rule.setNoLoop( true );
        agenda.addToAgenda( tuple,
                            rule );
        assertEquals( 1,
                      agenda.size( ) );
        agenda.fireNextItem( null );
        assertEquals( 1,
                      agenda.size( ) );        
        
    }

    public void testFilters() throws Exception
    {
        RuleBase ruleBase = new RuleBaseImpl( new Rete( ) );

        WorkingMemoryImpl workingMemory = (WorkingMemoryImpl) ruleBase.newWorkingMemory( );
        final Agenda agenda = workingMemory.getAgenda( );

        final Rule rule = new Rule( "test-rule" );
        rule.addParameterDeclaration( "paramVar",
                                      new MockObjectType( true ) );

        // add consequence
        rule.setConsequence( new org.drools.spi.Consequence( )
        {
            public void invoke(org.drools.spi.Tuple tuple)
            {
                agenda.addToAgenda( (ReteTuple) tuple,
                                    rule );
            }
        } );
        // add condition
        rule.addCondition( new org.drools.spi.InstrumentedCondition( ) );

        // add condition
        rule.addCondition( new org.drools.spi.InstrumentedCondition( ) );

        ReteTuple tuple = new ReteTuple( workingMemory );

        assertEquals( 0,
                      agenda.size( ) );

        /*
         * Add to agenda
         */
        rule.setNoLoop( false );
        agenda.addToAgenda( tuple,
                            rule );
        assertEquals( 1,
                      agenda.size( ) );
        agenda.fireNextItem( null );
        assertEquals( 1,
                      agenda.size( ) );
        agenda.clearAgenda( );

        /*
         * True filter, activations should always add
         */
        AgendaFilter filterTrue = new AgendaFilter( )
        {
            public boolean accept(Activation item)
            {
                return true;
            }
        };
        rule.setNoLoop( false );
        agenda.addToAgenda( tuple,
                            rule );
        assertEquals( 1,
                      agenda.size( ) );
        agenda.fireNextItem( filterTrue );
        assertEquals( 1,
                      agenda.size( ) );
        agenda.clearAgenda( );

        /*
         * False filter, activations should always be denied
         */
        AgendaFilter filterFalse = new AgendaFilter( )
        {
            public boolean accept(Activation item)
            {
                return false;
            }
        };
        rule.setNoLoop( false );
        agenda.addToAgenda( tuple,
                            rule );
        assertEquals( 1,
                      agenda.size( ) );
        agenda.fireNextItem( filterFalse );
        assertEquals( 0,
                      agenda.size( ) );
    }

    public void testClearAgenda() throws Exception
    {
        RuleBase ruleBase = new RuleBaseImpl( new Rete( ) );

        WorkingMemoryImpl workingMemory = (WorkingMemoryImpl) ruleBase.newWorkingMemory( );
        final Agenda agenda = workingMemory.getAgenda( );

        final Rule rule = new Rule( "test-rule" );
        rule.addParameterDeclaration( "paramVar",
                                      new MockObjectType( true ) );

        // add consequence
        rule.setConsequence( new org.drools.spi.Consequence( )
        {
            public void invoke(org.drools.spi.Tuple tuple)
            {
                WorkingMemory workingMemory = tuple.getWorkingMemory( );

                agenda.addToAgenda( (ReteTuple) tuple,
                                    rule );
                workingMemory.clearAgenda( );
            }
        } );
        // add condition
        rule.addCondition( new org.drools.spi.InstrumentedCondition( ) );

        // add condition
        rule.addCondition( new org.drools.spi.InstrumentedCondition( ) );

        ReteTuple tuple = new ReteTuple( workingMemory );

        assertEquals( 0,
                      agenda.size( ) );

        /*
         * This is recursive so a rule should be able to activate itself But
         * then we clear the agenda afterwards to size should still be 0
         */
        rule.setNoLoop( false );
        agenda.addToAgenda( tuple,
                            rule );
        assertEquals( 1,
                      agenda.size( ) );
        agenda.fireNextItem( null );
        assertEquals( 0,
                      agenda.size( ) );
    }

}
