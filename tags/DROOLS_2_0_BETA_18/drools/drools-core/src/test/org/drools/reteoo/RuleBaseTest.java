package org.drools.reteoo;

/*
 * $Id: RuleBaseTest.java,v 1.11 2004-11-28 14:44:28 simon Exp $
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

import org.drools.DroolsTestCase;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.conflict.DefaultConflictResolver;
import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.spi.MockObjectType;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.util.List;

public class RuleBaseTest extends DroolsTestCase
{
    public void testSerialize() throws Exception
    {
        Rule rule1 = new Rule( "test-rule 1" );
        rule1.addParameterDeclaration( "paramVar", new MockObjectType( true ) );

        //add consequence
        rule1.setConsequence( new org.drools.spi.InstrumentedConsequence( ) );

        //add conditions
        rule1.addCondition( new org.drools.spi.InstrumentedCondition( ) );
        rule1.addCondition( new org.drools.spi.InstrumentedCondition( ) );

        rule1.setSalience( 42 );

        Rule rule2 = new Rule( "test-rule 2" );

        rule2.addParameterDeclaration( "paramVar", new MockObjectType( true ) );

        //add consequence
        rule2.setConsequence( new org.drools.spi.InstrumentedConsequence( ) );

        //add conditions
        rule2.addCondition( new org.drools.spi.InstrumentedCondition( ) );
        rule2.addCondition( new org.drools.spi.InstrumentedCondition( ) );
        rule2.setSalience( 12 );

        RuleSet ruleSet = new RuleSet( "rule_set" );
        ruleSet.addRule( rule1 );
        ruleSet.addRule( rule2 );

        Builder builder = new Builder( );
        builder.addRuleSet( ruleSet );
        builder.setConflictResolver( DefaultConflictResolver.getInstance( ) );
        RuleBase ruleBase = builder.buildRuleBase( );

        // Serialize to a byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream( );
        ObjectOutput out = new ObjectOutputStream( bos );
        out.writeObject( ruleBase );
        out.close( );

        // Get the bytes of the serialized object
        byte[] bytes = bos.toByteArray( );

        // Deserialize from a byte array
        ObjectInput in = new ObjectInputStream( new ByteArrayInputStream( bytes ) );
        in.readObject( );
        in.close( );

        // JSR-94 Tests
        // Reset the session. [ruleSession.reset();]
        WorkingMemory newWorkingMemory = ruleBase.newWorkingMemory( );
        assertNotNull("WMNotNullTest ", newWorkingMemory);

        // Retrieve all the objects, nothing should be here. The
        // reset should have taken care of the removal.
        // [ruleSession.getObjects();]
        List objects = newWorkingMemory.getObjects( );
        assertNotNull("ValuesNotNullTest ", objects);
        assertEquals("ValuesZeroSizeTest ", 0, objects.size());
    }
}
