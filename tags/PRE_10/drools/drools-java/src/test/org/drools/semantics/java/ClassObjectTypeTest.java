package org.drools.semantics.java;

import junit.framework.TestCase;

public class ClassObjectTypeTest extends TestCase
{
    public ClassObjectTypeTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
    }

    public void tearDown()
    {
    }

    public void testConstructor_Default()
    {
        ClassObjectType type = new ClassObjectType();

        assertSame( java.lang.Object.class,
                    type.getType() );
    }

    public void testConstructor_DefaultOverride()
    {
        ClassObjectType type = new ClassObjectType();

        type.setType( java.lang.String.class );

        assertSame( java.lang.String.class,
                    type.getType() );
    }

    public void testConstructor_Class()
    {
        ClassObjectType type = new ClassObjectType( java.lang.String.class );

        assertSame( java.lang.String.class,
                    type.getType() );
    }

    public void testMatches_Invalid()
    {
        ClassObjectType type = new ClassObjectType( java.lang.String.class );

        assertTrue( ! type.matches( new Integer( 42 ) ) );
    }

    public void testMatches_ValidExact()
    {
        ClassObjectType type = new ClassObjectType( java.lang.String.class );

        assertTrue( type.matches( new String( "String instance" ) ) );
    }

    public void testMatches_ValidSubclass()
    {
        ClassObjectType type = new ClassObjectType( java.lang.Number.class );

        assertTrue( type.matches( new Integer( 42 ) ) );
        assertTrue( type.matches( new Long( 42L ) ) );
    }
} 
