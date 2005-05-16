package org.drools.semantics.groovy;

import org.drools.rule.RuleSet;
import org.drools.smf.Configuration;
import org.drools.smf.FactoryException;
import org.drools.smf.ImportEntryFactory;
import org.drools.spi.ImportEntry;
import org.drools.spi.RuleBaseContext;

public class GroovyImportEntryFactory implements ImportEntryFactory
{
    public ImportEntry newImportEntry(RuleSet ruleSet,
                                      RuleBaseContext context,
                                      Configuration config) throws FactoryException
    {
        try
        {
            return new GroovyImportEntry( config.getText() );
        }
        catch ( Exception e )
        {
            throw new FactoryException( e );
        }
    }
}