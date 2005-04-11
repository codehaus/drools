package org.drools.semantics.java;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.StringTokenizer;

import net.janino.ByteArrayClassLoader;
import net.janino.DebuggingInformation;
import net.janino.EvaluatorBase;
import net.janino.Java;
import net.janino.Parser;
import net.janino.Scanner;
import net.janino.Scanner.Location;
import net.janino.Java.CompileException;
import net.janino.Parser.ParseException;
import net.janino.Scanner.ScanException;
import org.drools.rule.RuleSet;
import org.drools.spi.Functions;
import org.drools.spi.RuleBaseContext;

/**
 * Class body evaluator that pays attention to imports defined outside the class body.
 * Made from <code>net.janino.ClassBodyEvaluator</code> and
 * <code>org.drools.semantics.java.JavaScriptEvaluator</code>.
 *
 * @author kdx
 */
public class ImporterClassBodyEvaluator extends EvaluatorBase
{

    private static final String DEFAULT_CLASS_NAME = "SC";

    private final Class clazz;

    /**
     * Construct.
     *
     * @param imports <code>Set&lt;String&gt;</code> of imported java classes and packages.
     * @param scanner The lexer.
     * @param classLoader Class loader for resolving other classes referred to
     * by the currently constructed class.
     * @throws IOException
     * @throws ScanException
     * @throws ParseException
     * @throws CompileException
     */
    public ImporterClassBodyEvaluator(Set imports, Scanner scanner, ClassLoader classLoader) throws ScanException, IOException, CompileException, ParseException
    {
        super(classLoader);

        String className = DEFAULT_CLASS_NAME;
        Class optionalExtendedType = (Class)null;
        Class [] implementedTypes = new Class[0];

        Java.CompilationUnit compilationUnit = new Java.CompilationUnit(scanner.peek().getLocation().getFileName());

        // Parse import declarations.
        Parser parser = new Parser(scanner);
        this.parseImportDeclarations(compilationUnit, scanner);

        // The difference from plain ClassBodyEvaluator: add extra imports.
        Scanner.Location loc = scanner.peek( ).getLocation( );
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
                compilationUnit.addTypeImportOnDemand( (String[]) list.toArray( new String[list.size( )] ) );
            }
            else
            {
                compilationUnit.addSingleTypeImport( loc,
                                                     (String[]) list.toArray( new String[list.size( )] ) );
            }
        }

        // Add class declaration.
        Java.ClassDeclaration cd = this.addPackageMemberClassDeclaration(
                                                                         scanner.peek().getLocation(),
                                                                         compilationUnit,
                                                                         className, optionalExtendedType, implementedTypes
                                                                         );

        // Parse class body declarations (member declarations) until EOF.
        while (!scanner.peek().isEOF()) {
            parser.parseClassBodyDeclaration(cd);
        }

        // Compile and load it.
        try {
            this.clazz = this.compileAndLoad(
                                             compilationUnit,                                             // compilationUnit
                                             DebuggingInformation.SOURCE.add(DebuggingInformation.LINES), // debuggingInformation
                                             className                                                    // className
                                             );
        } catch (ClassNotFoundException e) {
            throw new RuntimeException();
        }

    }

    public Class evaluate()
    {
        return clazz;
    }

}
