/*
 * Created on Oct 18, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.drools.rule;
import java.util.HashSet;
import java.util.Set;

import org.drools.spi.ImportEntry;
/**
 * @author mproctor
 * 
 * TODO To change the template for this generated type comment go to Window -
 * Preferences - Java - Code Style - Code Templates
 */
public class Imports
{
    private Set imports;

    public Imports()
    {

    }

    public void addImportEntry(ImportEntry importEntry)
    {
        if ( this.imports == null )
        {
            this.imports = new HashSet( );
        }
        this.imports.add( importEntry );

    }

    public void removeImportEntry(ImportEntry importEntry)
    {
        imports.remove( importEntry );
    }

    public Set getImportEntries()
    {
        return this.imports;
    }
}
