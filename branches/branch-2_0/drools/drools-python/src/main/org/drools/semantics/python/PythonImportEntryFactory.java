package org.drools.semantics.python;

import org.drools.rule.RuleSet;
import org.drools.smf.Configuration;
import org.drools.smf.FactoryException;
import org.drools.smf.ImportEntryFactory;
import org.drools.spi.ImportEntry;
import org.drools.spi.RuleBaseContext;

public class PythonImportEntryFactory implements ImportEntryFactory
{
    private static final PythonImportEntryFactory INSTANCE = new PythonImportEntryFactory( );

    public static PythonImportEntryFactory getInstance()
    {
        return INSTANCE;
    }

    public ImportEntry newImportEntry(RuleSet ruleSet,
                                      RuleBaseContext context,
                                      Configuration config) throws FactoryException
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