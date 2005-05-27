package org.drools.semantics.base;

import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.smf.DefaultConfiguration;
import org.drools.smf.DefaultImporter;
import org.drools.smf.FactoryException;
import org.drools.smf.ObjectTypeFactory;
import org.drools.spi.Importer;
import org.drools.spi.RuleBaseContext;
import org.drools.spi.Semaphore;

import junit.framework.TestCase;

public class SemaphoreFactoryTest extends TestCase
{

    public void testSemaphoreFactoryExceptions() throws Exception
    {
        RuleBaseContext ruleBaseContext = new RuleBaseContext( );

        DefaultConfiguration configuration = new DefaultConfiguration( "test1" );

        final RuleSet ruleSet = new RuleSet( "test RuleSet",
                                             ruleBaseContext );

        final Rule rule = new Rule( "Test Rule 1",
                                    ruleSet );

        Importer importer = new DefaultImporter( );
        rule.setImporter( importer );

        ObjectTypeFactory factory = new SemaphoreFactory( );

        try
        {
            ClassObjectType type = (ClassObjectType) factory.newObjectType( rule,
                                                                            ruleBaseContext,
                                                                            configuration );
            fail( "SemaphoreFactory should throw an exception as no type is specified" );
        }
        catch ( FactoryException e )
        {
            assertEquals( "no Semaphore type specified",
                          e.getMessage( ) );
        }

        configuration.setAttribute( "type",
                                    "Person" );
        try
        {
            ClassObjectType type = (ClassObjectType) factory.newObjectType( rule,
                                                                            ruleBaseContext,
                                                                            configuration );
            fail( "SemaphoreFactory should throw an exception as no identifier is specified" );
        }
        catch ( FactoryException e )
        {
            assertEquals( "no Semaphore identifier specified",
                          e.getMessage( ) );
        }

        configuration.setAttribute( "type",
                                    "NoneExistingClass" );
        configuration.setAttribute( "identifier",
                                    "state" );
        try
        {
            ClassObjectType type = (ClassObjectType) factory.newObjectType( rule,
                                                                            ruleBaseContext,
                                                                            configuration );
            fail( "SemaphoreFactory should throw an exception as class NoneExistingClass does not exist." );
        }
        catch ( FactoryException e )
        {
            assertEquals( "Unable create Semaphore for type 'NoneExistingClass'",
                          e.getMessage( ) );
        }

        configuration.setAttribute( "type",
                                    "Person" );
        configuration.setAttribute( "identifier",
                                    "state" );
        try
        {
            ClassObjectType type = (ClassObjectType) factory.newObjectType( rule,
                                                                            ruleBaseContext,
                                                                            configuration );
            fail( "SemaphoreFactory should throw an exception as class Person has no identifier field." );
        }
        catch ( FactoryException e )
        {
            assertEquals( "Field 'identifier' does not exist for Class 'org.drools.semantics.base.PersonSemaphore'",
                          e.getMessage( ) );
        }
    }

    public void testStringSemaphore() throws Exception
    {
        testSemaphoreImpl( StringSemaphore.class,
                           "String",
                           new StringSemaphore( "state" ),
                           new IntegerSemaphore( "state" ),
                           new StringSemaphore( "xxx" ) );
    }

    public void testShortSemaphore() throws Exception
    {
        testSemaphoreImpl( ShortSemaphore.class,
                           "Short",
                           new ShortSemaphore( "state" ),
                           new StringSemaphore( "state" ),
                           new ShortSemaphore( "xxx" ) );
    }

    public void testSetSemaphore() throws Exception
    {
        testSemaphoreImpl( SetSemaphore.class,
                           "Set",
                           new SetSemaphore( "state" ),
                           new StringSemaphore( "state" ),
                           new SetSemaphore( "xxx" ) );
    }

    public void testMapSemaphore() throws Exception
    {
        testSemaphoreImpl( MapSemaphore.class,
                           "Map",
                           new MapSemaphore( "state" ),
                           new StringSemaphore( "state" ),
                           new MapSemaphore( "xxx" ) );
    }

    public void testLongSemaphore() throws Exception
    {
        testSemaphoreImpl( LongSemaphore.class,
                           "Long",
                           new LongSemaphore( "state" ),
                           new StringSemaphore( "state" ),
                           new LongSemaphore( "xxx" ) );
    }

    public void testListSemaphore() throws Exception
    {
        testSemaphoreImpl( ListSemaphore.class,
                           "List",
                           new ListSemaphore( "state" ),
                           new StringSemaphore( "state" ),
                           new ListSemaphore( "xxx" ) );
    }

    public void testIntegerSemaphore() throws Exception
    {
        testSemaphoreImpl( IntegerSemaphore.class,
                           "Integer",
                           new IntegerSemaphore( "state" ),
                           new StringSemaphore( "state" ),
                           new IntegerSemaphore( "xxx" ) );
    }

    private void testSemaphoreImpl(Class clazz,
                                   String typeName,
                                   Semaphore matchedSemaphore,
                                   Semaphore badTypeSemaphore,
                                   Semaphore badIdentifierSemaphore) throws Exception
    {
        RuleBaseContext ruleBaseContext = new RuleBaseContext( );

        DefaultConfiguration configuration = new DefaultConfiguration( "test1" );
        configuration.setAttribute( "type",
                                    typeName );
        configuration.setAttribute( "identifier",
                                    "state" );

        final RuleSet ruleSet = new RuleSet( "test RuleSet",
                                             ruleBaseContext );

        final Rule rule = new Rule( "Test Rule 1",
                                    ruleSet );

        Importer importer = new DefaultImporter( );
        rule.setImporter( importer );

        ObjectTypeFactory factory = new SemaphoreFactory( );

        ClassObjectType type = (ClassObjectType) factory.newObjectType( rule,
                                                                        ruleBaseContext,
                                                                        configuration );
        assertEquals( clazz,
                      type.getType( ) );

        assertTrue( type.matches( matchedSemaphore ) );

        assertFalse( type.matches( badTypeSemaphore ) );

        assertFalse( type.matches( badIdentifierSemaphore ) );
    }
}
