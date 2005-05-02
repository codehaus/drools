package org.drools.semantics.java;

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
    private final Class         clazz;

    /**
     * Construct.
     * 
     * @param imports
     *            <code>Set&lt;String&gt;</code> of imported java classes and
     *            packages.
     * @param scanner
     *            The lexer.
     * @param classLoader
     *            Class loader for resolving other classes referred to by the
     *            currently constructed class.
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
                                                              ParseException
    {
        super( classLoader );

        Class optionalExtendedType = (Class) null;
        Class[] implementedTypes = new Class[0];

        Java.CompilationUnit compilationUnit = new Java.CompilationUnit( scanner.peek( ).getLocation( ).getFileName( ) );

        // Parse import declarations.
        Parser parser = new Parser( scanner );
        this.parseImportDeclarations( compilationUnit,
                                      scanner );

        // The difference from plain ClassBodyEvaluator: add extra imports.
        Location loc = scanner.peek( ).getLocation( );
        Set imports = importer.getImports( JavaImportEntry.class );
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

        // Compile and load it.
        try
        {
            this.clazz = this.compileAndLoad( compilationUnit, // compilationUnit
                                              DebuggingInformation.SOURCE.add( DebuggingInformation.LINES ), // debuggingInformation
                                              className );
        }
        catch ( ClassNotFoundException e )
        {
            throw new RuntimeException( );
        }

    }

    public Class evaluate()
    {
        return clazz;
    }

}
