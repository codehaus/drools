package org.drools.semantics.java;

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

import net.janino.EvaluatorBase;
import net.janino.Java;
import net.janino.Mod;
import net.janino.Parser;
import net.janino.Scanner;
import net.janino.util.PrimitiveWrapper;

import org.drools.rule.Declaration;
import org.drools.spi.ObjectType;

public class DroolsScriptEvaluator extends EvaluatorBase
{

    private final Method method;

    public Method getMethod()
    {
        return this.method;
    }

    public DroolsScriptEvaluator(String code,
                                 Class interfaceToImplement,
                                 String[] parameterNames,
                                 Declaration[] declarations,
                                 Map applicationData) throws Scanner.ScanException,
                                                     Parser.ParseException,
                                                     Java.CompileException,
                                                     IOException
    {
        super( null );
        Scanner scanner = new Scanner( null, new StringReader( code ) );

        Method[] methods = interfaceToImplement.getDeclaredMethods( );
        if ( methods.length != 1 ) throw new RuntimeException(
                                                               "Interface \""
                                                                                                                                                                                                                                                            + interfaceToImplement
                                                                                                                                                                                                                                                            + "\" must declare exactly one method" );
        Method methodToImplement = methods[0];
        String methodName = methodToImplement.getName( );
        Class[] parameterTypes = methodToImplement.getParameterTypes( );

        if ( parameterNames.length != parameterTypes.length ) throw new RuntimeException(
                                                                                          "Lengths of \"parameterNames\" and \"parameterTypes\" do not match" );

        // Create a temporary compilation unit.
        Java.CompilationUnit compilationUnit = new Java.CompilationUnit(
                                                                         scanner
                                                                                .peek( )
                                                                                .getLocation( )
                                                                                .getFileName( ) );

        // Parse import declarations.
        this.parseImportDeclarations( compilationUnit, scanner );

        // Create class, method and block.
        Java.Block block = this
                               .addClassMethodBlockDeclaration(
                                                                scanner
                                                                       .peek( )
                                                                       .getLocation( ), // location
                                                                compilationUnit, // compilationUnit
                                                                "DroolsConsequence", // className
                                                                null, // optionalExtendedType
                                                                new Class[]{interfaceToImplement}, // implementedTypes
                                                                false, // staticMethod
                                                                methodToImplement
                                                                                 .getReturnType( ), // returnType
                                                                methodName, // methodName
                                                                parameterNames, // parameterNames
                                                                parameterTypes, // parameterTypes
                                                                methodToImplement
                                                                                 .getExceptionTypes( ) // thrownExceptions
                               );

        // Parse block statements.
        Parser parser = new Parser( scanner );

        //block.addStatement(
        Set imports = new HashSet( );
        addDeclarations( scanner, block, declarations, imports );
        addAppData( scanner, block, applicationData, imports );

        Scanner.Location loc = scanner.peek( ).getLocation( );
        Iterator it = imports.iterator( );
        String importString;
        String type;
        List list;
        while ( it.hasNext( ) )
        {
            list = new ArrayList( );
            type = ( String ) it.next( );
            StringTokenizer st = new StringTokenizer( type, "." );
            while ( st.hasMoreTokens( ) )
            {
                list.add( st.nextToken( ) );
            }

            compilationUnit
                           .addSingleTypeImport(
                                                 loc,
                                                 ( String[] ) list
                                                                  .toArray( new String[list
                                                                                           .size( )] ) );
        }

        while ( !scanner.peek( ).isEOF( ) )
        {
            block.addStatement( parser.parseBlockStatement( block ) );
        }

        // Compile and load it.
        Class c;
        try
        {
            c = this.compileAndLoad( compilationUnit, Java.DEBUGGING_SOURCE | Java.DEBUGGING_LINES, "DroolsConsequence" );
        }
        catch ( ClassNotFoundException e )
        {
            throw new RuntimeException( );
        }

        // Find script method by name.
        try
        {
            this.method = c.getMethod( methodName, parameterTypes );
        }
        catch ( NoSuchMethodException ex )
        {
            throw new RuntimeException( ex.toString( ) );
        }

        if ( this.method == null ) throw new RuntimeException( "Method \""
                                                               + methodName
                                                               + "\" not found" );
    }

