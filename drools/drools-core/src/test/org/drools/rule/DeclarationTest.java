package org.drools.rule;

import junit.framework.TestCase;

import org.drools.MockObjectType;

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

    public void testConstruct_Complete()
    {
        MockObjectType type = new MockObjectType();

        Declaration decl = new Declaration( type,
                                            "cheese" );

        assertSame( type,
                    decl.getObjectType() );

        assertEquals( "cheese",
                      decl.getIdentifier() );
    }

    public void testConstruct_Incomplete()
    {
        Declaration decl = new Declaration( "cheese" );

        assertNull( decl.getObjectType() );

        assertEquals( "cheese",
                      decl.getIdentifier() );

        MockObjectType type = new MockObjectType();

        decl.setObjectType( type );

        assertSame( type,
                    decl.getObjectType() );
    }

    public void testEquals()
    {
        Declaration decl1 = new Declaration( new MockObjectType( String.class ),
                                             "cheese" );

        Declaration decl2 = new Declaration( new MockObjectType( String.class ),
                                             "cheese" );

        Declaration decl3 = new Declaration( new MockObjectType( Object.class ),
                                             "cheese" );

        assertTrue( decl1.equals( decl2 ) );

        assertTrue( ! decl2.equals( decl3 ) );
    }
}
