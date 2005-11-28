package org.drools.semantics.base;

/*
 * Copyright 2004 (C) The Werken Company. All Rights Reserved.
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
import org.drools.smf.Configuration;
import org.drools.smf.FactoryException;
import org.drools.smf.ObjectTypeFactory;
import org.drools.spi.Importer;
import org.drools.spi.ObjectType;
import org.drools.spi.RuleBaseContext;

public class ClassObjectTypeFactory
    implements
    ObjectTypeFactory
{
    private static final ClassObjectTypeFactory INSTANCE = new ClassObjectTypeFactory( );

    public static ClassObjectTypeFactory getInstance()
    {
        return INSTANCE;
    }

    public ObjectType newObjectType(Rule rule,
                                    RuleBaseContext context,
                                    Configuration config) throws FactoryException
    {
        String className = config.getText( ).trim( );

        if ( className == null || className.trim( ).equals( "" ) )
        {
            throw new FactoryException( "no class name specified" );
        }

        Class clazz = null;
        try
        {
            ClassLoader cl = (ClassLoader) context.get( "smf-classLoader" );
            if ( cl == null )
            {
                cl = Thread.currentThread( ).getContextClassLoader( );
                context.put( "smf-classLoader",
                             cl );
            }

            if ( cl == null )
            {
                cl = getClass( ).getClassLoader( );
                context.put( "smf-classLoader",
                             cl );
            }

            Importer importer = rule.getImporter( );
            clazz = importer.importClass( cl,
                                          className );
        }
        catch ( ClassNotFoundException e )
        {
            throw new FactoryException( e.getMessage( ) );
        }
        catch ( Error e )
        {
            throw new FactoryException( e.getMessage( ) );
        }

        return new ClassObjectType( clazz );
    }
    /*
     * private Class importClass(ClassLoader cl, String importText, String
     * className) { String qualifiedClass = null; Class clazz = null;
     * 
     * String convertedImportText; if ( importText.startsWith( "from " ) ) {
     * convertedImportText = converPythonImport( importText ); } else {
     * convertedImportText = importText; }
     *  // not python if ( convertedImportText.endsWith( "*" ) ) {
     * qualifiedClass = convertedImportText.substring( 0,
     * convertedImportText.indexOf( '*' ) ) + className; } else if (
     * convertedImportText.endsWith( "." + className ) ) { qualifiedClass =
     * convertedImportText; } else if ( convertedImportText.equals( className ) ) {
     * qualifiedClass = convertedImportText; }
     * 
     * 
     * if ( qualifiedClass != null ) { try { clazz = cl.loadClass(
     * qualifiedClass ); } catch ( ClassNotFoundException e ) { clazz = null; } }
     * return clazz; }
     * 
     * private String converPythonImport(String packageText) { String fromString =
     * "from "; String importString = "import "; int fromIndex =
     * packageText.indexOf( fromString ); int importIndex = packageText.indexOf(
     * importString ); return packageText.substring( fromIndex +
     * fromString.length( ), importIndex ).trim( ) + "." +
     * packageText.substring( importIndex + importString.length( ) ).trim( ); }
     */
}