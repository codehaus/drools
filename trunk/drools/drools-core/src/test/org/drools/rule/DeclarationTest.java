package org.drools.rule;

import org.drools.spi.ObjectType;
import org.drools.semantics.java.ClassObjectType;

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
        ClassObjectType type = new ClassObjectType( String.class );

        Declaration decl = new Declaration( type,
                                            "cheese" );

        assertSame( type,
                    decl.getObjectType() );

        assertEquals( "cheese",
                      decl.getIdentifier() );
    }

    public void testEquals()
    {
        Declaration decl1 = new Declaration( new ClassObjectType( String.class ),
                                             "cheese" );

        Declaration decl2 = new Declaration( new ClassObjectType( String.class ),
                                             "cheese" );

        Declaration decl3 = new Declaration( new ClassObjectType( Object.class ),
                                             "cheese" );

        assertTrue( decl1.equals( decl2 ) );

        assertTrue( ! decl2.equals( decl3 ) );
    }
}
