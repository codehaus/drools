package org.drools.semantics.python;

import org.drools.smf.SMFTestFrameWork;

/**
 * Extends SMFTestFrameWork specifying the python Semantic Module.
 * The SMFTestFramework base class then loads the python test data files,
 * extracts the tests for condition, extractors and consequences and
 * executes them
 */
public class PythonSemanticTest extends SMFTestFrameWork
{
    public PythonSemanticTest( String name )
    {
          super( name );
    }

    public void setUp() throws Exception
    {
        super.setUp("python");
    }
}
