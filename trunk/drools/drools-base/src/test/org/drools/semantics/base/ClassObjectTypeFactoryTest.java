package org.drools.semantics.base;

/*
 * $Id: ClassObjectTypeFactoryTest.java,v 1.3 2005-05-07 04:21:53 dbarnett Exp $
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
import org.drools.spi.ImportEntry;
import org.drools.spi.Importer;
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

