/*
 * Created on Oct 18, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.drools.semantics.python;

import org.drools.spi.ImportEntry;

/**
 * @author mproctor
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class PythonImportEntry implements ImportEntry
{

    private String importEntry;
    
    public PythonImportEntry(String importEntry)
    {
        this.importEntry = importEntry;
    }
    
    /* (non-Javadoc)
     * @see org.drools.spi.ImportEntry#getImportEntry()
     */
    public String getImportEntry()
    {
        return this.importEntry;
    }
    
    public String toString()
    {
        return "[Import Entry: " + this.importEntry + "]";
    }
    
    public int hashCode()
    {
        return this.importEntry.hashCode();
    }
    
    public boolean equals(Object object)
    {
        if (object == null) return false;
        if (!(object instanceof ImportEntry)) return false;
        return this.importEntry.equals(((ImportEntry)object).getImportEntry());
    }
}
