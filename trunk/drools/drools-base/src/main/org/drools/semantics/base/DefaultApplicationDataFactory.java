package org.drools.semantics.base;

/*
 * $Id: DefaultApplicationDataFactory.java,v 1.3 2004-12-13 20:54:51 mproctor Exp $
 *
 * Copyright 2002 (C) The Werken Company. All Rights Reserved.
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

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.drools.rule.ApplicationData;
import org.drools.smf.Configuration;
import org.drools.smf.ApplicationDataFactory;
import org.drools.smf.FactoryException;

import org.drools.spi.ImportEntry;

public class DefaultApplicationDataFactory
    implements
    ApplicationDataFactory
{
    public ApplicationData newApplicationData(Configuration config, Set imports) throws FactoryException
    {
        ClassLoader cl = Thread.currentThread( ).getContextClassLoader( );

          Class clazz = null;
          /* first try loading className */
          String className = config.getText( ).trim( );
          try
          {
              clazz = cl.loadClass( className );
          }
          catch ( Exception e )
          {
              // ignore for now
          }

          if ( null == clazz ) {
             //get imports
             Set importSet = new HashSet();
             if ( imports != null )
             {

                 Iterator it = imports.iterator();
                 ImportEntry importEntry;
                 while ( it.hasNext( ) )
                 {
                     importEntry = ( ImportEntry ) it.next( );
                     importSet.add( importEntry.getImportEntry( ) );
                 }
             }

             /* Now try the className with each of the given imports */
             if ( clazz == null )
             {
                 Iterator it = importSet.iterator();
                 while ( it.hasNext( ) && clazz == null )
                 {
                     clazz = importClass( cl, ( String ) it.next( ), className ) ;
                 }
             }
          }

         if ( null == clazz )
         {
             throw new FactoryException( "Cannot find class [" + className + "] for application data identifier [" + config.getAttribute("identifier") + "]" );
         }
         else
         {
             return new ApplicationData( config.getAttribute( "identifier" ),
                                         clazz );
         }
    }

     private Class importClass(ClassLoader cl, String importText, String className)
     {
         String qualifiedClass = null;
         Class clazz = null;
         if (importText.startsWith("from "))
         {
             importText = converPythonImport(importText);
         }
         //not python
         if (importText.endsWith("*"))
         {
             qualifiedClass = importText.substring(0, importText.indexOf('*'))  + className;
         }
         else if (importText.endsWith(className))
         {
             qualifiedClass = importText;
         }

         if (qualifiedClass != null)
         {
             try
             {
                 clazz = cl.loadClass( qualifiedClass );
             }
             catch ( Exception e )
             {
                 //swallow
             }
         }
         return clazz;
     }

     private String converPythonImport(String packageText)
     {
         int fromIndex = packageText.indexOf("from ");
         int importIndex = packageText.indexOf("import ");
         return packageText.substring(fromIndex  + 5, importIndex).trim()  + "." +
                packageText.substring(importIndex + 7).trim();
     }
}
