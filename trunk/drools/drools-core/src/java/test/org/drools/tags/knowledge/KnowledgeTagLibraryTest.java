package org.drools.tags.knowledge;

import org.apache.commons.jelly.tags.junit.JellyTestSuite;

import junit.framework.TestSuite;

public class KnowledgeTagLibraryTest extends JellyTestSuite
{
    public static TestSuite suite() throws Exception
    {
         return createTestSuite( KnowledgeTagLibraryTest.class,
                                 "KnowledgeTagLibraryTest.jelly");        
    }
}
