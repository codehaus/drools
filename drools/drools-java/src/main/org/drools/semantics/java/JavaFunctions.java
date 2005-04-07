package org.drools.semantics.java;

/*
 * $Id: JavaFunctions.java,v 1.5 2005-04-07 17:42:14 mproctor Exp $
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

import net.janino.ByteArrayClassLoader;
import net.janino.ClassBodyEvaluator;
import net.janino.Scanner;
import net.janino.Java.CompileException;
import net.janino.Parser.ParseException;
import net.janino.Scanner.ScanException;

import org.drools.rule.RuleSet;
import org.drools.spi.Functions;
import org.drools.spi.RuleBaseContext;

/**
 * Python block semantics <code>Consequence</code>.
 * 
 * @author <a href="mailto:bob@eng.werken.com">bob mcwhirter </a>
 * 
 * @version $Id: JavaFunctions.java,v 1.5 2005-04-07 17:42:14 mproctor Exp $
 */
public class JavaFunctions
    implements
    Functions
{

    private String              text;

    private transient Class     functionsClass;

    private RuleSet             ruleSet;

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
     * @throws IOException
     * @throws ScanException
     * @throws ParseException
     * @throws CompileException
     */
    public JavaFunctions(RuleSet ruleSet,
                         String text) throws ScanException,
                                     IOException,
                                     CompileException,
                                     ParseException
    {
        this.text = text;
        this.ruleSet = ruleSet;

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

            classLoader = new ByteArrayClassLoader( cl );

            ruleBaseContext.put( "java-classLoader",
                                 classLoader );
        }

        ClassBodyEvaluator classBody = new ClassBodyEvaluator( new Scanner( null,
                                                                            new java.io.StringReader( this.text ) ),
                                                               classLoader );
        this.functionsClass = classBody.evaluate( );
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

    public Class getFunctionsClass()
    {
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
    /*
     * private Script compile(Rule rule) throws Exception { Set imports = new
     * HashSet( ); if ( rule.getImports( ) != null ) { Iterator it =
     * rule.getImports( ).iterator( ); ImportEntry importEntry; while (
     * it.hasNext( ) ) { importEntry = (ImportEntry) it.next( ); if (
     * importEntry instanceof JavaImportEntry ) { imports.add(
     * importEntry.getImportEntry( ) ); } } }
     * 
     * return (Script) Interp.compile( rule, Script.class, this.block,
     * this.block, SCRIPT_PARAM_NAMES, this.declarations, imports,
     * rule.getApplicationData( ) ); }
     */
}
