package org.drools.semantics.groovy;

import org.drools.rule.Imports;
import org.drools.semantics.groovy.GroovyImportEntry;
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
        Imports imports = new Imports();               
        imports.addImportEntry(new GroovyImportEntry("java.math.*"));
        imports.addImportEntry(new GroovyImportEntry("org.drools.smf.SMFTestFrameWork.Cheese"));                 
        super.setUp( "groovy", imports );
    }
}