package org.drools.semantics.groovy;

/*
 * $Id: Eval.java,v 1.7 2004-11-03 22:54:36 mproctor Exp $
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

import java.util.Set;

import groovy.lang.Binding;

import org.drools.rule.Declaration;
import org.drools.rule.Imports;
import org.drools.spi.Tuple;

/**
 * Base class for Groovy based semantic components.
 * 
 * @see ExprCondition
 * @see ExprExtractor
 * 
 * @author <a href="mailto:james@coredevelopers.net">James Strachan </a>
 * @author <a href="mailto:ckl@dacelo.nl">Christiaan ten Klooster </a>
 * 
 * @version $Id: Eval.java,v 1.7 2004-11-03 22:54:36 mproctor Exp $
 */
public class Eval extends Interp
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Required decls. */
    private Declaration[] decls;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /**
     * Construct.
     */
    protected Eval(String text, Set imports, Declaration[] availDecls) throws Exception
    {
        super( text, imports, "eval" );

        this.decls = analyze( availDecls );
    }

    /**
     * Default constructor - required for serialization
     */
    public Eval()
    {
        super( );
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /**
     * Evaluate.
     * 
     * @param tuple Tuple containing variable bindings.
     * 
     * @return The result of evaluation.
     */
    public Object evaluate(Tuple tuple)
    {
        Binding dict = setUpDictionary( tuple );

        return evaluate( dict );
    }

    /**
     * Evaluate.
     * 
     * @param locals The evaluation dictionary.
     * 
     * @return The result of evaluation.
     */
    protected Object evaluate(Binding locals)
    {
        //ScriptContext globals = new ScriptContext();
        getCode( ).setBinding( locals );
        return getCode( ).run( );
    }

    /**
     * Evaluate.
     * 
     * @return The result of evaluation.
     */
    protected Object evaluate()
    {
        Binding locals = new Binding( );

        return evaluate( locals );
    }

    /**
     * Retrieve the array of <code>Declaration</code> s required by this
     * condition to perform its duties.
     * 
     * @return The array of <code>Declarations</code> expected on incoming
     *         <code>Tuples</code>.
     */
    public Declaration[] getRequiredTupleMembers()
    {
        return this.decls;
    }

    protected Declaration[] analyze(Declaration[] availDecls) throws Exception
    {
        ExprAnalyzer analyzer = new ExprAnalyzer( );

        return analyzer.analyze( getText( ), availDecls );
    }
}