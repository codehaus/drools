package org.drools.semantics.python;

import org.drools.MockObjectType;

import org.drools.rule.Declaration;
import org.drools.spi.MockTuple;
import org.drools.spi.ConditionException;

import org.python.core.PyException;

import junit.framework.TestCase;

public class ExprConditionTest extends TestCase
{
    public ExprConditionTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
    }

    public void tearDown()
    {
    }

    public void testIsAllowed_NoText()
    {
        ExprCondition condition = new ExprCondition();

        MockTuple tuple = new MockTuple();

        try
        {
            condition.isAllowed( tuple );
        }
        catch (ConditionException e)
        {
            // expected and correct
            PyException pe = (PyException) e.getRootCause();
        }
    }

    public void testIsAllowed_MissingObject()
    {
        ExprCondition condition = new ExprCondition();

        condition.setExpression( "a == 42" );

        MockTuple tuple = new MockTuple();

        try
        {
            condition.isAllowed( tuple );
        }
        catch (ConditionException e)
        {
            // expected and correct
            PyException pe = (PyException) e.getRootCause();
        }
    }

    public void testIsAllowed_ValidTrue() throws Exception
    {
        ExprCondition condition = new ExprCondition();

        condition.setExpression( "a == 42" );

        MockTuple tuple = new MockTuple();

        tuple.put( new Declaration( new MockObjectType( java.lang.Integer.class ),
                                    "a" ),
                   new Integer( 42 ) );

        assertTrue( condition.isAllowed( tuple ) );
    }

    public void testIsAllowed_ValidFalse() throws Exception
    {
        ExprCondition condition = new ExprCondition();

        condition.setExpression( "a == 42" );

        MockTuple tuple = new MockTuple();

        tuple.put( new Declaration( new MockObjectType( java.lang.Integer.class ),
                                    "a" ),
                   new Integer( 44 ) );

        assertTrue( ! condition.isAllowed( tuple ) );
    }
}

