package org.drools.semantics.java;

/*
 * $Id: JavaCondition.java,v 1.1 2004-12-07 13:58:50 simon Exp $
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

import net.janino.Scanner;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spi.Condition;
import org.drools.spi.ConditionException;
import org.drools.spi.KnowledgeHelper;
import org.drools.spi.Tuple;

import javax.naming.ConfigurationException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.List;
import java.util.Map;

/**
 * Java expression semantics <code>Condition</code>.
 *
 * @author <a href="mailto:bob@werken.com">bob@werken.com </a>
 */
public class JavaCondition
    implements
    Serializable,
    Condition
{
    // ------------------------------------------------------------
    // Constants
    // ------------------------------------------------------------

    private static final String[]       SCRIPT_PARAM_NAMES = new String[] { "tuple", "decls", "drools", "applicationData" };

    // ------------------------------------------------------------
    // Instance members
    // ------------------------------------------------------------

    private final String                originalExpression;

    private final Rule                  rule;

    private final String                expression;

    private final Declaration[]         requiredDeclarations;


    private transient ConditionScript   conditionScript;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /**
     * Construct.
     *
     * @param expression The expression.
     * @param rule The rule.
     * @throws ConfigurationException If an error occurs while attempting to perform configuration.
     */
    protected JavaCondition( String expression,
                             Rule rule ) throws Exception
    {
        this.originalExpression = expression;
        this.expression = "return (" + expression + ");";
        this.rule = rule;


        ExprAnalyzer analyzer = new ExprAnalyzer();
        List requiredDecls = analyzer.analyze( expression,
                                               rule.getParameterDeclarations() );

        this.requiredDeclarations = ( Declaration[] ) requiredDecls.toArray( new Declaration[ requiredDecls.size( ) ] );

        this.conditionScript = compile( );
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //     org.drools.spi.Condition
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Determine if the supplied <code>Tuple</code> is allowed by this
     * condition.
     *
     * @param tuple The <code>Tuple</code> to test.
     *
     * @return <code>true</code> if the <code>Tuple</code> passes this
     *         condition, else <code>false</code>.
     *
     * @throws ConditionException if an error occurs during filtering.
     */
    public boolean isAllowed(Tuple tuple) throws ConditionException
    {
        try
        {
            return conditionScript.invoke( tuple,
                                           requiredDeclarations,
                                           new KnowledgeHelper( rule, tuple ),
                                           tuple.getWorkingMemory( ).getApplicationDataMap( ) );
        }
        catch ( Scanner.LocatedException e )
        {
            throw new ConditionException( e,
                                          rule,
                                          originalExpression );
        }
        catch (CompilationException e)
        {
            System.err.println( "[" + e.getText() + "]" );
            throw new ConditionException( e.getMessage(),
                                          e.getRule( ),
                                          e.getText( ) );
        }
        catch (Exception e)
        {
            throw new ConditionException( e,
                                          rule,
                                          originalExpression );
        }
    }

    /**
     * Retrieve the <code>Declaration</code> s required for evaluating the expression.
     *
     * @return The required declarations.
     */
    public Declaration[] getRequiredTupleMembers()
    {
        return this.requiredDeclarations;
    }

    private ConditionScript compile() throws Exception
    {
        return ( ConditionScript ) Interp.compile( this.rule,
                                                   ConditionScript.class,
                                                   this.expression,
                                                   this.originalExpression,
                                                   SCRIPT_PARAM_NAMES,
                                                   this.requiredDeclarations,
                                                   this.rule.getImports( JavaImportEntry.class ),
                                                   rule.getApplicationData( ) );
    }

    // ------------------------------------------------------------

    private void readObject( ObjectInputStream stream ) throws Exception
    {
        stream.defaultReadObject( );

        this.conditionScript = compile( );
    }

    public int hashCode()
    {
        return this.expression.hashCode( );
    }

    public boolean equals( Object object )
    {
        if ( this == object )
        {
            return true;
        }

        if ( object == null || getClass( ) != object.getClass( ) )
        {
            return false;
        }

        return this.expression.equals( ( ( JavaCondition ) object ).expression );
    }

    public String toString()
    {
        return "[Condition: " + originalExpression + "]";
    }

    public static interface ConditionScript
    {
        public boolean invoke( Tuple tuple,
                               Declaration[] decls,
                               KnowledgeHelper drools,
                               Map applicationData ) throws Exception;
    }
}
