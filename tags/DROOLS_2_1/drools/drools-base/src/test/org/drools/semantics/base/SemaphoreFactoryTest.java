package org.drools.semantics.base;

/*
 * $Id: SemaphoreFactoryTest.java,v 1.3 2005-05-07 04:21:53 dbarnett Exp $
 *
 * Copyright 2005-2005 (C) The Werken Company. All Rights Reserved.
 *
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. The name "drools" must not be used to endorse or promote products derived
 * from this Software without prior written permission of The Werken Company.
 * For written permission, please contact bob@werken.com.
 *
 * 4. Products derived from this Software may not be called "drools" nor may
 * "drools" appear in their names without prior written permission of The Werken
 * Company. "drools" is a registered trademark of The Werken Company.
 *
 * 5. Due credit should be given to The Werken Company.
 * (http://drools.werken.com/).
 *
 * THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE WERKEN COMPANY OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */

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

