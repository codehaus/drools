package org.drools.reteoo;

/*
 * $Id: AgendaItemTest.java,v 1.18 2005-05-08 04:05:13 dbarnett Exp $
 *
 * Copyright 2003-2005 (C) The Werken Company. All Rights Reserved.
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
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spi.InstrumentedConsequence;
import org.drools.spi.MockObjectType;

public class AgendaItemTest extends DroolsTestCase
{
    public void testConstruct() throws Exception
    {
        FactHandleImpl handle = new FactHandleImpl( 1 );

        Rule rule = new Rule( "test-rule" );
        Declaration decl = rule.addParameterDeclaration( "paramVar", new MockObjectType( true ) );

        //add consequence
        rule.setConsequence( new org.drools.spi.InstrumentedConsequence( ) );
        //add condition
        rule.addCondition( new org.drools.spi.InstrumentedCondition( ) );

        ReteTuple tuple = new ReteTuple( null, decl, handle );

        AgendaItem item = new AgendaItem( tuple, rule );

        assertSame( rule, item.getRule( ) );

        assertSame( tuple, item.getTuple( ) );

        assertSame( tuple.getKey( ), item.getKey( ) );

        assertTrue( item.dependsOn( handle ) );
        assertFalse( item.dependsOn( new FactHandleImpl( 2 ) ) );
    }

    public void testSetTuple() throws Exception
    {
        FactHandleImpl handle = new FactHandleImpl( 1 );
        Rule rule = new Rule( "test-rule" );
        Declaration decl = rule.addParameterDeclaration( "paramVar", new MockObjectType( true ) );

        //add consequence
        rule.setConsequence( new org.drools.spi.InstrumentedConsequence( ) );
        //add condition
        rule.addCondition( new org.drools.spi.InstrumentedCondition( ) );

        ReteTuple tuple = new ReteTuple( null, decl, handle );

        AgendaItem item = new AgendaItem( tuple, rule );

        assertSame( tuple, item.getTuple( ) );

        assertTrue( item.dependsOn( handle ) );
        assertFalse( item.dependsOn( new FactHandleImpl( 2 ) ) );

        ReteTuple newTuple = new ReteTuple( null, decl, handle );

        item.setTuple( newTuple );

        assertSame( newTuple, item.getTuple( ) );

        assertTrue( item.dependsOn( handle ) );
        assertFalse( item.dependsOn( new FactHandleImpl( 2 ) ) );
    }

    public void testFire() throws Exception
    {

        RuleBase ruleBase = new RuleBaseImpl( new Rete( ) );

        FactHandleImpl handle = new FactHandleImpl( 1 );

        Rule rule = new Rule( "test-rule" );

        Declaration decl = rule.addParameterDeclaration( "cheese", new MockObjectType( true ) );

        ReteTuple tuple = new ReteTuple( (WorkingMemoryImpl) ruleBase.newWorkingMemory( ), decl, handle );

        InstrumentedConsequence consequence = new InstrumentedConsequence( );

        rule.setConsequence( consequence );

        AgendaItem item = new AgendaItem( tuple, rule );

        item.fire( ( WorkingMemoryImpl ) tuple.getWorkingMemory( ) );

        assertEquals( 1, consequence.getInvokedTuples( ).size( ) );

        assertTrue( consequence.getInvokedTuples( ).contains( tuple ) );
    }
}
