package org.drools.semantics.java;

import java.util.HashSet;
import java.util.Set;

import org.drools.rule.Imports;
import org.drools.smf.SMFTestFrameWork;

/**
 * Extends SMFTestFrameWork specifying the java Semantic Module. The
 * SMFTestFramework base class then loads the java test data files, extracts the
 * tests for condition, extractors and consequences and executes them
 */
public class JavaSemanticTest extends SMFTestFrameWork
{
    public JavaSemanticTest(String name)
    {
        super( name );
    }

    public void setUp() throws Exception
    {
        Set imports = new HashSet();               
        imports.add(new JavaImportEntry("java.math.*"));
        imports.add(new JavaImportEntry("org.drools.smf.SMFTestFrameWork"));                 
        super.setUp( "java", imports );
    }
}