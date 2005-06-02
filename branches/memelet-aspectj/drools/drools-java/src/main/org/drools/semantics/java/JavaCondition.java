package org.drools.semantics.java;

/*
 * $Id: JavaCondition.java,v 1.14 2005-05-08 19:54:48 mproctor Exp $
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

import java.io.Serializable;
import java.util.List;
import java.util.Map;

import javax.naming.ConfigurationException;

import org.codehaus.janino.Scanner;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spi.Condition;
import org.drools.spi.ConditionException;
import org.drools.spi.DefaultKnowledgeHelper;
import org.drools.spi.KnowledgeHelper;
import org.drools.spi.Tuple;

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
    // Instance members
    // ------------------------------------------------------------

    private final String        originalExpression;

    private final Rule          rule;

    private final String        expression;

    private final Declaration[] requiredDeclarations;

    private final String        className;

    private transient Script    script;

    // ------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------

    /**
     * Construct.
     *
     * @param expression
     *            The expression.
     * @param rule
     *            The rule.
     * @throws ConfigurationException
     *             If an error occurs while attempting to perform configuration.
     */
    protected JavaCondition(Rule rule,
                            int id,
                            String expression) throws Exception
    {
        this.originalExpression = expression;
        this.expression = "return (" + expression + ");";
        this.rule = rule;

        JavaExprAnalyzer analyzer = new JavaExprAnalyzer( );
        List requiredDecls = analyzer.analyze( expression,
                                               rule.getParameterDeclarations( ) );

        this.requiredDeclarations = (Declaration[]) requiredDecls.toArray( new Declaration[requiredDecls.size( )] );

        this.className = "Condition_" + id;
        this.script = compile( );
    }

    // ------------------------------------------------------------
    // Instance methods
    // ------------------------------------------------------------

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // org.drools.spi.Condition
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Determine if the supplied <code>Tuple</code> is allowed by this
     * condition.
     *
     * @param tuple
     *            The <code>Tuple</code> to test.
     *
     * @return <code>true</code> if the <code>Tuple</code> passes this
     *         condition, else <code>false</code>.
     *
     * @throws ConditionException
     *             if an error occurs during filtering.
     */
    public boolean isAllowed(Tuple tuple) throws ConditionException
    {
        try
        {
            if ( this.script == null )
            {
                this.script = compile( );
            }
            return this.script.invoke( tuple,
                                       requiredDeclarations,
                                       new DefaultKnowledgeHelper( rule,
                                                                   tuple ),
                                       tuple.getWorkingMemory( ).getApplicationDataMap( ) );
        }
        catch ( Scanner.LocatedException e )
        {
            throw new ConditionException( e,
                                          this.rule,
                                          this.originalExpression );
        }
        catch ( CompilationException e )
        {
            throw new ConditionException( e.getMessage( ),
                                          e.getRule( ),
                                          e.getText( ) );
        }
        catch ( Exception e )
        {
            throw new ConditionException( e,
                                          this.rule,
                                          this.originalExpression );
        }
    }

    /**
     * Retrieve the <code>Declaration</code> s required for evaluating the
     * expression.
     *
     * @return The required declarations.
     */
    public Declaration[] getRequiredTupleMembers()
    {
        return this.requiredDeclarations;
    }

    private Script compile() throws Exception
    {
        return (Script) JavaCompiler.compile( this.rule,
                                              this.className,
                                              Script.class,
                                              this.expression,
                                              this.originalExpression,
                                              this.requiredDeclarations );
    }

    // ------------------------------------------------------------

    public int hashCode()
    {
        return this.originalExpression.hashCode( );
    }

    public boolean equals(Object object)
    {
        if ( this == object )
        {
            return true;
        }

        if ( object == null || getClass( ) != object.getClass( ) )
        {
            return false;
        }

        return this.originalExpression.equals( ((JavaCondition) object).originalExpression );
    }

    public String toString()
    {
        return "[Condition: " + this.originalExpression + "]";
    }

    public static interface Script
    {
        public boolean invoke(Tuple tuple,
                              Declaration[] decls,
                              KnowledgeHelper drools,
                              Map applicationData) throws Exception;
    }
}
