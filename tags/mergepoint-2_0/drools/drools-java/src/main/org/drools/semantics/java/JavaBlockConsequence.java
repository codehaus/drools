package org.drools.semantics.java;

/*
 * $Id: JavaBlockConsequence.java,v 1.6.2.1 2005-05-08 00:57:36 mproctor Exp $
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

import org.codehaus.janino.Scanner;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spi.ConditionException;
import org.drools.spi.Consequence;
import org.drools.spi.ConsequenceException;
import org.drools.spi.DefaultKnowledgeHelper;
import org.drools.spi.KnowledgeHelper;
import org.drools.spi.Tuple;

/**
 * Java block semantics <code>Consequence</code>.
 * 
 * @author <a href="mailto:bob@werken.com">bob@werken.com </a>
 */
public class JavaBlockConsequence
    implements
    Consequence,
    Serializable
{
    private final String        block;

    private final Rule          rule;

    private final Declaration[] declarations;

    private final String        className;

    private transient Script    script;

    // ------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------

    /**
     * Construct.
     * 
     * @param block
     *        The statement block.
     * @param rule
     *        The rule.
     */
    public JavaBlockConsequence(Rule rule,
                                int id,
                                String block) throws Exception
    {
        this.block = block;

        this.rule = rule;

        List declarations = rule.getParameterDeclarations( );
        this.declarations = (Declaration[]) declarations.toArray( new Declaration[declarations.size( )] );
        this.className = "Consequence_" + id;
        this.script = compile( );
    }

    // ------------------------------------------------------------
    // Instance methods
    // ------------------------------------------------------------

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // org.drools.spi.Consequence
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Execute the consequence for the supplied matching <code>Tuple</code>.
     * 
     * @param tuple
     *        The matching tuple.
     * @param workingMemory
     *        The working memory session.
     * 
     * @throws ConsequenceException
     *         If an error occurs while attempting to invoke the consequence.
     */
    public void invoke(Tuple tuple) throws ConsequenceException
    {
        try
        {
            if ( this.script == null )
            {
                this.script = compile( );
            }
            this.script.invoke( tuple,
                                this.declarations,
                                new DefaultKnowledgeHelper( this.rule,
                                                            tuple ),
                                tuple.getWorkingMemory( ).getApplicationDataMap( ) );
        }
        catch ( Scanner.LocatedException e )
        {
            throw new ConsequenceException( e,
                                            this.rule,
                                            this.block );
        }
        catch ( CompilationException e )
        {
            throw new ConsequenceException( e.getMessage( ),
                                            e.getRule( ),
                                            e.getText( ) );
        }
        catch ( Exception e )
        {
            throw new ConsequenceException( e,
                                            this.rule,
                                            this.block );
        }
    }

    private Script compile() throws Exception
    {
        return (Script) JavaCompiler.compile( this.rule,
                                              this.className,
                                              Script.class,
                                              this.block,
                                              this.block,
                                              this.declarations );
    }

    public String toString()
    {
        return "[Consequence: " + this.block + "]";
    }

    public static interface Script
    {
        public void invoke(Tuple tuple,
                           Declaration[] decls,
                           KnowledgeHelper drools,
                           Map applicationData) throws Exception;
    }
}
