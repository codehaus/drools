
package org.drools.semantic.java;

import org.drools.spi.ObjectType;

import junit.framework.TestCase;

public class JavaObjectTypeTest extends TestCase
{
    public JavaObjectTypeTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
    }

    public void tearDown()
    {
    }

    public void testExactMatches()
    {
        ObjectType stringType = new JavaObjectType( String.class );

        assertTrue( stringType.matches( "cheese" ) );
    }

    public void testPolymorphicMatches()
    {
        ObjectType objectType = new JavaObjectType( Object.class );

        assertTrue( objectType.matches( new Object() ) );

        assertTrue( objectType.matches( "cheese" ) );
    }

    public void testEquals()
    {
        ObjectType type1 = new JavaObjectType( String.class );
        ObjectType type2 = new JavaObjectType( String.class );
        ObjectType type3 = new JavaObjectType( Object.class );

        assertTrue( type1.equals( type2 ) );
        assertTrue( ! type2.equals( type3 ) );
    }
}
