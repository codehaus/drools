package org.drools.semantics.python;

import org.drools.rule.Declaration;
import org.drools.smf.Configuration;
import org.drools.smf.ImportEntryFactory;
import org.drools.smf.FactoryException;
import org.drools.spi.ImportEntry;

public class PythonImportEntryFactory implements ImportEntryFactory
{
    private static final PythonImportEntryFactory INSTANCE = new PythonImportEntryFactory( );

    public static PythonImportEntryFactory getInstance()
    {
        return INSTANCE;
    }

    public ImportEntry newImportEntry(Configuration config) throws FactoryException
    {
        try
        {
            return new PythonImportEntry( config.getText() );
        }
        catch ( Exception e )
        {
            throw new FactoryException( e );
        }
    }
}