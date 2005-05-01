package org.drools.semantics.base;

import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.smf.DefaultConfiguration;
import org.drools.smf.DefaultImporter;
import org.drools.smf.FactoryException;
import org.drools.smf.ObjectTypeFactory;
import org.drools.spi.ImportEntry;
import org.drools.spi.Importer;
import org.drools.spi.ObjectType;
import org.drools.spi.RuleBaseContext;

import junit.framework.TestCase;

public class ClassObjectTypeFactoryTest extends TestCase
{
    
    public void testClassObjectTypeFactoryExceptions() throws Exception
    {
        RuleBaseContext ruleBaseContext = new RuleBaseContext( );

        DefaultConfiguration configuration = new DefaultConfiguration( "test1" );

        final RuleSet ruleSet = new RuleSet( "test RuleSet",
                                             ruleBaseContext );

        final Rule rule = new Rule( "Test Rule 1",
                                    ruleSet );

        Importer importer = new DefaultImporter( );
        rule.setImporter( importer );

        ObjectTypeFactory factory = new ClassObjectTypeFactory( );

        try
        {
            ClassObjectType type = (ClassObjectType) factory.newObjectType( rule,
                                                                            ruleBaseContext,
                                                                            configuration );
            fail( "Should fail, no class name has been specified" );
        }
        catch ( FactoryException e )
        {
            assertEquals( "no class name specified",
                          e.getMessage( ) );
        }    
    }
    
    public void testObjectTypeWithOutImports() throws FactoryException
    {
        RuleBaseContext ruleBaseContext = new RuleBaseContext( );

        DefaultConfiguration configuration = new DefaultConfiguration( "test1" );
        configuration.setText( "java.util.HashMap" );

        final RuleSet ruleSet = new RuleSet( "test RuleSet",
                                             ruleBaseContext );

        final Rule rule = new Rule( "Test Rule 1",
                                    ruleSet );

        Importer importer = new DefaultImporter( );       
        rule.setImporter( importer );

        ObjectTypeFactory factory = new ClassObjectTypeFactory( );

        ClassObjectType type = (ClassObjectType) factory.newObjectType( rule,
                                                                        ruleBaseContext,
                                                                        configuration );

        assertEquals( java.util.HashMap.class,
                      type.getType( ) );

        assertTrue( type.matches( new java.util.HashMap( ) ) );
    }

    public void testObjectTypeWithStaticImports() throws FactoryException
    {
        RuleBaseContext ruleBaseContext = new RuleBaseContext( );

        DefaultConfiguration configuration = new DefaultConfiguration( "test1" );
        configuration.setText( "HashMap" );

        final RuleSet ruleSet = new RuleSet( "test RuleSet",
                                             ruleBaseContext );

        final Rule rule = new Rule( "Test Rule 1",
                                    ruleSet );

        Importer importer = new DefaultImporter( );
        importer.addImport( new DefaultImportEntry( "java.util.HashMap" ) ); 
        rule.setImporter( importer );

        ObjectTypeFactory factory = new ClassObjectTypeFactory( );

        ClassObjectType type = (ClassObjectType) factory.newObjectType( rule,
                                                                        ruleBaseContext,
                                                                        configuration );

        assertEquals( java.util.HashMap.class,
                      type.getType( ) );

        assertTrue( type.matches( new java.util.HashMap( ) ) );        
    }

    public void testObjectTypeWithDynamicImports() throws FactoryException
    {
        RuleBaseContext ruleBaseContext = new RuleBaseContext( );

        DefaultConfiguration configuration = new DefaultConfiguration( "test1" );
        configuration.setText( "HashMap" );

        final RuleSet ruleSet = new RuleSet( "test RuleSet",
                                             ruleBaseContext );

        Importer importer = new DefaultImporter( );
        importer.addImport( new DefaultImportEntry( "java.util.*" ) );

        final Rule rule = new Rule( "Test Rule 1",
                                    ruleSet );

        rule.setImporter( importer );

        ObjectTypeFactory factory = new ClassObjectTypeFactory( );

        ClassObjectType type = (ClassObjectType) factory.newObjectType( rule,
                                                                        ruleBaseContext,
                                                                        configuration );

        assertEquals( java.util.HashMap.class,
                      type.getType( ) );

        assertTrue( type.matches( new java.util.HashMap( ) ) );    }

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
