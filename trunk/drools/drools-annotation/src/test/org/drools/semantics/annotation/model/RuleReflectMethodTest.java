package org.drools.semantics.annotation.model;

import java.lang.reflect.Method;

import org.drools.rule.Rule;
import org.drools.semantics.annotation.Drools;
import org.drools.spi.Tuple;
import org.easymock.container.EasymockContainer;
import org.easymock.container.EasymockContainer.Mock;

import junit.framework.TestCase;

public class RuleReflectMethodTest extends TestCase
{
    private EasymockContainer mocks = new EasymockContainer( );

    public void testGetParameterValues( ) throws Exception
    {
        ParameterValue[] expectedParameterValues = new ParameterValue[] {};

        RuleReflectMethod ruleMethod = new RuleReflectMethod( null, null, null,
                expectedParameterValues );
        ParameterValue[] parameterValues = ruleMethod.getParameterValues( );

        assertSame( expectedParameterValues, parameterValues );
    }

    public void testInvokeMethod( ) throws Exception
    {
        class Pojo
        {
            public void method( Drools drools, String p1, Object a1 )
            {}
        }
        Pojo pojo = new Pojo( );
        Method pojoMethod = Pojo.class.getMethod( "method", new Class[] { Drools.class,
                String.class, Object.class } );

        Rule rule = new Rule( "test" );
        Mock< ParameterValue > mockDroolsParameterValue = mocks.createMock( ParameterValue.class );
        Mock< ParameterValue > mockTupleParameterValue = mocks.createMock( ParameterValue.class );
        Mock< ParameterValue > mockApplicationDataParameterValue = mocks
                .createMock( ParameterValue.class );

        Mock< Tuple > mockTuple = mocks.createMock( Tuple.class );
        Mock< Drools > mockDrools = mocks.createMock( Drools.class );
        String p1 = "p1";
        Object a1 = new Object( );

        mockDroolsParameterValue.control.expectAndReturn( mockDroolsParameterValue.object
                .getValue( mockTuple.object ), mockDrools.object );
        mockTupleParameterValue.control.expectAndReturn( mockTupleParameterValue.object
                .getValue( mockTuple.object ), p1 );
        mockApplicationDataParameterValue.control
                .expectAndReturn( mockApplicationDataParameterValue.object
                        .getValue( mockTuple.object ), a1 );

        ParameterValue[] parameterValues = new ParameterValue[] { mockDroolsParameterValue.object,
                mockTupleParameterValue.object, mockApplicationDataParameterValue.object };

        mocks.replay( );

        RuleReflectMethod ruleMethod = new RuleReflectMethod( rule, pojo, pojoMethod,
                parameterValues );
        ruleMethod.invokeMethod( mockTuple.object );

        mocks.verify( );

    }
}
