package org.drools.semantics.java;

/*
 * $Id: Expr.java,v 1.32 2004-11-29 11:37:45 simon Exp $
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

import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spi.ImportEntry;
import org.drools.spi.KnowledgeHelper;
import org.drools.spi.Tuple;

import javax.naming.ConfigurationException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Base class for expression-based Java semantic components.
 *
 * @see ExprCondition
 *
 * @author <a href="mailto:bob@eng.werken.com">bob mcwhirter </a>
 */
public class Expr
    implements
    Serializable
{
    // ------------------------------------------------------------
    // Instance members
    // ------------------------------------------------------------

    private final String originalExpression;

    private final Rule rule;

    private static final String[] SCRIPT_PARAM_NAMES = new String[]{"tuple", "decls", "drools", "applicationData"};

    /** Required declarations. */
    private final Declaration[] requiredDecls;

    private final String expr;

    private transient ConditionScript conditionScript;

    // protected Expr() throws Exception
    // {
    // super();
    // }

    // ------------------------------------------------------------
    // Constants
    // ------------------------------------------------------------

    /**
     * Construct.
     *
     * @param expr The expression.
     * @param rule The rule.
     *
     * @throws ConfigurationException If an error occurs while attempting to perform configuration.
     */
    protected Expr( String expr,
                    Rule rule) throws Exception
    {
        this.expr = "return (" + expr + ");";
        this.rule = rule;

        this.originalExpression = expr;
        List requiredDecls = analyze( expr,
                                      rule.getParameterDeclarations( ) );
        this.requiredDecls = (Declaration[]) requiredDecls.toArray( new Declaration[requiredDecls.size( )] );

        this.conditionScript = compile( );
    }

    private void readObject(ObjectInputStream s) throws Exception
    {
        s.defaultReadObject();

        this.conditionScript = compile( );
    }

    /**
     * Retrieve the expression.
     *
     * @return The expression.
     */
    public String getExpression()
    {
        return this.originalExpression;
    }

    public boolean evaluateCondition(Tuple tuple) throws Exception
    {
        return conditionScript.invoke( tuple,
                                       this.requiredDecls,
                                       new KnowledgeHelper( this.rule,
                                                            tuple ),
                                       tuple.getWorkingMemory( ).getApplicationDataMap( ) );

    }

    protected List analyze(String expr,
                           List available) throws Exception
    {
        ExprAnalyzer analyzer = new ExprAnalyzer( );

        return analyzer.analyze( expr,
                                 available );
    }

    /**
     * Retrieve the <code>Declaration</code> s required for evaluating the
     * expression.
     *
     * @return The required declarations.
     */
    public Declaration[] getRequiredTupleMembers()
    {
        return this.requiredDecls;
    }

    private ConditionScript compile() throws Exception
    {
        Set imports = new HashSet( );
        if ( rule.getImports( ) != null )
        {
            Iterator it = rule.getImports( ).iterator( );
            ImportEntry importEntry;
            while ( it.hasNext( ) )
            {
                importEntry = (ImportEntry) it.next( );
                if ( importEntry instanceof JavaImportEntry )
                {
                    imports.add( importEntry.getImportEntry( ) );
                }
            }
        }

        return (ConditionScript) Interp.compile( this.rule,
                                                 ConditionScript.class,
                                                 this.expr,
                                                 this.originalExpression,
                                                 SCRIPT_PARAM_NAMES,
                                                 this.requiredDecls,
                                                 imports,
                                                 rule.getApplicationData( ) );
    }

    public interface ConditionScript
    {
        public boolean invoke(Tuple tuple,
                              Declaration[] decls,
                              KnowledgeHelper drools,
                              Map applicationData) throws Exception;
    }

    protected Rule getRule()
    {
        return this.rule;
    }
}
