package org.drools.semantics.java;

/*
 * $Id: ImporterClassBodyEvaluator.java,v 1.5 2005-05-08 19:54:48 mproctor Exp $
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import org.codehaus.janino.DebuggingInformation;
import org.codehaus.janino.EvaluatorBase;
import org.codehaus.janino.Java;
import org.codehaus.janino.Location;
import org.codehaus.janino.Parser;
import org.codehaus.janino.Scanner;
import org.codehaus.janino.Java.CompileException;
import org.codehaus.janino.Parser.ParseException;
import org.codehaus.janino.Scanner.ScanException;
import org.drools.spi.Importer;

/**
 * Class body evaluator that pays attention to imports defined outside the class
 * body. Made from <code>net.janino.ClassBodyEvaluator</code> and
 * <code>org.drools.semantics.java.JavaScriptEvaluator</code>.
 * 
 * @author kdx
 */
public class ImporterClassBodyEvaluator extends EvaluatorBase
{
    private final Class clazz;

    /**
     * Construct.
     * 
     * @param imports
     *        <code>Set&lt;String&gt;</code> of imported java classes and
     *        packages.
     * @param scanner
     *        The lexer.
     * @param classLoader
     *        Class loader for resolving other classes referred to by the
     *        currently constructed class.
     * @throws IOException
     * @throws ScanException
     * @throws ParseException
     * @throws CompileException
     */
    public ImporterClassBodyEvaluator(Importer importer,
                                      String className,
                                      Scanner scanner,
                                      ClassLoader classLoader) throws ScanException,
                                                              IOException,
                                                              CompileException,
                                                              ParseException,
                                                              ClassNotFoundException
    {
        super( classLoader );

        Class optionalExtendedType = null;
        Class[] implementedTypes = new Class[0];

        Java.CompilationUnit compilationUnit = new Java.CompilationUnit( scanner.peek( ).getLocation( ).getFileName( ) );

        // Parse import declarations.
        Parser parser = new Parser( scanner );
        this.parseImportDeclarations( compilationUnit,
                                      scanner );

        // The difference from plain ClassBodyEvaluator: add extra imports.
        Location loc = scanner.peek( ).getLocation( );
        Set imports = importer.getImports( );
        Iterator it = imports.iterator( );
        String type;
        List list;
        StringTokenizer st;
        String token;
        boolean importOnDemand;
        while ( it.hasNext( ) )
        {
            importOnDemand = false;
            list = new ArrayList( );
            type = (String) it.next( );
            st = new StringTokenizer( type,
                                      "." );
            while ( st.hasMoreTokens( ) )
            {
                token = st.nextToken( );
                if ( !token.equals( "*" ) )
                {
                    list.add( token );
                }
                else
                {
                    importOnDemand = true;
                }
            }
            if ( importOnDemand )
            {
                compilationUnit.addImportDeclaration( new Java.TypeImportOnDemandDeclaration( loc,
                                                                                              (String[]) list.toArray( new String[list.size( )] ) ) );
            }
            else
            {
                compilationUnit.addImportDeclaration( new Java.SingleTypeImportDeclaration( loc,
                                                                                            (String[]) list.toArray( new String[list.size( )] ) ) );
            }
        }

        // Add class declaration.
        Java.ClassDeclaration cd = this.addPackageMemberClassDeclaration( scanner.peek( ).getLocation( ),
                                                                          compilationUnit,
                                                                          className,
                                                                          optionalExtendedType,
                                                                          implementedTypes );

        // Parse class body declarations (member declarations) until EOF.
        while ( !scanner.peek( ).isEOF( ) )
        {
            parser.parseClassBodyDeclaration( cd );
        }

        this.clazz = this.compileAndLoad( compilationUnit, // compilationUnit
                                          DebuggingInformation.SOURCE.add( DebuggingInformation.LINES ), // debuggingInformation
                                          className );
    }

    public Class evaluate()
    {
        return clazz;
    }
}
