package org.drools.semantics.python;

import org.drools.rule.Imports;
import org.drools.smf.SMFTestFrameWork;

/**
 * Extends SMFTestFrameWork specifying the python Semantic Module. The
 * SMFTestFramework base class then loads the python test data files, extracts
 * the tests for condition, extractors and consequences and executes them
 */
public class PythonSemanticTest extends SMFTestFrameWork
{
    public PythonSemanticTest(String name)
    {
        super( name );
    }

    public void setUp() throws Exception
    {
        Imports imports = new Imports();               
        imports.addImportEntry(new PythonImportEntry("from java.math import *"));
        imports.addImportEntry(new PythonImportEntry("from org.drools.smf import SMFTestFrameWork"));
        imports.addImportEntry(new PythonImportEntry("from org.drools.smf.SMFTestFrameWork import Cheese"));
        super.setUp( "python", imports );
    }
}