package org.drools.rule;

import org.drools.DroolsTestCase;
import org.drools.spi.MockObjectType;

public class DeclarationTest extends DroolsTestCase
{
    public void testIsComparable()
    {
        assertTrue(Comparable.class.isAssignableFrom( Declaration.class ) );
        
        Declaration declaration37a = new Declaration( "id", new MockObjectType( Object.class ), 37 );
        Declaration declaration37b = new Declaration( "id", new MockObjectType( Object.class ), 37 );
        Declaration declaration59 = new Declaration( "id", new MockObjectType( Object.class ), 59 );

        assertTrue( declaration37a.compareTo( declaration59) < 0 );
        assertTrue( declaration59.compareTo( declaration37a) > 0 );
        assertTrue( declaration37a.compareTo( declaration37b) == 0 );
    }
}