package org.drools.tags.rule;

import org.apache.commons.jelly.tags.junit.JellyTestSuite;

import junit.framework.TestSuite;

public class RuleTagLibraryTest extends JellyTestSuite
{
    public static TestSuite suite() throws Exception
    {
         return createTestSuite( RuleTagLibraryTest.class,
                                 "RuleTagLibraryTest.jelly");        
    }
}
