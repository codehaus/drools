
package org.drools.semantic.java;

import org.drools.rule.Declaration;
import org.drools.spi.ConditionException;
import org.drools.spi.Tuple;
import org.drools.reteoo.impl.ReteTuple;

import junit.framework.TestCase;

import java.util.Collections;

public class BeanShellExprConditionTest extends TestCase
{
    private Declaration objectDecl;
    private Declaration stringDecl;

    private ReteTuple   tuple;

    private Object      object;
    private String      string;

    public BeanShellExprConditionTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
        this.objectDecl = new Declaration( new JavaObjectType( Object.class ),
                                           "object" );
        
        this.stringDecl = new Declaration( new JavaObjectType( String.class ),
                                           "string" );

        this.object = new Object();
        this.string = "I like cheese";

        this.tuple = new ReteTuple();

        this.tuple.putOtherColumn( this.objectDecl,
                                   this.object );

        this.tuple.putOtherColumn( this.stringDecl,
                                   this.string );
    }

    public void tearDown()
    {
    }

    public void testIsAllowedPasses()
    {
        BeanShellExprCondition cond = new BeanShellExprCondition( "string.length() > 5",
                                                                  Collections.EMPTY_SET );

        try
        {
            boolean result = cond.isAllowed( this.tuple );

            assertTrue( result );
        }
        catch (ConditionException e)
        {
            fail( e.toString() );
        }
    }

    public void testIsAllowedFails()
    {
        BeanShellExprCondition cond = new BeanShellExprCondition( "string.length() > 50",
                                                                  Collections.EMPTY_SET );

        try
        {
            boolean result = cond.isAllowed( this.tuple );

            assertTrue( ! result );
        }
        catch (ConditionException e)
        {
            fail( e.toString() );
        }
    }

    public void testIsAllowedBadReference()
    {
        BeanShellExprCondition cond = new BeanShellExprCondition( "cheese.length()",
                                                                  Collections.EMPTY_SET );

        try
        {
            boolean result = cond.isAllowed( this.tuple );

            fail( "Should have thrown FilterException" );
        }
        catch (ConditionException e)
        {
            // expected and correct
        }
    }

    public void testIsAllowedNonBoolean()
    {
        BeanShellExprCondition cond = new BeanShellExprCondition( "string.length()",
                                                                  Collections.EMPTY_SET );

        try
        {
            boolean result = cond.isAllowed( this.tuple );

            fail( "Should have thrown NonBooleanExpressionException" );
        }
        catch (NonBooleanExpressionException e)
        {
            // expected and correct
        }
        catch (ConditionException e)
        {
            fail( e.toString() );
        }
    }
}

