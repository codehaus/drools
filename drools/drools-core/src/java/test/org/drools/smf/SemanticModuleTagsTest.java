package org.drools.smf;

import org.apache.commons.jelly.tags.junit.JellyTestSuite;

import junit.framework.TestSuite;

public class SemanticModuleTagsTest extends JellyTestSuite
{
    public static TestSuite suite() throws Exception
    {
         return createTestSuite( SemanticModuleTagsTest.class,
                                 "SemanticModuleTagsTest.jelly");        
    }
}
