package org.drools.tags.semantics;

import org.apache.commons.jelly.tags.junit.JellyTestSuite;

import junit.framework.TestSuite;

public class SemanticsTagLibraryTest extends JellyTestSuite
{
    public static TestSuite suite() throws Exception
    {
         return createTestSuite( SemanticsTagLibraryTest.class,
                                 "SemanticsTagLibraryTest.jelly");        
    }
}
