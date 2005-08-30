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



import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.drools.rule.Rule;
import org.drools.spi.KnowledgeHelper;
import org.drools.spi.Tuple;
import org.drools.spring.pojorule.Argument;
import org.drools.spring.pojorule.RuleReflectMethod;
import org.easymock.MockControl;
import org.easymock.container.EasymockContainer;

public class RuleReflectMethodTest extends TestCase {

    private EasymockContainer mocks = new EasymockContainer();

    public void testGetParameterValues() throws Exception {
        Argument[] expectedParameterValues = new Argument[] {};

        RuleReflectMethod ruleMethod = new RuleReflectMethod(null, null, null,
                expectedParameterValues);
        Argument[] parameterValues = ruleMethod.getArguments();

        assertSame(expectedParameterValues, parameterValues);
    }

    public void testInvokeMethod() throws Exception {
        class Pojo {
            public void method(KnowledgeHelper knowledgeHelper, String p1, Object a1) {
            }
        }
        Pojo pojo = new Pojo();
        Method pojoMethod = Pojo.class.getMethod("method", new Class[] { KnowledgeHelper.class,
                String.class, Object.class });

        Rule rule = new Rule("test");
        MockControl controlKnowledgeHelperArgument = mocks.createControl(Argument.class);
        Argument mockKnowledgeHelperArgument = (Argument) controlKnowledgeHelperArgument.getMock();
        MockControl controlFactArgument = mocks.createControl(Argument.class);
        Argument mockFactArgument = (Argument) controlFactArgument.getMock();
        MockControl controlApplicationDataArgument = mocks.createControl(Argument.class);
        Argument mockApplicationDataArgument = (Argument) controlApplicationDataArgument.getMock();

        MockControl controlTuple = mocks.createControl(Tuple.class);
        Tuple mockTuple = (Tuple)controlTuple.getMock();
        MockControl controlKnowledgeHelper = mocks.createControl(KnowledgeHelper.class);
        KnowledgeHelper mockKnowledgeHelper = (KnowledgeHelper) controlKnowledgeHelper.getMock();
        String p1 = "p1";
        Object a1 = new Object();

        controlKnowledgeHelperArgument.expectAndReturn(
                mockKnowledgeHelperArgument.getValue(mockTuple),
                mockKnowledgeHelper);
        controlFactArgument.expectAndReturn(
                mockFactArgument.getValue(mockTuple), p1);
        controlApplicationDataArgument.expectAndReturn(
                mockApplicationDataArgument.getValue(mockTuple), a1);

        Argument[] parameterValues = new Argument[] {
                mockKnowledgeHelperArgument, mockFactArgument, mockApplicationDataArgument };

        mocks.replay();

        RuleReflectMethod ruleMethod = new RuleReflectMethod(rule, pojo, pojoMethod,
                parameterValues);
        ruleMethod.invokeMethod(mockTuple);

        mocks.verify();
    }

    private static class SerializablePojo implements Serializable {
        public String methodString;

        public void method(String string) {
            methodString = string;
        }
    }

    private static class TestArgument implements Argument {
        public Object getValue(Tuple tuple) {
            return "VALUE";
        }
    }

    public void testSerialize() throws Exception {
        Rule rule = new Rule("test");
        Argument[] parameterValues = new Argument[] { new TestArgument() };

        SerializablePojo pojo = new SerializablePojo();
        Method pojoMethod = SerializablePojo.class
                .getMethod("method", new Class[] { String.class });
        RuleReflectMethod ruleMethod = new RuleReflectMethod(rule, pojo, pojoMethod,
                parameterValues);

        RuleReflectMethod deserializedRuleMethod = (RuleReflectMethod) fromByteArray(toByteArray(ruleMethod));
        deserializedRuleMethod.invokeMethod(null);

        Field pojoField = deserializedRuleMethod.getClass().getDeclaredField("pojo");
        pojoField.setAccessible(true);
        SerializablePojo deserializedPojo = (SerializablePojo) pojoField
                .get(deserializedRuleMethod);

        assertEquals("VALUE", deserializedPojo.methodString);
    }

    private static byte[] toByteArray(Object serializable) throws IOException {
        ByteArrayOutputStream byteOutputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(byteOutputStream);
        objectOutputStream.writeObject(serializable);
        byte[] bytes = byteOutputStream.toByteArray();
        objectOutputStream.close();
        return bytes;
    }

    private static Object fromByteArray(byte[] bytes) throws IOException, ClassNotFoundException {
        ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes));
        Object object = objectInputStream.readObject();
        objectInputStream.close();
        return object;
    }
}

