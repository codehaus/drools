package org.drools.io;

import org.drools.DroolsTestCase;

public class SemanticsReaderTest
    extends DroolsTestCase
{
    public void testParseType()
    {
        SemanticsReader reader = new SemanticsReader();

        assertEquals( "cheese",
                      reader.parseType( "cheese(toast)" ) );

        assertNull( reader.parseType( "cheese" ) );

        assertEquals( "",
                      reader.parseType( "(toast)" ) );
    }

    public void testParseName()
    {
        SemanticsReader reader = new SemanticsReader();

        assertEquals( "toast",
                      reader.parseName( "cheese(toast)" ) );

        assertNull( reader.parseName( "cheese(" ) );

        assertNull( reader.parseName( "cheese)" ) );

        assertEquals( "",
                      reader.parseName( "cheese()" ) );
    }
}
