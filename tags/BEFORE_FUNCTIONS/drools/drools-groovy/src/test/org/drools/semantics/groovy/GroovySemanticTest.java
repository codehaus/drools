package org.drools.semantics.groovy;

import org.drools.smf.SMFTestFrameWork;

import java.util.HashSet;
import java.util.Set;

/**
 * Extends SMFTestFrameWork specifying the groovy Semantic Module. The
 * SMFTestFramework base class then loads the groovy test data files, extracts
 * the tests for conditions and consequences and executes them
 */
public class GroovySemanticTest extends SMFTestFrameWork
{
    public GroovySemanticTest(String name)
    {
        super( name );
    }

    public void setUp() throws Exception
    {
        Set imports = new HashSet();
        imports.add(new GroovyImportEntry("java.math.*"));
        imports.add(new GroovyImportEntry("org.drools.smf.SMFTestFrameWork.Cheese"));
        super.setUp( "groovy", imports );
    }
}