package org.drools.semantics.java;

/*
 * $Id: MissingDeclarationException.java,v 1.4 2005-11-10 05:11:18 mproctor Exp $
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

import org.drools.DroolsException;

/**
 * Indicates an attempt to use an expression which references non-existant <code>Declaration</code>s.
 * 
 * @author <a href="mailto:bob@eng.werken.com">bob mcwhirter </a>
 * 
 * @version $Id: MissingDeclarationException.java,v 1.4 2005-11-10 05:11:18 mproctor Exp $
 */
public class MissingDeclarationException extends DroolsException
{
    // ------------------------------------------------------------
    // Instance members
    // ------------------------------------------------------------

    /** The expression. */
    private String expr;

    /** The missing id. */
    private String id;

    // ------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------

    /**
     * Construct.
     * 
     * @param expr
     *            The expression.
     * @param id
     *            The missing declaration.
     */
    public MissingDeclarationException(String expr,
                                       String id)
    {
        this.expr = expr;
        this.id = id;
    }

    // ------------------------------------------------------------
    // Instance methods
    // ------------------------------------------------------------

    /**
     * Retrieve the expression.
     * 
     * @return The expression.
     */
    public String getExpression()
    {
        return this.expr;
    }

    /**
     * Retrieve the identifier.
     * 
     * @return The identifier.
     */
    public String getIdentifier()
    {
        return this.id;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    // java.lang.Throwable
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Retrieve the erorr message.
     * 
     * @return The error message.
     */
    public String getMessage()
    {
        return "No declaration for \"" + getIdentifier() + "\" in: " + getExpression();
    }
}