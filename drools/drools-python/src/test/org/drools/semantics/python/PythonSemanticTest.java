package org.drools.semantics.python;

import org.drools.smf.SMFTestFrameWork;

import java.util.HashSet;
import java.util.Set;

/**
 * Extends SMFTestFrameWork specifying the python Semantic Module. The
 * SMFTestFramework base class then loads the python test data files, extracts
 * the tests for conditions and consequences and executes them
 */
public class PythonSemanticTest extends SMFTestFrameWork
{
    public PythonSemanticTest(String name)
    {
        super( name );
    }

    public void setUp() throws Exception
    {
        Set imports = new HashSet();
        imports.add(new PythonImportEntry("from java.math import *"));
        imports.add(new PythonImportEntry("from org.drools.smf import SMFTestFrameWork"));
        imports.add(new PythonImportEntry("from org.drools.smf.SMFTestFrameWork import Cheese"));
        super.setUp( "python", imports );
    }
}