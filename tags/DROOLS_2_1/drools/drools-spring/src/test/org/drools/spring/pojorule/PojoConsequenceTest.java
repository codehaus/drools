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

import org.drools.rule.Rule;
import org.drools.spi.ConsequenceException;
import org.drools.spi.Tuple;
import org.drools.spring.pojorule.Argument;
import org.drools.spring.pojorule.PojoConsequence;
import org.drools.spring.pojorule.RuleReflectMethod;
import org.easymock.MockControl;
import org.easymock.container.EasymockContainer;

import junit.framework.TestCase;

public class PojoConsequenceTest extends TestCase {

    private EasymockContainer mocks = new EasymockContainer( );

    private MockControl newMockRuleMethod() {
        return mocks.createControl(
                RuleReflectMethod.class,
                new Class[] {Rule.class, Object.class, Method.class, Argument[].class},
                new Object[] {null, null, null, null});
    }

    public void testPojoMethodThrowsException() throws Exception {
        MockControl controlTuple = mocks.createControl(Tuple.class);
        Tuple mockTuple = (Tuple)controlTuple.getMock();
        MockControl controlRuleMethod = newMockRuleMethod();
        RuleReflectMethod mockRuleMethod = (RuleReflectMethod) controlRuleMethod.getMock();
        controlRuleMethod.expectAndThrow(mockRuleMethod.invokeMethod(mockTuple),
                new RuntimeException("test-exception"));
        RuleReflectMethod[] mockRuleMethods = new RuleReflectMethod[]{mockRuleMethod};

        mocks.replay();

        PojoConsequence pojoConsequence = new PojoConsequence(mockRuleMethods);
        try {
            pojoConsequence.invoke(mockTuple);
            fail( "Expected ConsequenceException" );
        }
        catch (ConsequenceException e) {
            // expected
        }

        mocks.verify();
    }

    public void testInvoke() throws Exception {
        MockControl controlTuple = mocks.createControl(Tuple.class);
        Tuple mockTuple = (Tuple)controlTuple.getMock();
        MockControl controlRuleMethod = newMockRuleMethod();
        RuleReflectMethod mockRuleMethod = (RuleReflectMethod) controlRuleMethod.getMock();
        controlRuleMethod.expectAndReturn(mockRuleMethod.invokeMethod(mockTuple), null);
        RuleReflectMethod[] mockRuleMethods = new RuleReflectMethod[]{mockRuleMethod};

        mocks.replay();

        PojoConsequence pojoConsequence = new PojoConsequence(mockRuleMethods);
        pojoConsequence.invoke(mockTuple);

        mocks.verify();
    }

    public void testInvokeMulti() throws Exception {
        MockControl controlTuple = mocks.createControl(Tuple.class);
        Tuple mockTuple = (Tuple)controlTuple.getMock();
        MockControl controlRuleMethod_1 = newMockRuleMethod();
        RuleReflectMethod mockRuleMethod_1 = (RuleReflectMethod) controlRuleMethod_1.getMock();
        controlRuleMethod_1.expectAndReturn(mockRuleMethod_1.invokeMethod(mockTuple), null);
        MockControl controlRuleMethod_2 = newMockRuleMethod();
        RuleReflectMethod mockRuleMethod_2 = (RuleReflectMethod) controlRuleMethod_2.getMock();
        controlRuleMethod_2.expectAndReturn(mockRuleMethod_2.invokeMethod(mockTuple), null);
        RuleReflectMethod[] mockRuleMethods = new RuleReflectMethod[]{mockRuleMethod_1, mockRuleMethod_2};

        mocks.replay();

        PojoConsequence pojoConsequence = new PojoConsequence(mockRuleMethods);
        pojoConsequence.invoke(mockTuple);

        mocks.verify();
    }
}

