package org.drools.semantics.annotation.model;

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
import org.easymock.container.EasymockContainer;
import org.easymock.container.EasymockContainer.Mock;

public class RuleReflectMethodTest extends TestCase
{
    private EasymockContainer mocks = new EasymockContainer( );

    public void testGetParameterValues( ) throws Exception
    {
        ArgumentSource[] expectedParameterValues = new ArgumentSource[] {};

        RuleReflectMethod ruleMethod = new RuleReflectMethod( null, null, null,
                expectedParameterValues );
        ArgumentSource[] parameterValues = ruleMethod.getArguments( );

        assertSame( expectedParameterValues, parameterValues );
    }

    public void testInvokeMethod( ) throws Exception
    {
        class Pojo
        {
            public void method( KnowledgeHelper knowledgeHelper, String p1, Object a1 )
            {}
        }
        Pojo pojo = new Pojo( );
        Method pojoMethod = Pojo.class.getMethod( "method", new Class[] { KnowledgeHelper.class,
                String.class, Object.class } );

        Rule rule = new Rule( "test" );
        Mock< ArgumentSource > mockKnowledgeHelperParameterValue = mocks.createMock( ArgumentSource.class );
        Mock< ArgumentSource > mockTupleParameterValue = mocks.createMock( ArgumentSource.class );
        Mock< ArgumentSource > mockApplicationDataParameterValue = mocks
                .createMock( ArgumentSource.class );

        Mock< Tuple > mockTuple = mocks.createMock( Tuple.class );
        Mock< KnowledgeHelper > mockKnowledgeHelperDrools = mocks.createMock( KnowledgeHelper.class );
        String p1 = "p1";
        Object a1 = new Object( );

        mockKnowledgeHelperParameterValue.control.expectAndReturn(
                mockKnowledgeHelperParameterValue.object.getValue( mockTuple.object ),
                mockKnowledgeHelperDrools.object );
        mockTupleParameterValue.control.expectAndReturn(
                mockTupleParameterValue.object.getValue( mockTuple.object ),
                p1 );
        mockApplicationDataParameterValue.control.expectAndReturn(
                mockApplicationDataParameterValue.object.getValue( mockTuple.object ),
                a1 );

        ArgumentSource[] parameterValues = new ArgumentSource[] {
                mockKnowledgeHelperParameterValue.object,
                mockTupleParameterValue.object,
                mockApplicationDataParameterValue.object };

        mocks.replay( );

        RuleReflectMethod ruleMethod =
                new RuleReflectMethod( rule, pojo, pojoMethod, parameterValues );
        ruleMethod.invokeMethod( mockTuple.object );

        mocks.verify( );
    }


    private static class SerializablePojo implements Serializable
    {
        public String methodString;

        public void method( String string )
        {
            methodString = string;
        }
    }

    private static class TestArgumentSource implements ArgumentSource {
        public Object getValue(Tuple tuple)
        {
            return "VALUE";
        }
    }

    public void testSerialize() throws Exception {
        Rule rule = new Rule( "test" );
        ArgumentSource[] parameterValues = new ArgumentSource[] {
                 new TestArgumentSource()};

        SerializablePojo pojo = new SerializablePojo( );
        Method pojoMethod = SerializablePojo.class.getMethod(
                "method", new Class[] { String.class } );
        RuleReflectMethod ruleMethod = new RuleReflectMethod( rule, pojo, pojoMethod, parameterValues );


        RuleReflectMethod deserializedRuleMethod
                = (RuleReflectMethod) fromByteArray(toByteArray(ruleMethod));
        deserializedRuleMethod.invokeMethod( null );

        Field pojoField = deserializedRuleMethod.getClass().getDeclaredField("pojo");
        pojoField.setAccessible(true);
        SerializablePojo deserializedPojo = (SerializablePojo) pojoField.get(deserializedRuleMethod);

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
