package org.drools.reteoo;

/*
 * $Id: AgendaTest.java,v 1.16 2005-09-25 17:57:26 mproctor Exp $
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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import org.drools.DroolsTestCase;
import org.drools.FactHandle;
import org.drools.MockFactHandle;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spi.Activation;
import org.drools.spi.AgendaFilter;
import org.drools.spi.Consequence;
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

        // add consequence
        rule.setConsequence( new org.drools.spi.Consequence( )
        {
            public void invoke(org.drools.spi.Tuple tuple)
            {             
                agenda.addToAgenda( (ReteTuple) tuple,
                                    rule );
            }
        } );

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
    
    public void testXorGroup() throws Exception
    {
        RuleBase ruleBase = new RuleBaseImpl( new Rete( ) );

        WorkingMemoryImpl workingMemory = (WorkingMemoryImpl) ruleBase.newWorkingMemory( );
        Agenda agenda = workingMemory.getAgenda( );        
        
        Consequence consequence = new org.drools.spi.Consequence( )
        {
            public void invoke(org.drools.spi.Tuple tuple)
            {
            }
        };
        
                
        Rule rule1 = new Rule( "rule1" );
        rule1.setSalience( 50 );
        rule1.setXorGroup( "group1" );

        /* this rule should fire first and be the only rule to fire */
        Rule rule2 = new Rule( "rule2" );
        rule2.setConsequence( consequence );
        rule2.setXorGroup( "group1" );
        rule2.setSalience( 100 );
        
        Rule rule3 = new Rule( "rule3" );
        rule3.setDuration( 1000 );
        rule3.setSalience( 40 );        
        rule3.setXorGroup( "group1" );
        
        Rule rule4 = new Rule( "rule4" );
        rule4.setSalience( 30 );        
        rule4.setXorGroup( "group2" );      
        
        Rule rule5 = new Rule( "rule5" );
        rule4.setSalience( 20 );
        rule5.setXorGroup( null );                               

        ReteTuple tuple = new ReteTuple( workingMemory );


        agenda.addToAgenda( tuple,
                            rule1 );        
        agenda.addToAgenda( tuple,
                            rule2 );        
        agenda.addToAgenda( tuple,
                            rule3 );        
        agenda.addToAgenda( tuple,
                            rule4 );        
        agenda.addToAgenda( tuple,
                            rule5 );        
        
        List activations = agenda.getActivations();
        assertEquals( 5,
                      activations.size( ) );   
             
        agenda.fireNextItem( null );

        List rules = new ArrayList();
        Iterator it = activations.iterator();
        Activation activation;
        while ( it.hasNext() )
        {
            activation = ( Activation ) it.next();
            rules.add( activation.getRule() );
        }
        
        assertContains( rule4,
                        rules );
        
        assertContains( rule5,
                        rules );                       
    }
    
    public void testFilters() throws Exception
    {
        RuleBase ruleBase = new RuleBaseImpl( new Rete( ) );

        WorkingMemoryImpl workingMemory = (WorkingMemoryImpl) ruleBase.newWorkingMemory( );
        final Agenda agenda = workingMemory.getAgenda( );

        final Rule rule = new Rule( "test-rule" );

        // add consequence
        rule.setConsequence( new org.drools.spi.Consequence( )
        {
            public void invoke(org.drools.spi.Tuple tuple)
            {
                agenda.addToAgenda( (ReteTuple) tuple,
                                    rule );
            }
        } );

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
