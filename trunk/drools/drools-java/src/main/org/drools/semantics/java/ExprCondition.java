package org.drools.semantics.java;

/*
 $Id: ExprCondition.java,v 1.14 2004-07-04 11:59:56 mproctor Exp $

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

import bsh.NameSpace;
import bsh.Interpreter;
import bsh.Primitive;

import java.util.Arrays;

import org.drools.rule.Declaration;
import org.drools.smf.Configuration;
import org.drools.spi.Tuple;
import org.drools.spi.Condition;
import org.drools.spi.ConditionException;

/** Java expression semantics <code>Condition</code>.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 *
 *  @version $Id: ExprCondition.java,v 1.14 2004-07-04 11:59:56 mproctor Exp $
 */
public class ExprCondition
    extends Expr
    implements Condition
{
    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    public ExprCondition()
        throws Exception
    {
        super();
    }

    /** Construct.
     *
     *  @param expr The expression.
     *  @param availDecls The available declarations.
     *
     *  @throws ConfigurationException If an error occurs while
     *          attempting to perform configuration.
     */
    public ExprCondition(String expr,
                         Declaration[] availDecls)
        throws Exception
    {
        super( expr,
               availDecls );
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //     org.drools.spi.Condition
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /** Determine if the supplied <code>Tuple</code> is allowed
     *  by this condition.
     *
     *  @param tuple The <code>Tuple</code> to test.
     *
     *  @return <code>true</code> if the <code>Tuple</code>
     *          passes this condition, else <code>false</code>.
     *
     *  @throws ConditionException if an error occurs during filtering.
     */
    public boolean isAllowed(Tuple tuple) throws ConditionException
    {
        try
        {
            NameSpace ns = setUpNameSpace( tuple, getNameSpace() );

            Declaration[] params = getRequiredTupleMembers();

            Object[] paramValues = new Object[ params.length ];

            for ( int i = 0 ; i < params.length ; ++i ) {
                paramValues[i] = tuple.get( params[i] );
            }

            Object result = ns.invokeMethod( getMethodName(),
                                             paramValues,
                                             getInterpreter() );

            if ( result instanceof Primitive )
            {
                result = Primitive.unwrap( result );
            }

            if ( result instanceof Boolean )
            {
                return ((Boolean)result).booleanValue();
            }
        }
        catch (Exception e)
        {
            throw new ConditionException( e );
        }

        throw new NonBooleanExprException( getExpression() );
    }
}
