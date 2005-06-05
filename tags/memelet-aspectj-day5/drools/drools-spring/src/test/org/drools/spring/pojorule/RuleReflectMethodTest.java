package org.drools.spring.pojorule;

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
