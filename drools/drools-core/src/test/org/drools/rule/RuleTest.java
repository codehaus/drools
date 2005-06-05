package org.drools.rule;

/*
 * $Id: RuleTest.java,v 1.23.2.1 2005-06-02 23:25:31 memelet Exp $
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
import java.util.List;

import org.drools.DroolsTestCase;
import org.drools.spi.Consequence;
import org.drools.spi.MockObjectType;
import org.drools.spi.Tuple;

public class RuleTest extends DroolsTestCase
{
    public void testConstruct()
    {
        Rule rule = new Rule( "test-rule" );

        assertTrue( !rule.isValid( ) );

        try
        {
            rule.checkValidity( );

            fail( "Should have thrown InvalidRuleException" );
        }
        catch ( InvalidRuleException e )
        {
            // expected and correct
        }

        assertLength( 0,
                      rule.getParameterDeclarations( ) );

        assertLength( 0,
                      rule.getConditions( ) );

        assertNull( rule.getConsequence( ) );
    }

    public void testParameterDeclarations() throws Exception
    {
        Rule rule = new Rule( "test-rule" );

        Declaration paramDecl = rule.addParameterDeclaration( "paramVar",
                                                              new MockObjectType( true ) );

        List paramDecls = rule.getParameterDeclarations( );

        assertLength( 1,
                      paramDecls );

        assertContains( paramDecl,
                        paramDecls );

        assertSame( paramDecl,
                    rule.getParameterDeclaration( "paramVar" ) );

        assertNull( rule.getParameterDeclaration( "betty" ) );
    }

    public void testLocalDeclarations() throws Exception
    {
        Rule rule = new Rule( "test-rule" );

        Declaration paramDecl = rule.addParameterDeclaration( "paramVar",
                                                              new MockObjectType( true ) );

        List paramDecls = rule.getParameterDeclarations( );
        assertLength( 1,
                      paramDecls );

        assertContains( paramDecl,
                        paramDecls );

        assertNull( rule.getParameterDeclaration( "betty" ) );
    }

    public void testDocumentation() throws Exception
    {
        Rule rule = new Rule( "test-rule" );

        assertNull( rule.getDocumentation( ) );

        rule.setDocumentation( "the cheesiest!" );

        assertEquals( "the cheesiest!",
                      rule.getDocumentation( ) );
    }

    public void testSalience() throws Exception
    {
        Rule rule = new Rule( "test-rule" );

        assertEquals( 0,
                      rule.getSalience( ) );

        rule.setSalience( 42 );

        assertEquals( 42,
                      rule.getSalience( ) );
    }

    public void testLoadOrder() throws Exception
    {
        Rule rule = new Rule( "test-rule" );

        assertEquals( 0,
                      rule.getLoadOrder( ) );

        rule.setLoadOrder( 42 );

        assertEquals( 42,
                      rule.getLoadOrder( ) );
    }

    public void testDeclarationOrder() throws Exception
    {
        Rule rule = new Rule( "test-rule" );

        assertEquals( 0,
                      rule.addParameterDeclaration( "paramVar0",
                                                    new MockObjectType( true ) ).getIndex( ) );
        assertEquals( 1,
                      rule.addParameterDeclaration( "paramVar1",
                                                    new MockObjectType( true ) ).getIndex( ) );
    }

//duration
//    public void testDuration_SimpleLong() throws Exception
//    {
//        Rule rule = new Rule( "test-rule" );
//
//        rule.setDuration( 42L );
//
//        Duration dur = rule.getDuration( );
//
//        assertNotNull( dur );
//
//        assertTrue( dur instanceof FixedDuration );
//
//        assertEquals( 42L,
//                      rule.getDuration( ).getDuration( null ) );
//    }
//
//    public void testDuration_WithObject() throws Exception
//    {
//        Duration dur = new FixedDuration( 42 );
//
//        Rule rule = new Rule( "test-rule" );
//
//        rule.setDuration( dur );
//
//        assertSame( dur,
//                    rule.getDuration( ) );
//    }

    public void testConsequence() throws Exception
    {
        Rule rule = new Rule( "test-rule" );

        assertNull( rule.getConsequence( ) );

        Consequence consequence = new Consequence( )
        {
            public void invoke(Tuple tuple)
            {
                // nothing;
            }
        };

        rule.setConsequence( consequence );

        assertSame( consequence,
                    rule.getConsequence( ) );
    }

    public void testSerializeRuleSet() throws Exception
    {

        Rule rule = new Rule( "test-rule" );

        rule.addParameterDeclaration( "paramVar",
                                      new MockObjectType( true ) );

        // add consequence
        rule.setConsequence( new org.drools.spi.InstrumentedConsequence( ) );

        // add conditions
        rule.addCondition( new org.drools.spi.InstrumentedCondition( ) );
        rule.addCondition( new org.drools.spi.InstrumentedCondition( ) );

        rule.setSalience( 42 );
        rule.setLoadOrder( 22 );

        // Serialize to a byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream( );
        ObjectOutput out = new ObjectOutputStream( bos );
        out.writeObject( rule );
        out.close( );

        // Get the bytes of the serialized object
        byte[] bytes = bos.toByteArray( );

        // Deserialize from a byte array
        ObjectInput in = new ObjectInputStream( new ByteArrayInputStream( bytes ) );
        rule = (Rule) in.readObject( );
        in.close( );

        assertEquals( 42,
                      rule.getSalience( ) );
        assertEquals( 22,
                      rule.getLoadOrder( ) );
        assertLength( 1,
                      rule.getParameterDeclarations( ) );
        assertLength( 2,
                      rule.getConditions( ) );
    }
}
