package org.drools.semantics.java;

/*
 $Id: Expr.java,v 1.18 2004-07-28 13:55:41 mproctor Exp $

 Copyright 2002 (C) The Werken Company. All Rights Reserved.

 Redistribution and use of this software and associated documentation
 ("Software"), with or without modification, are permitted provided
 that the following conditions are met:

 1. Redistributions of source code must retain copyright
    statements and notices.  Redistributions must also contain a
    copy of this document.

 2. Redistributions in binary form must reproduce the
    above copyright notice, this list of conditions and the
    following disclaimer in the documentation and/or other
    materials provided with the distribution.

 3. The name "drools" must not be used to endorse or promote
    products derived from this Software without prior written
    permission of The Werken Company.  For written permission,
    please contact bob@werken.com.

 4. Products derived from this Software may not be called "drools"
    nor may "drools" appear in their names without prior written
    permission of The Werken Company. "drools" is a registered
    trademark of The Werken Company.

 5. Due credit should be given to The Werken Company.
    (http://drools.werken.com/).

 THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE WERKEN COMPANY OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.

 */
import net.janino.ScriptEvaluator;
import net.janino.Scanner;
import java.io.StringReader;
import org.drools.spi.Tuple;
import org.drools.spi.KnowledgeHelper;

import java.util.Map;

import org.drools.rule.Declaration;

import java.lang.reflect.InvocationTargetException;

/** Base class for expression-based Java semantic components.
 *
 *  @see ExprCondition
 *  @see ExprExtractor
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id: Expr.java,v 1.18 2004-07-28 13:55:41 mproctor Exp $
 */
public class Expr
    extends Interp
{
    // ------------------------------------------------------------
    //     Constants
    // ------------------------------------------------------------

    /** Empty declaration array. */
    private static final Declaration[] EMPTY_DECLS = new Declaration[0];

    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Required declarations. */
    private Declaration[] requiredDecls;

    private ConditionScript conditionScript;
    private ExtractorScript extractorScript;

    private static final String[] scriptParamNames = new String[] {"tuple", "decls", "drools", "applicationData"};

    protected Expr()
        throws Exception
    {
        super();
    }

    // ------------------------------------------------------------
    //     Constants
    // ------------------------------------------------------------

    /** Construct.
     *
     *  @param expr The expression.
     *  @param availDecls The available declarations.
     *
     *  @throws ConfigurationException If an error occurs while
     *          attempting to perform configuration.
     */
    protected Expr(String expr,
                   Declaration[] availDecls)
        throws Exception
    {
        super(expr);
        this.requiredDecls = analyze( expr,
                                      availDecls );
    }

    // ------------------------------------------------------------
    //     Constants
    // ------------------------------------------------------------

    /** Retrieve the expression.
     *
     *  @return The expression.
     */
    public String getExpression()
    {
        return getText();
    }

   public boolean evaluateCondition(Tuple tuple) throws Exception
   {
        if (conditionScript == null)
        {
           String expr = getPreparedText(tuple, this.requiredDecls);
           conditionScript  = ( ConditionScript )
                              ScriptEvaluator.createFastScriptEvaluator(
                              new Scanner(null, new StringReader(expr)),
                              ConditionScript.class,
                              scriptParamNames,
                              (ClassLoader) null);          // Use current thread's context class loader
        }

        return conditionScript.invoke(tuple,
                             this.requiredDecls,
                             new KnowledgeHelper( tuple ),
                             tuple.getWorkingMemory().getApplicationDataMap());
   }

   public Object evaluateExtractor(Tuple tuple) throws Exception
   {
        if (extractorScript == null)
        {
           String expr = getPreparedText(tuple, this.requiredDecls);
           extractorScript  = (ExtractorScript)
                              ScriptEvaluator.createFastScriptEvaluator(
                              new Scanner(null, new StringReader(expr)),
                              ExtractorScript.class,
                              scriptParamNames,
                              (ClassLoader) null);          // Use current thread's context class loader
        }

        return extractorScript.invoke(tuple,
                                      this.requiredDecls,
                                      new KnowledgeHelper( tuple ),
                                      tuple.getWorkingMemory().getApplicationDataMap());
   }

    protected Declaration[] analyze(String expr,
                                    Declaration[] available)
        throws Exception
    {
        ExprAnalyzer analyzer = new ExprAnalyzer();

        return analyzer.analyze( expr,
                                 available );
    }

    /** Retrieve the <code>Declaration</code>s required for
     *  evaluating the expression.
     *
     *  @return The required declarations.
     */
    public Declaration[] getRequiredTupleMembers()
    {
        return this.requiredDecls;
    }

    public interface ConditionScript
    {
        public boolean invoke(Tuple tuple,
                             Declaration[] decls,
                             KnowledgeHelper drools,
                             Map applicationData) throws Exception;
    }

    public interface ExtractorScript
    {
        public Object invoke(Tuple tuple,
                             Declaration[] decls,
                             KnowledgeHelper drools,
                             Map applicationData) throws Exception;
    }

}
