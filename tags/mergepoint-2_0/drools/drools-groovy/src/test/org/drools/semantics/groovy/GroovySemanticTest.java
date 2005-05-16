package org.drools.semantics.groovy;

import java.util.HashSet;
import java.util.Set;

import org.drools.semantics.base.BaseImportEntry;
import org.drools.smf.DefaultImporter;
import org.drools.smf.SMFTestFrameWork;
import org.drools.spi.Importer;

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
        Importer importer = new DefaultImporter( );
        importer.addImport( new BaseImportEntry( "java.math.*" ) );
        importer.addImport( new BaseImportEntry( "org.drools.smf.SMFTestFrameWork.Cheese" ) );
        super.setUp( "groovy",
                     importer );
    }
}