
package org.drools.semantic.java;

import org.drools.rule.Declaration;
import org.drools.spi.FilterException;
import org.drools.spi.Tuple;
import org.drools.reteoo.impl.ReteTuple;

import junit.framework.TestCase;

import java.util.Collections;

public class BeanShellFilterConditionTest extends TestCase
{
    private Declaration objectDecl;
    private Declaration stringDecl;

    private ReteTuple   tuple;

    private Object      object;
    private String      string;

    public BeanShellFilterConditionTest(String name)
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
        BeanShellFilterCondition cond = new BeanShellFilterCondition( "string.length() > 5",
                                                                      Collections.EMPTY_SET );

        try
        {
            boolean result = cond.isAllowed( this.tuple );

            assertTrue( result );
        }
        catch (FilterException e)
        {
            fail( e.toString() );
        }
    }

    public void testIsAllowedFails()
    {
        BeanShellFilterCondition cond = new BeanShellFilterCondition( "string.length() > 50",
                                                                      Collections.EMPTY_SET );

        try
        {
            boolean result = cond.isAllowed( this.tuple );

            assertTrue( ! result );
        }
        catch (FilterException e)
        {
            fail( e.toString() );
        }
    }

    public void testIsAllowedBadReference()
    {
        BeanShellFilterCondition cond = new BeanShellFilterCondition( "cheese.length()",
                                                                      Collections.EMPTY_SET );

        try
        {
            boolean result = cond.isAllowed( this.tuple );

            fail( "Should have thrown FilterException" );
        }
        catch (FilterException e)
        {
            // expected and correct
        }
    }

    public void testIsAllowedNonBoolean()
    {
        BeanShellFilterCondition cond = new BeanShellFilterCondition( "string.length()",
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
        catch (FilterException e)
        {
            fail( e.toString() );
        }
    }
}

