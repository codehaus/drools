
package org.drools.semantic.java;

import org.drools.reteoo.ReteTuple;

import org.drools.spi.Declaration;
import org.drools.spi.Tuple;
import org.drools.spi.ActionInvokationException;

import junit.framework.TestCase;

public class BeanShellActionTest extends TestCase
{
    public static boolean STATUS;
    public static Object  RETURN_1;
    public static Object  RETURN_2;

    private Declaration objectDecl;
    private Declaration stringDecl;

    private Tuple       tuple;

    private Object      object;
    private String      string;

    public BeanShellActionTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
        STATUS   = false;
        RETURN_1 = null;
        RETURN_2 = null;

        this.objectDecl = new Declaration( new JavaObjectType( Object.class ),
                                           "object" );
        
        this.stringDecl = new Declaration( new JavaObjectType( String.class ),
                                           "string" );

        this.object = new Object();
        this.string = "I like cheese";

        this.tuple = new ReteTuple();

        this.tuple.put( this.objectDecl,
                        this.object );

        this.tuple.put( this.stringDecl,
                        this.string );
    }

    public void tearDown()
    {
        STATUS   = false;
        RETURN_1 = null;
        RETURN_2 = null;
    }

    public void testInvoke()
    {
        try
        {
            STATUS = false;

            BeanShellAction action = new BeanShellAction( "import org.drools.semantic.java.BeanShellActionTest; BeanShellActionTest.STATUS = true;" );
            
            action.invoke( tuple,
                           null );

            assertTrue( STATUS );
        }
        catch (ActionInvokationException e)
        {
            fail( e.toString() );
        }
    }

    public void testInvokeWithError()
    {
        try
        {
            BeanShellAction action = new BeanShellAction( "Fnord.cheese = true;" );

            action.invoke( tuple,
                           null );

            fail( "Should have thrown ActionInokationException" );
        }
        catch (ActionInvokationException e)
        {
            // expected and correct.
        }
    }

    public void testTupleMemberAccess()
    {
        try
        {
            STATUS = false;

            BeanShellAction action = new BeanShellAction( "import org.drools.semantic.java.BeanShellActionTest; BeanShellActionTest.RETURN_1 = string; BeanShellActionTest.RETURN_2 = object;" );
            
            action.invoke( tuple,
                           null );

            assertSame( this.string,
                        RETURN_1 );

            assertSame( this.object,
                        RETURN_2 );
        }
        catch (ActionInvokationException e)
        {
            fail( e.toString() );
        }
    }
}
