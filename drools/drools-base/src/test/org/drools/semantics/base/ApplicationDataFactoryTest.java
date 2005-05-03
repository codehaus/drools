package org.drools.semantics.base;

import org.drools.rule.ApplicationData;
import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.smf.ApplicationDataFactory;
import org.drools.smf.DefaultConfiguration;
import org.drools.smf.DefaultImporter;
import org.drools.smf.FactoryException;
import org.drools.smf.ObjectTypeFactory;
import org.drools.spi.ImportEntry;
import org.drools.spi.Importer;
import org.drools.spi.ObjectType;
import org.drools.spi.RuleBaseContext;

import junit.framework.TestCase;

public class ApplicationDataFactoryTest extends TestCase
{

    public void testApplicationDataWithOutImports() throws FactoryException
    {
        RuleBaseContext ruleBaseContext = new RuleBaseContext( );

        DefaultConfiguration configuration = new DefaultConfiguration( "test1" );
        configuration.setText( "java.util.HashMap" );
        configuration.setAttribute( "identifier",
                                    "map" );

        final RuleSet ruleSet = new RuleSet( "test RuleSet",
                                             ruleBaseContext );

        Importer importer = new DefaultImporter( );
        ruleSet.setImporter( importer );

        ApplicationDataFactory factory = new BaseApplicationDataFactory( );

        ApplicationData applicationData = (ApplicationData) factory.newApplicationData( ruleSet,
                                                                                        ruleBaseContext,
                                                                                        configuration );

        assertEquals( java.util.HashMap.class,
                      applicationData.getType( ) );

        assertEquals( "map",
                      applicationData.getIdentifier( ) );
    }

    public void testApplicationDataWithStaticImports() throws FactoryException
    {
        RuleBaseContext ruleBaseContext = new RuleBaseContext( );

        DefaultConfiguration configuration = new DefaultConfiguration( "test1" );
        configuration.setText( "HashMap" );
        configuration.setAttribute( "identifier",
                                    "map" );

        final RuleSet ruleSet = new RuleSet( "test RuleSet",
                                             ruleBaseContext );

        Importer importer = new DefaultImporter( );
        importer.addImport( new DefaultImportEntry( "java.util.HashMap" ) );
        ruleSet.setImporter( importer );

        ApplicationDataFactory factory = new BaseApplicationDataFactory( );

        ApplicationData applicationData = (ApplicationData) factory.newApplicationData( ruleSet,
                                                                                        ruleBaseContext,
                                                                                        configuration );

        assertEquals( java.util.HashMap.class,
                      applicationData.getType( ) );

        assertEquals( "map",
                      applicationData.getIdentifier( ) );
    }

    public void testApplicationDataDynamicImports() throws FactoryException
    {
        RuleBaseContext ruleBaseContext = new RuleBaseContext( );

        DefaultConfiguration configuration = new DefaultConfiguration( "test1" );
        configuration.setText( "HashMap" );
        configuration.setAttribute( "identifier",
                                    "map" );

        final RuleSet ruleSet = new RuleSet( "test RuleSet",
                                             ruleBaseContext );

        Importer importer = new DefaultImporter( );
        importer.addImport( new DefaultImportEntry( "java.util.*" ) );
        ruleSet.setImporter( importer );

        ApplicationDataFactory factory = new BaseApplicationDataFactory( );

        ApplicationData applicationData = (ApplicationData) factory.newApplicationData( ruleSet,
                                                                                        ruleBaseContext,
                                                                                        configuration );

        assertEquals( java.util.HashMap.class,
                      applicationData.getType( ) );

        assertEquals( "map",
                      applicationData.getIdentifier( ) );
    }

    private class DefaultImportEntry
        implements
        ImportEntry
    {
        private String importEntry;

        public DefaultImportEntry(String importEntry)
        {
            this.importEntry = importEntry;
        }

        public String getImportEntry()
        {
            return this.importEntry;
        }
    }
    
    

}
