package org.drools.semantics.java;

/*
 * $Id: JavaFunctions.java,v 1.9 2005-05-08 19:54:48 mproctor Exp $
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
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.codehaus.janino.ByteArrayClassLoader;
import org.codehaus.janino.Scanner;
import org.codehaus.janino.Java.CompileException;
import org.codehaus.janino.Parser.ParseException;
import org.codehaus.janino.Scanner.ScanException;
import org.drools.rule.RuleSet;
import org.drools.spi.Functions;
import org.drools.spi.ImportEntry;
import org.drools.spi.Importer;
import org.drools.spi.RuleBaseContext;

/**
 * Python block semantics <code>Consequence</code>.
 *
 * @author <a href="mailto:bob@eng.werken.com">bob mcwhirter </a>
 */
public class JavaFunctions
    implements
    Functions
{

    private String              text;

    private transient Class     functionsClass;

    private RuleSet             ruleSet;

    private String              className;

    // private

    /** The line separator system property ("\n" on UNIX). */
    private static final String LINE_SEPARATOR = System.getProperty( "line.separator" );
    // ------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------

    /**
     * Construct.
     *
     * @param classLoader
     *
     * @param text
     *            The block text.
     * @throws ClassNotFoundException
     * @throws IOException
     * @throws CompilationException
     * @throws IOException
     * @throws ScanException
     * @throws ParseException
     * @throws CompileException
     */
    public JavaFunctions(RuleSet ruleSet,
                         int id,
                         String text) throws CompilationException, IOException, ClassNotFoundException
    {
        this.text = text;
        this.ruleSet = ruleSet;

        this.className = "Function_" + id;

        compile();
    }

    private void compile() throws IOException, CompilationException, ClassNotFoundException
    {
        RuleBaseContext ruleBaseContext = ruleSet.getRuleBaseContext( );
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
        }

        Set imports = new HashSet();
        Importer importer = ruleSet.getImporter();

        try
        {
            ImporterClassBodyEvaluator classBody = new ImporterClassBodyEvaluator(importer, this.className, new Scanner(null, new java.io.StringReader(this.text)), classLoader);
            this.functionsClass = classBody.evaluate();
        }
        catch ( Scanner.LocatedException e )
        {
            throw new CompilationException( ruleSet,
                                            null,
                                            this.text,
                                            e.getLocation( ) != null ? e.getLocation( ).getLineNumber( ) : -1,
                                            e.getLocation( ) != null ? e.getLocation( ).getColumnNumber( ) : -1,
                                            e.getMessage( ) );
        }

        ruleBaseContext.put( "java-classLoader",
                             this.functionsClass.getClassLoader() );
    }

    /*
     * (non-Javadoc)
     *
     * @see org.drools.spi.Functions#getText()
     */
    public String getText()
    {
        return this.text;
    }

    public Class getFunctionsClass() throws CompilationException, IOException, ClassNotFoundException
    {
        if (functionsClass == null)
        {
            compile();
        }
        return functionsClass;
    }

    /*
     * (non-Javadoc)
     *
     * @see org.drools.spi.Functions#getSemantic()
     */
    public String getSemantic()
    {
        return "java";
    }

}
