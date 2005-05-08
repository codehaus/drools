package org.drools.semantics.java;

/*
 * $Id: JavaScriptEvaluator.java,v 1.10 2005-05-08 19:54:48 mproctor Exp $
 *
 * Copyright 2004-2005 (C) The Werken Company. All Rights Reserved.
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
import java.io.StringReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.StringTokenizer;

import org.codehaus.janino.DebuggingInformation;
import org.codehaus.janino.EvaluatorBase;
import org.codehaus.janino.Java;
import org.codehaus.janino.Mod;
import org.codehaus.janino.Parser;
import org.codehaus.janino.Scanner;
import org.codehaus.janino.Location;
import org.codehaus.janino.Java.CompileException;
import org.codehaus.janino.Parser.ParseException;
import org.codehaus.janino.Scanner.ScanException;
import org.codehaus.janino.util.PrimitiveWrapper;

import org.drools.rule.Declaration;
import org.drools.semantics.base.ClassObjectType;
import org.drools.spi.Importer;
import org.drools.spi.ObjectType;

public class JavaScriptEvaluator extends EvaluatorBase
{

    private final Method method;

    public Method getMethod()
    {
        return this.method;
    }

    public JavaScriptEvaluator(String code,
                               String className,
                               Class interfaceToImplement,
                               String[] parameterNames,
                               Declaration[] declarations,
                               Importer importer,
                               Map applicationData,
                               Class baseClass,
                               ClassLoader classLoader) throws Scanner.ScanException,
                                                     Parser.ParseException,
                                                     Java.CompileException,
                                                     IOException,
                                                     ClassNotFoundException
    {
        super( classLoader );
			
        Scanner scanner = new Scanner( null,
                                       new StringReader( code ) );

        Method[] methods = interfaceToImplement.getDeclaredMethods( );
        if ( methods.length != 1 ) throw new RuntimeException( "Interface \"" + interfaceToImplement + "\" must declare exactly one method" );
        Method methodToImplement = methods[0];
        String methodName = methodToImplement.getName( );
        Class[] parameterTypes = methodToImplement.getParameterTypes( );

        if ( parameterNames.length != parameterTypes.length ) throw new RuntimeException( "Lengths of \"parameterNames\" and \"parameterTypes\" do not match" );

        // Create a temporary compilation unit.
        Java.CompilationUnit compilationUnit = new Java.CompilationUnit( scanner.peek( ).getLocation( ).getFileName( ) );

        // Parse import declarations.
        this.parseImportDeclarations( compilationUnit,
                                      scanner );

        // Create class, method and block.
        Java.Block block = this.addClassMethodBlockDeclaration( scanner.peek( ).getLocation( ), // location
                                                                compilationUnit, // compilationUnit
                                                                className, // className
                                                                baseClass, // optionalExtendedType
                                                                new Class[]{interfaceToImplement}, // implementedTypes
                                                                false, // staticMethod
                                                                methodToImplement.getReturnType( ), // returnType
                                                                methodName, // methodName
                                                                parameterNames, // parameterNames
                                                                parameterTypes, // parameterTypes
                                                                methodToImplement.getExceptionTypes( ) );// thrownExceptions
        
        //make a copy as we will be adding app data declaration imports
        //but do not want to feed that back into the main import cache
        Set imports = new HashSet( importer.getImports( ) );

        // Parse block statements.
        Parser parser = new Parser( scanner );

        // block.addStatement(
        
        addDeclarations( scanner,
                         block,
                         declarations,
                         imports );
        addAppData( scanner,
                    block,
                    applicationData,
                    imports);

        Location loc = scanner.peek( ).getLocation( );
        
        
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
        
        while ( !scanner.peek( ).isEOF( ) )
        {
            block.addStatement( parser.parseBlockStatement( block ) );
        }

        // do not remove comments, usefull for debugging
        // UnparseVisitor.unparse(compilationUnit, new BufferedWriter( new
        // OutputStreamWriter(System.err)));

        // Compile and load it.
        Class c;
        //try
        //{
            c = this.compileAndLoad( compilationUnit,
                                     DebuggingInformation.ALL,
                                     className );
        //}
        //catch ( Exception e )
        //{
            //e.printStackTrace();
            //throw new RuntimeException( );
        //}

        // Find script method by name.
        try
        {
            this.method = c.getMethod( methodName,
                                       parameterTypes );
        }
        catch ( NoSuchMethodException ex )
        {
            throw new RuntimeException( ex.toString( ) );
        }

        if ( this.method == null ) throw new RuntimeException( "Method \"" + methodName + "\" not found" );
    }

    private void addAppData(Scanner scanner,
                            Java.Block block,
                            Map appData,
                            Set imports )
    {
        Set keys = appData.keySet( );
        Iterator it = keys.iterator( );
        String key;
        Class clazz;
        String type;
        int nestedClassPosition;

        while ( it.hasNext( ) )
        {
            key = (String) it.next( );
            clazz = (Class) appData.get( key );

            type = clazz.getName( );
            nestedClassPosition = type.indexOf( '$' );

            if ( nestedClassPosition != -1 )
            {
                type = type.substring( 0,
                                       nestedClassPosition );
            }

            imports.add( type );

            Location loc = scanner.peek( ).getLocation( );

            Java.VariableDeclarator[] variables = new Java.VariableDeclarator[]{ // variableDeclarators
            new Java.VariableDeclarator( loc, // location
                                         key, // name
                                         0, // brackets
                                         new Java.Cast( loc, // location
                                                        this.classToType( loc,
                                                                          clazz ), // targetType
                                                        new Java.MethodInvocation( loc, // location
                                                                                   block, // enclosingScope
                                                                                   new Java.AmbiguousName( loc,
                                                                                                           block,
                                                                                                           new String[]{"applicationData"} ),
                                                                                   "get", // methodName
                                                                                   new Java.Rvalue[]{new Java.Literal( loc, 
                                                                                                                       key )} ) ) )};

            block.addStatement( new Java.LocalVariableDeclarationStatement( loc, // location
                                                                            block, // declaringBock
                                                                            Mod.FINAL, // modifiers
                                                                            this.classToType( loc,
                                                                                              clazz ), // type
                                                                            variables ) );
        }        
    }

    private void addDeclarations(Scanner scanner,
                                 Java.Block block,
                                 Declaration[] declarations,
                                 Set imports)
    {
        ObjectType objectType;
        Class clazz;
        String identifier;
        Declaration declaration;

        String type;
        int nestedClassPosition;

        for ( int i = 0; i < declarations.length; i++ )
        {
            declaration = declarations[i];
            identifier = declaration.getIdentifier( );
            objectType = declaration.getObjectType( );

            clazz = ((ClassObjectType) objectType).getType( );

            type = clazz.getName( );
            nestedClassPosition = type.indexOf( '$' );

            if ( nestedClassPosition != -1 )
            {
                type = type.substring( 0,
                                       nestedClassPosition );
            }

            imports.add( type );

            Location loc = scanner.peek( ).getLocation( );

            Java.VariableDeclarator[] variables = new Java.VariableDeclarator[]{ // variableDeclarators
            new Java.VariableDeclarator( loc, // location
                                         identifier, // name
                                         0, // brackets
                                         new Java.Cast( loc, // location
                                                        this.classToType( loc,
                                                                          clazz ), // targetType
                                                        new Java.MethodInvocation( loc, // location
                                                                                   block, // enclosingScope
                                                                                   new Java.AmbiguousName( loc,
                                                                                                           block,
                                                                                                           new String[]{"tuple"} ),
                                                                                   "get", // methodName
                                                                                   new Java.Rvalue[]{new Java.ArrayAccessExpression( loc,
                                                                                                                                     new Java.AmbiguousName( loc,
                                                                                                                                                             block,
                                                                                                                                                             new String[]{"decls"} ),
                                                                                                                                     new Java.ConstantValue( loc,
                                                                                                                                                             PrimitiveWrapper.wrap( i ) ) )} ) ) )};

            block.addStatement( new Java.LocalVariableDeclarationStatement( loc, // location
                                                                            block, // declaringBock
                                                                            Mod.FINAL, // modifiers
                                                                            this.classToType( loc,
                                                                                              clazz ), // type
                                                                            variables ) );
        }

    }

    public static Object compile(String block,
                                 String className,
                                 Class interfaceToImplement,
                                 String[] parameterNames,
                                 Declaration[] declarations,
                                 Importer importer,
                                 Map applicationData,
                                 Class baseClass,
                                 ClassLoader classLoader) throws ScanException, ParseException, CompileException, IOException, ClassNotFoundException 
    {
        JavaScriptEvaluator scriptEvaluator = new JavaScriptEvaluator( block,
                                                                       className,
                                                                       interfaceToImplement,
                                                                       parameterNames,
                                                                       declarations,
                                                                       importer,
                                                                       applicationData,
                                                                       baseClass,
                                                                       classLoader );

        try
        {
            return scriptEvaluator.getMethod( ).getDeclaringClass( ).newInstance( );
        }
        catch ( InstantiationException e )
        {
            // SNO - Declared class is always non-abstract.
            throw new RuntimeException( e.toString( ) );
        }
        catch ( IllegalAccessException e )
        {
            // SNO - interface methods are always PUBLIC.
            throw new RuntimeException( e.toString( ) );
        }
    }
}
