package org.drools.semantics.groovy;

import org.drools.smf.SMFTestFrameWork;

/**
 * Extends SMFTestFrameWork specifying the groovy Semantic Module. The
 * SMFTestFramework base class then loads the groovy test data files, extracts
 * the tests for condition, extractors and consequences and executes them
 */
public class GroovySemanticTest extends SMFTestFrameWork
{
    public GroovySemanticTest(String name)
    {
        super( name );
    }

    public void setUp() throws Exception
    {
        super.setUp( "groovy" );
    }
}