package org.drools.semantics.java;

/*
 * $Id: JavaCompiler.java,v 1.7.2.2 2005-04-12 00:24:41 mproctor Exp $
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

import java.io.IOException;
import java.util.HashMap;

import org.codehaus.janino.ByteArrayClassLoader;
import org.codehaus.janino.Scanner;

import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spi.RuleBaseContext;

class JavaCompiler
{
    private static final String[] PARAM_NAMES = new String[]{"tuple", "decls", "drools", "applicationData"};

    public static Object compile(Rule rule,
                                 String className,
                                 Class clazz,
                                 String expression,
                                 String originalExpression,
                                 Declaration[] params) throws IOException,
                                                      CompilationException
    {
        try
        {
            RuleBaseContext ruleBaseContext = rule.getRuleSet( ).getRuleBaseContext( );
            ClassLoader classLoader = (ClassLoader) ruleBaseContext.get( "java-classLoader" );

            if ( classLoader == null )
            {
                ClassLoader cl = (ClassLoader) ruleBaseContext.get( "smf-classLoader" );

                if ( cl == null )
                {
                    cl = Thread.currentThread( ).getContextClassLoader( );
                    ruleBaseContext.put( "smf-classLoader",
                                         cl );
                }

                if ( cl == null )
                {
                    cl = JavaCompiler.class.getClassLoader( );
                    ruleBaseContext.put( "smf-classLoader",
                                         cl );
                }

                classLoader = new ByteArrayClassLoader( new HashMap( ), 
                                                        cl );

                ruleBaseContext.put( "java-classLoader",
                                     classLoader );
            }
            JavaFunctions functions = (JavaFunctions) rule.getRuleSet( ).getFunctions( "java" );
            Class functionsClass = null;

            if ( functions != null )
            {
                functionsClass = functions.getFunctionsClass( );
            }

            return JavaScriptEvaluator.compile( expression,
                                                className,
                                                clazz,
                                                PARAM_NAMES,
                                                params,
                                                rule.getImports( JavaImportEntry.class ),
                                                rule.getApplicationData( ),
                                                functionsClass,
                                                classLoader );
        }
        catch ( Scanner.LocatedException e )
        {
            throw new CompilationException( rule,
                                            originalExpression,
                                            e.getLocation( ) != null ? e.getLocation( ).getLineNumber( ) : -1,
                                            e.getLocation( ) != null ? e.getLocation( ).getColumnNumber( ) : -1,
                                            e.getMessage( ) );
        }
    }
}
