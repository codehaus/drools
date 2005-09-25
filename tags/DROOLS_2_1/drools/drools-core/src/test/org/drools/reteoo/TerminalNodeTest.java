package org.drools.reteoo;

/*
 * $Id: TerminalNodeTest.java,v 1.26 2005-05-08 04:05:13 dbarnett Exp $
 *
 * Copyright 2001-2005 (C) The Werken Company. All Rights Reserved.
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
import org.drools.RuleBase;
import org.drools.conflict.DefaultConflictResolver;
import org.drools.rule.Rule;
import org.drools.spi.InstrumentedConsequence;

public class TerminalNodeTest extends DroolsTestCase
{
    public void testAssertTuple() throws Exception
    {
        final Agenda[] agendas = new Agenda[1];

        WorkingMemoryImpl memory = new WorkingMemoryImpl( new RuleBaseImpl( new Rete() ) )
        {
            public Agenda getAgenda()
            {
                return agendas[0];
            }
        };

        final InstrumentedAgenda agenda = new InstrumentedAgenda( memory, DefaultConflictResolver.getInstance() );
        agendas[ 0 ] = agenda;

        Rule rule = new Rule( "test-rule" );

        //add consequence
        rule.setConsequence( new org.drools.spi.InstrumentedConsequence( ) );
        //add condition
        rule.addCondition( new org.drools.spi.InstrumentedCondition( ) );

        TerminalNode node = new TerminalNode( new MockTupleSource( ), rule );

        RuleBase ruleBase = new RuleBaseImpl( new Rete( ) );

        ReteTuple tuple = new ReteTuple( (WorkingMemoryImpl) ruleBase.newWorkingMemory( ) );

        node.assertTuple( tuple, memory );

        assertLength( 1, agenda.getAdded( ) );

        assertContains( tuple, agenda.getAdded( ) );
    }

    public void testRetractTuples() throws Exception
    {
        final Agenda[] agendas = new Agenda[1];

        WorkingMemoryImpl memory = new WorkingMemoryImpl( new RuleBaseImpl( new Rete() ) )
        {
            public Agenda getAgenda()
            {
                return agendas[0];
            }
        };

        final InstrumentedAgenda agenda = new InstrumentedAgenda(memory,
                                                                  DefaultConflictResolver.getInstance( ) );
        agendas[0] = agenda;

        Rule rule = new Rule( "test-rule" );

        InstrumentedConsequence consequence = new InstrumentedConsequence( );

        rule.setConsequence( consequence );

        TerminalNode node = new TerminalNode( new MockTupleSource( ), rule );

        TupleKey key = TupleKey.EMPTY_KEY;

        node.retractTuples( key, memory );

        assertLength( 1, agenda.getRemoved( ) );

        assertContains( key, agenda.getRemoved( ) );
    }
}
