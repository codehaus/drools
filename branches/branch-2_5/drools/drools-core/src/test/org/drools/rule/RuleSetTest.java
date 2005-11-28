package org.drools.rule;

/*
 * $Id: RuleSetTest.java,v 1.15 2005-05-08 04:05:13 dbarnett Exp $
 *
 * Copyright 2002-2005 (C) The Werken Company. All Rights Reserved.
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

import org.drools.DroolsTestCase;
import org.drools.spi.MockObjectType;

public class RuleSetTest extends DroolsTestCase
{
    private RuleSet ruleSet;

    public RuleSetTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
        this.ruleSet = new RuleSet( "rule_set" );
    }

    public void tearDown()
    {
        this.ruleSet = null;
    }

    public void testBasics()
    {
        assertEquals( "rule_set",
                      this.ruleSet.getName( ) );
    }

    public void testDocumentation()
    {
        assertNull( this.ruleSet.getDocumentation( ) );

        this.ruleSet.setDocumentation( "the cheesiest!" );

        assertEquals( "the cheesiest!",
                      this.ruleSet.getDocumentation( ) );
    }

    /**
     * A RuleSet MUST accept any Rule that does not have a conflicting name.
     */
    public void testAddRule() throws Exception
    {
        InstrumentedRule rule;

        rule = new InstrumentedRule( "cheese" );
        rule.isValid( true );
        this.ruleSet.addRule( rule );
        assertEquals( 0,
                      rule.getLoadOrder( ) );

        rule = new InstrumentedRule( "meat" );
        rule.isValid( true );
        this.ruleSet.addRule( rule );
        assertEquals( 1,
                      rule.getLoadOrder( ) );

        rule = new InstrumentedRule( "vegetables" );
        rule.isValid( true );
        this.ruleSet.addRule( rule );
        assertEquals( 2,
                      rule.getLoadOrder( ) );
    }

    public void testGetRule() throws Exception
    {
        assertNull( this.ruleSet.getRule( "cheese" ) );

        InstrumentedRule rule = new InstrumentedRule( "cheese" );

        rule.isValid( true );

        this.ruleSet.addRule( rule );

        assertSame( rule,
                    this.ruleSet.getRule( "cheese" ) );

        assertNull( this.ruleSet.getRule( "betty" ) );
    }

    /**
     * A RuleSet MUST throw a DuplicateRuleNameException if an attempt to add a
     * Rule whose name conflicts with an already added Rule.
     */
    public void testAddRuleDuplicate() throws Exception
    {
        InstrumentedRule rule1 = new InstrumentedRule( "cheese" );
        InstrumentedRule rule2 = new InstrumentedRule( "cheese" );

        rule1.isValid( true );
        rule2.isValid( true );

        this.ruleSet.addRule( rule1 );

        try
        {
            this.ruleSet.addRule( rule2 );

            fail( "Should have thrown DuplicateRuleNameException" );
        }
        catch ( DuplicateRuleNameException e )
        {
            assertSame( this.ruleSet,
                        e.getRuleSet( ) );

            assertSame( rule1,
                        e.getOriginalRule( ) );

            assertSame( rule2,
                        e.getConflictingRule( ) );
        }
    }

    public void testSerializeRuleSet() throws Exception
    {
        Rule rule1 = new Rule( "test-rule 1" );

        rule1.addParameterDeclaration( "paramVar",
                                       new MockObjectType( true ) );

        // add consequence
        rule1.setConsequence( new org.drools.spi.InstrumentedConsequence( ) );

        // add conditions
        rule1.addCondition( new org.drools.spi.InstrumentedCondition( ) );
        rule1.addCondition( new org.drools.spi.InstrumentedCondition( ) );

        rule1.setSalience( 42 );
        rule1.setLoadOrder( 22 );

        Rule rule2 = new Rule( "test-rule 2" );

        rule2.addParameterDeclaration( "paramVar",
                                       new MockObjectType( true ) );

        // add consequence
        rule2.setConsequence( new org.drools.spi.InstrumentedConsequence( ) );

        // add conditions
        rule2.addCondition( new org.drools.spi.InstrumentedCondition( ) );
        rule2.addCondition( new org.drools.spi.InstrumentedCondition( ) );
        rule2.setSalience( 12 );
        rule2.setLoadOrder( 2 );

        RuleSet ruleSet = new RuleSet( "rule_set" );
        ruleSet.addRule( rule1 );
        ruleSet.addRule( rule2 );

        // Serialize to a byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream( );
        ObjectOutput out = new ObjectOutputStream( bos );
        out.writeObject( ruleSet );
        out.close( );

        // Get the bytes of the serialized object
        byte[] bytes = bos.toByteArray( );

        // Deserialize from a byte array
        ObjectInput in = new ObjectInputStream( new ByteArrayInputStream( bytes ) );
        ruleSet = (RuleSet) in.readObject( );
        in.close( );

        assertLength( 2,
                      ruleSet.getRules( ) );
    }
}
