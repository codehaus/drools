package org.drools.spring.pojorule;

/*
 * Copyright 2005 (C) The Werken Company. All Rights Reserved.
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



import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spi.ConditionException;
import org.drools.spi.ObjectType;
import org.drools.spi.Tuple;
import org.drools.spring.pojorule.ApplicationDataArgument;
import org.drools.spring.pojorule.Argument;
import org.drools.spring.pojorule.FactArgument;
import org.drools.spring.pojorule.PojoCondition;
import org.drools.spring.pojorule.RuleReflectMethod;
import org.easymock.MockControl;
import org.easymock.container.EasymockContainer;

public class PojoConditionTest extends TestCase {

    private EasymockContainer mocks = new EasymockContainer();

    private MockControl newMockRuleMethod() {
        return mocks.createControl(RuleReflectMethod.class, new Class[] { Rule.class, Object.class,
                Method.class, Argument[].class }, new Object[] { null, null, null, null });
    }

    public void testGetRequiredTupleMembers() throws Exception {
        Rule rule = new Rule("for-test.declaration-factory");
        Argument[] parameterValues = new Argument[3];
        MockControl controlObjectType_1 = mocks.createControl(ObjectType.class);
        MockControl controlObjectType_2 = mocks.createControl(ObjectType.class);
        Declaration expectedDeclaration_1 = rule.addParameterDeclaration(
                "id-1", (ObjectType) controlObjectType_1.getMock());
        Declaration expectedDeclaration_2 = rule.addParameterDeclaration(
                "id-2", (ObjectType) controlObjectType_2.getMock());

        parameterValues[0] = new ApplicationDataArgument("name", Object.class);
        parameterValues[1] = new FactArgument(expectedDeclaration_1);
        parameterValues[2] = new FactArgument(expectedDeclaration_2);

        MockControl controlRuleMethod = newMockRuleMethod();
        RuleReflectMethod mockRuleMethod = (RuleReflectMethod) controlRuleMethod.getMock();
        controlRuleMethod.expectAndReturn(mockRuleMethod.getArguments(), parameterValues);

        mocks.replay();

        PojoCondition pojoCondition = new PojoCondition(mockRuleMethod);

        Declaration[] requiredTupleMembers = pojoCondition.getRequiredTupleMembers();

        mocks.verify();
        assertEquals(2, requiredTupleMembers.length);
        assertSame(expectedDeclaration_1, requiredTupleMembers[0]);
        assertSame(expectedDeclaration_2, requiredTupleMembers[1]);
    }

    public void testPojoMethodThrowsException() throws Exception {
        MockControl controlRuleMethod = newMockRuleMethod();
        RuleReflectMethod mockRuleMethod = (RuleReflectMethod) controlRuleMethod.getMock();
        controlRuleMethod.expectAndReturn(mockRuleMethod.getArguments(), new Argument[] {});
        controlRuleMethod.expectAndThrow(mockRuleMethod.invokeMethod(null), new RuntimeException("test"));

        mocks.replay();

        PojoCondition pojoCondition = new PojoCondition(mockRuleMethod);
        try {
            pojoCondition.isAllowed(null);
            fail("Expected ConditionException");
        } catch (ConditionException e) {
            // expected
        }

        mocks.verify();
    }

    public void testIsAllowed() throws Exception {
        MockControl controlTuple = mocks.createControl(Tuple.class);
        Tuple mockTuple = (Tuple)controlTuple.getMock();
        MockControl controlRuleMethod = newMockRuleMethod();
        RuleReflectMethod mockRuleMethod = (RuleReflectMethod) controlRuleMethod.getMock();
        controlRuleMethod.expectAndReturn(mockRuleMethod.getArguments(), new Argument[] {});
        controlRuleMethod.expectAndReturn(mockRuleMethod.invokeMethod(mockTuple), Boolean.TRUE);

        mocks.replay();

        PojoCondition pojoCondition = new PojoCondition(mockRuleMethod);
        pojoCondition.isAllowed(mockTuple);

        mocks.verify();
    }
}

