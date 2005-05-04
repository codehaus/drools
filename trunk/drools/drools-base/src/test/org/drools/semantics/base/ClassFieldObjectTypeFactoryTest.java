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

public class ClassFieldObjectTypeFactoryTest extends TestCase
{

    public void testClassOFieldbjectTypeFactoryExceptions() throws Exception
    {
        RuleBaseContext ruleBaseContext = new RuleBaseContext( );

        DefaultConfiguration configuration = new DefaultConfiguration( "test1" );

        final RuleSet ruleSet = new RuleSet( "test RuleSet",
                                             ruleBaseContext );

        final Rule rule = new Rule( "Test Rule 1",
                                    ruleSet );

        Importer importer = new DefaultImporter( );
        rule.setImporter( importer );

        ObjectTypeFactory factory = new ClassFieldObjectTypeFactory( );

        try
        {
            ClassObjectType type = (ClassObjectType) factory.newObjectType( rule,
                                                                            ruleBaseContext,
                                                                            configuration );
            fail( "Should fail, no field name has been specified" );
        }
        catch ( FactoryException e )
        {
            assertEquals( "no class name specified",
                          e.getMessage( ) );
        }

        configuration.setText( "org.drools.semantics.base.Person" );
        try
        {
            ClassObjectType type = (ClassObjectType) factory.newObjectType( rule,
                                                                            ruleBaseContext,
                                                                            configuration );
            fail( "Should fail, no field name has been specified" );
        }
        catch ( FactoryException e )
        {
            assertEquals( "no field name specified",
                          e.getMessage( ) );
        }

        configuration.setAttribute( "field",
                                    "name" );
        try
        {
            ClassObjectType type = (ClassObjectType) factory.newObjectType( rule,
                                                                            ruleBaseContext,
                                                                            configuration );
            fail( "Should fail, no field value has been specified" );
        }
        catch ( FactoryException e )
        {
            assertEquals( "no field value specified",
                          e.getMessage( ) );
        }
    }

    public void testObjectTypeWithOutImports() throws FactoryException
    {
        RuleBaseContext ruleBaseContext = new RuleBaseContext( );

        DefaultConfiguration configuration = new DefaultConfiguration( "test1" );
        configuration.setText( "org.drools.semantics.base.Person" );
        configuration.setAttribute( "field",
                                    "name" );
        configuration.setAttribute( "value",
                                    "bob" );

        final RuleSet ruleSet = new RuleSet( "test RuleSet",
                                             ruleBaseContext );

        final Rule rule = new Rule( "Test Rule 1",
                                    ruleSet );

        Importer importer = new DefaultImporter( );
        rule.setImporter( importer );

        ObjectTypeFactory factory = new ClassFieldObjectTypeFactory( );

        ClassFieldObjectType type = (ClassFieldObjectType) factory.newObjectType( rule,
                                                                                  ruleBaseContext,
                                                                                  configuration );

        assertEquals( Person.class,
                      type.getType( ) );

        assertTrue( type.matches( new Person( "bob" ) ) );
        assertFalse( type.matches( new Person( "tim" ) ) );

    }

    public void testObjectTypeWithStaticImports() throws FactoryException
    {
        RuleBaseContext ruleBaseContext = new RuleBaseContext( );

        DefaultConfiguration configuration = new DefaultConfiguration( "test1" );
        configuration.setText( "Person" );
        configuration.setAttribute( "field",
                                    "name" );
        configuration.setAttribute( "value",
                                    "bob" );

        final RuleSet ruleSet = new RuleSet( "test RuleSet",
                                             ruleBaseContext );

        final Rule rule = new Rule( "Test Rule 1",
                                    ruleSet );

        Importer importer = new DefaultImporter( );
        importer.addImport( new DefaultImportEntry( "org.drools.semantics.base.Person" ) );
        rule.setImporter( importer );

        ObjectTypeFactory factory = new ClassFieldObjectTypeFactory( );

        ClassFieldObjectType type = (ClassFieldObjectType) factory.newObjectType( rule,
                                                                                  ruleBaseContext,
                                                                                  configuration );

        assertEquals( Person.class,
                      type.getType( ) );

        assertTrue( type.matches( new Person( "bob" ) ) );
        assertFalse( type.matches( new Person( "tim" ) ) );

    }

    public void testbjectTypeWithDynamicImports() throws FactoryException
    {
        RuleBaseContext ruleBaseContext = new RuleBaseContext( );

        DefaultConfiguration configuration = new DefaultConfiguration( "test1" );
        configuration.setText( "Person" );
        configuration.setAttribute( "field",
                                    "name" );
        configuration.setAttribute( "value",
                                    "bob" );

        final RuleSet ruleSet = new RuleSet( "test RuleSet",
                                             ruleBaseContext );

        final Rule rule = new Rule( "Test Rule 1",
                                    ruleSet );

        Importer importer = new DefaultImporter( );
        importer.addImport( new DefaultImportEntry( "org.drools.semantics.base.*" ) );
        rule.setImporter( importer );

        ObjectTypeFactory factory = new ClassFieldObjectTypeFactory( );

        ClassFieldObjectType type = (ClassFieldObjectType) factory.newObjectType( rule,
                                                                                  ruleBaseContext,
                                                                                  configuration );

        assertEquals( Person.class,
                      type.getType( ) );

        assertTrue( type.matches( new Person( "bob" ) ) );
        assertFalse( type.matches( new Person( "tim" ) ) );

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
