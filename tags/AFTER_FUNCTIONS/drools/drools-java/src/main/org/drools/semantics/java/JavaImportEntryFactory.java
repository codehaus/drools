package org.drools.semantics.java;

import org.drools.rule.RuleSet;
import org.drools.smf.Configuration;
import org.drools.smf.ImportEntryFactory;
import org.drools.smf.FactoryException;
import org.drools.spi.RuleBaseContext;
import org.drools.spi.ImportEntry;

public class JavaImportEntryFactory implements ImportEntryFactory
{
    private static final JavaImportEntryFactory INSTANCE = new JavaImportEntryFactory( );

    public static JavaImportEntryFactory getInstance()
    {
        return INSTANCE;
    }

    public ImportEntry newImportEntry(RuleSet ruleSet,
                                      RuleBaseContext context,
                                      Configuration config) throws FactoryException
    {
        try
        {
            return new JavaImportEntry( config.getText() );
        }
        catch ( Exception e )
        {
            throw new FactoryException( e );
        }
    }
}