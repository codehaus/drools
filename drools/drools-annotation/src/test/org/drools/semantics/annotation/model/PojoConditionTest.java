package org.drools.semantics.annotation.model;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spi.ConditionException;
import org.drools.spi.ObjectType;
import org.drools.spi.Tuple;
import org.easymock.container.EasymockContainer;
import org.easymock.container.EasymockContainer.Mock;

public class PojoConditionTest extends TestCase
{
    private EasymockContainer mocks = new EasymockContainer( );

    private Mock< RuleReflectMethod > newMockRuleMethod( )
    {
        return mocks.createMock( RuleReflectMethod.class, new Class[] { Rule.class, Object.class,
                Method.class, ParameterValue[].class }, new Object[] { null, null, null, null } );
    }

    public void testGetRequiredTupleMembers( ) throws Exception
    {
        Rule rule = new Rule( "test" ); // only for factorying Declarations
        ParameterValue[] parameterValues = new ParameterValue[3];
        Mock< ObjectType > mockObjectType_1 = mocks.createMock( ObjectType.class );
        Mock< ObjectType > mockObjectType_2 = mocks.createMock( ObjectType.class );
        Declaration expectedDeclaration_1 = rule.addParameterDeclaration( "id-1",
                                                                          mockObjectType_1.object );
        Declaration expectedDeclaration_2 = rule.addParameterDeclaration( "id-2",
                                                                          mockObjectType_2.object );

        parameterValues[0] = new ApplicationDataParameterValue( "name", Object.class );
        parameterValues[1] = new TupleParameterValue( expectedDeclaration_1 );
        parameterValues[2] = new TupleParameterValue( expectedDeclaration_2 );

        Mock< RuleReflectMethod > mockRuleMethod = newMockRuleMethod( );
        mockRuleMethod.control.expectAndReturn( mockRuleMethod.object.getParameterValues( ),
                                                parameterValues );

        mocks.replay( );

        PojoCondition pojoCondition = new PojoCondition( mockRuleMethod.object );

        Declaration[] requiredTupleMembers = pojoCondition.getRequiredTupleMembers( );

        mocks.verify( );
        assertEquals( 2, requiredTupleMembers.length );
        assertSame( expectedDeclaration_1, requiredTupleMembers[0] );
        assertSame( expectedDeclaration_2, requiredTupleMembers[1] );
    }

    public void testPojoMethodThrowsException( ) throws Exception
    {
        Mock< RuleReflectMethod > mockRuleMethod = newMockRuleMethod( );
        mockRuleMethod.control.expectAndReturn( mockRuleMethod.object.getParameterValues( ),
                                                new ParameterValue[] {} );
        mockRuleMethod.control.expectAndThrow( mockRuleMethod.object.invokeMethod( null ),
                                               new RuntimeException( "test" ) );

        mocks.replay( );

        PojoCondition pojoCondition = new PojoCondition( mockRuleMethod.object );
        try
        {
            pojoCondition.isAllowed( null );
            fail( "Expected ConditionException" );
        }
        catch (ConditionException e)
        {
            // expected
        }

        mocks.verify( );
    }

    public void testIsAllowed( ) throws Exception
    {
        Mock< Tuple > mockTuple = mocks.createMock( Tuple.class );
        Mock< RuleReflectMethod > mockRuleMethod = newMockRuleMethod( );
        mockRuleMethod.control.expectAndReturn( mockRuleMethod.object.getParameterValues( ),
                                                new ParameterValue[] {} );
        mockRuleMethod.control.expectAndReturn( mockRuleMethod.object
                .invokeMethod( mockTuple.object ), true );

        mocks.replay( );

        PojoCondition pojoCondition = new PojoCondition( mockRuleMethod.object );
        pojoCondition.isAllowed( mockTuple.object );

        mocks.verify( );
    }
}
