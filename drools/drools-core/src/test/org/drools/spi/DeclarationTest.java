
package org.drools.spi;

import org.drools.semantic.java.JavaObjectType;

import junit.framework.TestCase;

public class DeclarationTest extends TestCase
{
    public DeclarationTest(String name)
    {
        super(  name );
    }

    public void setUp()
    {
    }

    public void tearDown()
    {
    }

    public void testConstruct()
    {
        ObjectType type = new JavaObjectType( String.class );

        Declaration decl = new Declaration( type,
                                            "cheese" );

        assertSame( type,
                    decl.getObjectType() );

        assertEquals( "cheese",
                      decl.getIdentifier() );
    }

    public void testEquals()
    {
        Declaration decl1 = new Declaration( new JavaObjectType( String.class ),
                                             "cheese" );

        Declaration decl2 = new Declaration( new JavaObjectType( String.class ),
                                             "cheese" );

        Declaration decl3 = new Declaration( new JavaObjectType( Object.class ),
                                             "cheese" );

        assertTrue( decl1.equals( decl2 ) );

        assertTrue( ! decl2.equals( decl3 ) );
    }
}