    private void addAppData(Scanner scanner,
                            Java.Block block,
                            Map appData,
                            Set imports) //Declaration[] declarations)
    {
        Set keys = appData.keySet( );
        Iterator it = keys.iterator( );
        String key;
        Object object;
        String type;
        int nestedClassPosition;

        while ( it.hasNext( ) )
        {
            key = ( String ) it.next( );
            object = appData.get( key );

            type = object.getClass( ).getName( );
            nestedClassPosition = type.indexOf( '$' );

            if ( nestedClassPosition != -1 )
            {
                type = type.substring( 0, nestedClassPosition );
            }

            imports.add( type );

            Scanner.Location loc = scanner.peek( ).getLocation( );

            Java.VariableDeclarator[] variables = new Java.VariableDeclarator[]{ // variableDeclarators
            new Java.VariableDeclarator(
                                         loc, // location
                                         key, // name
                                         0, // brackets
                                         new Java.Cast(
                                                        // optionalInitializer
                                                        loc, // location
                                                        this
                                                            .classToType(
                                                                          loc,
                                                                          object
                                                                                .getClass( ) ), // targetType
                                                        new Java.MethodInvocation(
                                                                                   // value
                                                                                   loc, // location
                                                                                   ( Java.Scope ) block, // enclosingScope
                                                                                   new Java.AmbiguousName(
                                                                                                           // optionalTarget
                                                                                                           loc,
                                                                                                           ( Java.Scope ) block,
                                                                                                           new String[]{"applicationData"} ),
                                                                                   "get", // methodName
                                                                                   new Java.Rvalue[]{ // arguments
                                                                                   new Java.Literal(
                                                                                                     scanner.new StringLiteralToken(
                                                                                                                                     key ) )} ) ) )};

            block
                 .addStatement( new Java.LocalVariableDeclarationStatement(
                                                                            loc, // location
                                                                            block, // declaringBock
                                                                            Mod.FINAL, // modifiers
                                                                            this
                                                                                .classToType(
                                                                                              loc,
                                                                                              object
                                                                                                    .getClass( ) ), // type
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
            clazz = ( ( ClassObjectType ) objectType ).getType( );

            type = clazz.getName( );
            nestedClassPosition = type.indexOf( '$' );

            if ( nestedClassPosition != -1 )
            {
                type = type.substring( 0, nestedClassPosition );
            }

            imports.add( type );

            Scanner.Location loc = scanner.peek( ).getLocation( );

            Java.VariableDeclarator[] variables = new Java.VariableDeclarator[]{ // variableDeclarators
            new Java.VariableDeclarator(
                                         loc, // location
                                         identifier, // name
                                         0, // brackets
                                         new Java.Cast(
                                                        // optionalInitializer
                                                        loc, // location
                                                        this
                                                            .classToType( loc,
                                                                          clazz ), // targetType
                                                        new Java.MethodInvocation(
                                                                                   // value
                                                                                   loc, // location
                                                                                   ( Java.Scope ) block, // enclosingScope
                                                                                   new Java.AmbiguousName(
                                                                                                           // optionalTarget
                                                                                                           loc,
                                                                                                           ( Java.Scope ) block,
                                                                                                           new String[]{"tuple"} ),
                                                                                   "get", // methodName
                                                                                   new Java.Rvalue[]{new Java.ArrayAccessExpression(
                                                                                                                                     loc,
                                                                                                                                     new Java.AmbiguousName(
                                                                                                                                                             loc,
                                                                                                                                                             ( Java.Scope ) block,
                                                                                                                                                             new String[]{"decls"} ),
                                                                                                                                     new Java.ConstantValue(
                                                                                                                                                             loc,
                                                                                                                                                             PrimitiveWrapper
                                                                                                                                                                             .wrap( i ) ) )} ) ) )};

            block
                 .addStatement( new Java.LocalVariableDeclarationStatement(
                                                                            loc, // location
                                                                            block, // declaringBock
                                                                            Mod.FINAL, // modifiers
                                                                            this
                                                                                .classToType(
                                                                                              loc,
                                                                                              clazz ), // type
                                                                            variables ) );
        }
    }

    public static Object compile(String block,
                                 Class interfaceToImplement,
                                 String[] parameterNames,
                                 Declaration[] declarations,
                                 Map applicationData) throws Java.CompileException,
                                                     Parser.ParseException,
                                                     Scanner.ScanException,
                                                     IOException
    {
        DroolsScriptEvaluator scriptEvaluator = new DroolsScriptEvaluator(
                                                                           block,
                                                                           interfaceToImplement,
                                                                           parameterNames,
                                                                           declarations,
                                                                           applicationData );

        try
        {
            return scriptEvaluator.getMethod( ).getDeclaringClass( )
                                  .newInstance( );
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