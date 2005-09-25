package org.drools.reteoo;

/*
 * $Id: ConditionNodeTest.java,v 1.19 2005-05-08 04:05:13 dbarnett Exp $
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

import java.util.List;
import java.util.Set;

import junit.framework.TestCase;

import org.drools.AssertionException;
import org.drools.RuleBase;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spi.FalseCondition;
import org.drools.spi.MockObjectType;
import org.drools.spi.TrueCondition;

public class ConditionNodeTest extends TestCase
{
    private RuleBase    ruleBase;
    private Rule        rule;
    private ReteTuple   tuple;

    public ConditionNodeTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
        this.ruleBase = new RuleBaseImpl( new Rete( ) );
        this.rule = new Rule( "test-rule 1" );

        //add consequence
        this.rule.setConsequence( new org.drools.spi.InstrumentedConsequence( ) );

        //add condition
        rule.addCondition( new org.drools.spi.InstrumentedCondition( ) );
        this.tuple = new ReteTuple( (WorkingMemoryImpl) ruleBase.newWorkingMemory( ) );
    }

    /**
     * If a condition allows an incoming Object, then the Object MUST be
     * propagated.
     */
    public void testAllowed()
    {
        ConditionNode node = new ConditionNode( rule, null, new TrueCondition( ) );

        InstrumentedTupleSink sink = new InstrumentedTupleSink( );

        node.addTupleSink( sink );

        try
        {
            RuleBase ruleBase = new RuleBaseImpl( new Rete( ) );
            node.assertTuple( this.tuple, ( WorkingMemoryImpl ) ruleBase.newWorkingMemory( ) );

            List asserted = sink.getAssertedTuples( );

            assertEquals( 1, asserted.size( ) );

            ReteTuple tuple = ( ReteTuple ) asserted.get( 0 );

            assertSame( this.tuple, tuple );
        }
        catch ( AssertionException e )
        {
            fail( e.toString( ) );
        }
    }

    /**
     * If a Condition does not allow an incoming Object, then the object MUST
     * NOT be propagated.
     */
    public void testNotAllowed()
    {
        ConditionNode node = new ConditionNode( rule, null, new FalseCondition( ) );

        InstrumentedTupleSink sink = new InstrumentedTupleSink( );

        node.addTupleSink( sink );

        try
        {
            node.assertTuple( this.tuple, ( WorkingMemoryImpl ) this.ruleBase.newWorkingMemory( ) );

            List asserted = sink.getAssertedTuples( );

            assertEquals( 0, asserted.size( ) );
        }
        catch ( AssertionException e )
        {
            fail( e.toString( ) );
        }
    }

    /**
     * A FilterNode MUST delegate to its input source for getTupleDeclarations()
     * since it does not alter the structure of the Tuples.
     */
    public void testGetTupleDeclarations() throws Exception
    {
        Declaration decl = this.rule.addParameterDeclaration( "object", new MockObjectType( Object.class ) );

        ParameterNode paramNode = new ParameterNode( null, decl );

        ConditionNode condNode = new ConditionNode( rule, paramNode, null );

        Set decls = condNode.getTupleDeclarations( );

        assertEquals( 1, decls.size( ) );

        assertTrue( decls.contains( decl ) );
    }
}
