package org.drools.semantics.python;

/*
 * $Id: ExprVisitor.java,v 1.5 2004-11-28 03:34:05 simon Exp $
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
/*
 * (c) Copyright 2001, 2002 Tonic Software, Inc. All Rights Reserved
 */

import org.python.parser.SimpleNode;
import org.python.parser.Visitor;
import org.python.parser.ast.Name;

import java.util.HashSet;
import java.util.Set;

/**
 * Visits nodes in a Jython parse tree to extract the individual expression
 * criteria.
 *
 * @author <a href="martin@tonic.com">Martin Chilvers </a>
 * @author <a href="bob@werken.com">bob mcwhirter </a>
 * @author <a href="mailto:christiaan@dacelo.nl">Christiaan ten Klooster </a>
 */
public class ExprVisitor extends Visitor
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** List of the variables as we parse an expression. */
    private Set variables;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /**
     * Construct.
     */
    ExprVisitor()
    {
        // intentionally left blank.
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //     org.python.parser.Visitor
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Visite the node.
     *
     * @param node The node.
     *
     * @return Set of variable names.
     *
     * @throws Exception If an error occurs while traversing.
     */
    public Object eval_input(SimpleNode node) throws Exception
    {
        variables = new HashSet( );

        traverse( node );

        return variables;
    }

    /**
     * Visit a Name node.
     *
     * @param node The node.
     *
     * @return The node.
     *
     * @throws Exception If an error occurs while traversing.
     */
    public Object visitName(Name node) throws Exception
    {
        variables.add( node.id );

        return node;
    }

}