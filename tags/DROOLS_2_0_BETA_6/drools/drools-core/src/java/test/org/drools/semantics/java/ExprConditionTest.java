package org.drools.semantics.java;

import org.drools.rule.Declaration;
import org.drools.spi.MockTuple;
import org.drools.spi.ConditionException;

import bsh.EvalError;

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
            NullPointerException npe = (NullPointerException) e.getRootCause();
        }
    }

    public void testIsAllowed_MissingObject()
    {
        ExprCondition condition = new ExprCondition();

        condition.setExpression( "a" );

        MockTuple tuple = new MockTuple();

        try
        {
            condition.isAllowed( tuple );
        }
        catch (ConditionException e)
        {
            // expected and correct
            EvalError ee = (EvalError) e.getRootCause();
        }
    }

    public void testIsAllowed_ValidTrue() throws Exception
    {
        ExprCondition condition = new ExprCondition();

        condition.setExpression( "a" );

        MockTuple tuple = new MockTuple();

        tuple.put( new Declaration( new ClassObjectType( java.lang.Boolean.class ),
                                    "a" ),
                   Boolean.TRUE );

        assertTrue( condition.isAllowed( tuple ) );
    }

    public void testIsAllowed_ValidFalse() throws Exception
    {
        ExprCondition condition = new ExprCondition();

        condition.setExpression( "a" );

        MockTuple tuple = new MockTuple();

        tuple.put( new Declaration( new ClassObjectType( java.lang.Boolean.class ),
                                    "a" ),
                   Boolean.FALSE );

        assertTrue( ! condition.isAllowed( tuple ) );
    }
}

