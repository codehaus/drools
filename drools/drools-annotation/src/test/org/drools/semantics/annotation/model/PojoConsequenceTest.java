package org.drools.semantics.annotation.model;

import java.lang.reflect.Method;

import org.drools.WorkingMemory;
import org.drools.rule.Rule;
import org.drools.semantics.annotation.model.PojoConsequence;
import org.drools.spi.ConsequenceException;
import org.drools.spi.Tuple;
import org.easymock.container.EasymockContainer;
import org.easymock.container.EasymockContainer.Mock;

import junit.framework.TestCase;

public class PojoConsequenceTest extends TestCase
{
    private EasymockContainer mocks = new EasymockContainer( );

    private Mock< RuleReflectMethod > newMockRuleMethod( )
    {
        return mocks.createMock( RuleReflectMethod.class, new Class[] { Rule.class, Object.class,
                Method.class, ParameterValue[].class }, new Object[] { null, null, null, null } );
    }

    public void testPojoMethodThrowsException( ) throws Exception
    {
        Mock< Tuple > mockTuple = mocks.createMock( Tuple.class );
        Mock< WorkingMemory > mockWorkingMemory = mocks.createMock( WorkingMemory.class );
        Mock< RuleReflectMethod > mockRuleMethod = newMockRuleMethod( );
        mockRuleMethod.control.expectAndThrow( mockRuleMethod.object
                .invokeMethod( mockTuple.object ), new RuntimeException( "test" ) );

        mocks.replay( );

        PojoConsequence pojoConsequence = new PojoConsequence( mockRuleMethod.object );
        try
        {
            pojoConsequence.invoke( mockTuple.object, mockWorkingMemory.object );
            fail( "Expected ConsequenceException" );
        }
        catch (ConsequenceException e)
        {
            // expected
        }

        mocks.verify( );
    }

    public void testInvoke( ) throws Exception
    {
        Mock< Tuple > mockTuple = mocks.createMock( Tuple.class );
        Mock< WorkingMemory > mockWorkingMemory = mocks.createMock( WorkingMemory.class );
        Mock< RuleReflectMethod > mockRuleMethod = newMockRuleMethod( );
        mockRuleMethod.control.expectAndReturn( mockRuleMethod.object
                .invokeMethod( mockTuple.object ), null );

        mocks.replay( );

        PojoConsequence pojoConsequence = new PojoConsequence( mockRuleMethod.object );
        pojoConsequence.invoke( mockTuple.object, mockWorkingMemory.object );

        mocks.verify( );
    }

}
