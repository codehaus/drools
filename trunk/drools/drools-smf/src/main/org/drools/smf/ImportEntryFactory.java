package org.drools.smf;

import org.drools.spi.ImportEntry;

public interface ImportEntryFactory
{
    ImportEntry newImportEntry(Configuration config) throws FactoryException;
}