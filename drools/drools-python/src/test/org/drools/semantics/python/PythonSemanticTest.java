package org.drools.semantics.python;

import java.util.HashSet;
import java.util.Set;

import org.drools.semantics.base.BaseImportEntry;
import org.drools.smf.DefaultImporter;
import org.drools.smf.SMFTestFrameWork;
import org.drools.spi.Importer;

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
        Importer importer = new DefaultImporter( );
        importer.addImport( new BaseImportEntry( "from java.math import *" ) );
        importer.addImport( new BaseImportEntry( "from org.drools.smf import SMFTestFrameWork" ) );
        importer.addImport( new BaseImportEntry( "from org.drools.smf.SMFTestFrameWork import Cheese" ) );
        super.setUp( "python",
                     importer );
    }
}