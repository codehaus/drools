package org.drools.semantics.java;

/*
 * $Id: JavaCondition.java,v 1.15 2005-11-10 05:10:08 mproctor Exp $
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

import javax.naming.ConfigurationException;

import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.smf.ConditionInvoker;
import org.drools.smf.Invoker;
import org.drools.smf.SemanticCondition;
import org.drools.smf.SemanticRuleCompiler;
import org.drools.spi.Condition;
import org.drools.spi.ConditionException;
import org.drools.spi.Tuple;

/**
 * Java expression semantics <code>Condition</code>.
 * 
 * @author <a href="mailto:bob@werken.com">bob@werken.com </a>
 */
public class JavaCondition
    implements
    Serializable,
    Condition,
    SemanticCondition
{
    // ------------------------------------------------------------
    // Instance members
    // ------------------------------------------------------------
    protected final String        semanticType    = "java";

    protected final String        name;

    protected final String        expression;

    protected final Rule          rule;

    protected final Declaration[] requiredDeclarations;

    protected ConditionInvoker    conditionInvoker;

    protected final String        thrownException = "java.lang.Exception";

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
    protected JavaCondition(String name,
                            String expression,
                            Declaration[] requiredDeclarations,
                            Rule rule) throws Exception
    {
        this.name = name;
        this.expression = expression;
        this.requiredDeclarations = requiredDeclarations;
        this.rule = rule;
    }

    public String getSemanticType()
    {
        return semanticType;
    }

    public boolean isExceptionThrown()
    {
        return true;
    }

    public String getThrownException()
    {
        return this.thrownException;
    }

    public String getName()
    {
        return this.name;
    }

    public String getText()
    {
        return this.expression;
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

    public SemanticRuleCompiler getSemanticRuleCompiler()
    {
        return JavaSemanticRuleCompiler.getInstance();
    }

    public void setInvoker(Invoker invoker)
    {
        this.conditionInvoker = (ConditionInvoker) invoker;
    }

    /**
     * Determine if the supplied <code>Tuple</code> is allowed by this condition.
     * 
     * @param tuple
     *            The <code>Tuple</code> to test.
     * 
     * @return <code>true</code> if the <code>Tuple</code> passes this condition, else <code>false</code>.
     * 
     * @throws ConditionException
     *             if an error occurs during filtering.
     */
    public boolean isAllowed(Tuple tuple) throws ConditionException
    {

        try
        {
            return conditionInvoker.invoke( tuple,
                                            this.requiredDeclarations,
                                            tuple.getWorkingMemory().getApplicationDataMap() );
        }
        catch ( Exception e )
        {
            throw new ConditionException( e,
                                          this.rule,
                                          this.expression );
        }
    }

    // ------------------------------------------------------------

    public int hashCode()
    {
        return this.expression.hashCode();
    }

    public boolean equals(Object object)
    {
        if ( this == object )
        {
            return true;
        }

        if ( object == null || getClass() != object.getClass() )
        {
            return false;
        }

        return this.expression.equals( ((JavaCondition) object).expression );
    }

    public String toString()
    {
        return "[Condition: " + this.expression + "]";
    }

}
