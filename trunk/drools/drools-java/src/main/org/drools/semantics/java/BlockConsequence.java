package org.drools.semantics.java;

/*
 * $Id: BlockConsequence.java,v 1.41 2004-11-29 12:14:44 simon Exp $
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

import org.drools.WorkingMemory;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spi.Consequence;
import org.drools.spi.ConsequenceException;
import org.drools.spi.ImportEntry;
import org.drools.spi.KnowledgeHelper;
import org.drools.spi.Tuple;

import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Java block semantics <code>Consequence</code>.
 *
 * @author <a href="mailto:bob@werken.com">bob@werken.com </a>
 */
public class BlockConsequence
    implements
    Consequence,
    Serializable
{
    private static final String[] SCRIPT_PARAM_NAMES = new String[]{"tuple", "decls", "drools", "applicationData"};

    private final String block;

    private final Rule rule;

    private final Declaration[] declarations;

    private transient Script script;

    // ------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------

    /**
     * Construct.
     *
     * @param block The statement block.
     * @param rule The rule.
     */
    public BlockConsequence(String block,
                            Rule rule) throws Exception
    {
        this.block = block;

        this.rule = rule;

        List declarations = rule.getParameterDeclarations( );
        this.declarations = (Declaration[]) declarations.toArray( new Declaration[declarations.size( )] );

        this.script = compile( rule );
    }

    private void readObject(ObjectInputStream s) throws Exception
    {
        s.defaultReadObject( );

        this.script = compile( rule );
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
     *            The matching tuple.
     * @param workingMemory
     *            The working memory session.
     *
     * @throws ConsequenceException
     *             If an error occurs while attempting to invoke the
     *             consequence.
     */
    public void invoke(Tuple tuple,
                       WorkingMemory workingMemory) throws ConsequenceException
    {
        try
        {
            Map applicationData = tuple.getWorkingMemory( ).getApplicationDataMap( );

            script.invoke( tuple,
                           this.declarations,
                           new KnowledgeHelper( this.rule,
                                                tuple ),
                           applicationData );
        }
        catch ( Exception e )
        {
            throw new ConsequenceException( e,
                                            this.rule );
        }
    }

    /**
     * Retrieve the expression.
     *
     * @return The expression.
     */
    public String getBlock()
    {
        return this.block;
    }

    private Script compile(Rule rule) throws Exception
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

        return (Script) Interp.compile( rule,
                                        Script.class,
                                        this.block,
                                        this.block,
                                        SCRIPT_PARAM_NAMES,
                                        this.declarations,
                                        imports,
                                        rule.getApplicationData( ) );
    }

    public interface Script
    {
        public void invoke(Tuple tuple,
                           Declaration[] decls,
                           KnowledgeHelper drools,
                           Map applicationData) throws Exception;
    }

}
