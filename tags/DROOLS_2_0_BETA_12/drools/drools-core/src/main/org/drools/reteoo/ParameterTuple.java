package org.drools.reteoo;

/*
 $Id: ParameterTuple.java,v 1.10 2003-11-19 21:31:10 bob Exp $

 Copyright 2001-2003 (C) The Werken Company. All Rights Reserved.
 
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
    permission of The Werken Company. "drools" is a trademark of 
    The Werken Company.
 
 5. Due credit should be given to The Werken Company.
    (http://werken.com/)
 
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

import org.drools.rule.Declaration;

import java.util.Set;
import java.util.HashSet;

/** Implementation of <code>ReteTuple</code> with a single column,
 *  based upon a <i>root fact object parameter</i> of a rule.
 *
 *  @see ReteTuple
 *  @see ParameterNode
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
class ParameterTuple
    extends ReteTuple
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** The parameter <code>Declaration</code>. */
    private Declaration declaration;

    /** The object bound to the parameter. */
    private Object      object;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     *
     *  @param declaration The parameter <code>Declaration</code>.
     *  @param object The object bound to the <code>Declaration</code>.
     */
    ParameterTuple(Declaration declaration,
                   Object object)
    {
        this.declaration = declaration;
        this.object      = object;

        // addRootFactObject( object );
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Retrieve the object for the parameter.
     *
     *  @return The current value of the parameter.
     */
    public Object getParameterObject()
    {
        return this.object;
    }

    /** Retrieve the root fact object <code>Declaration</code>.
     *
     *  @return The <code>Declaration</code>.
     */
    Declaration getParameterDeclaration()
    {
        return this.declaration;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     org.drools.reteoo.ReteTuple
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Retrieve the value of a particular declaration column.
     *
     *  @param declaration The declaration.
     *
     *  @return The value.
     */
    public Object get(Declaration declaration)
    {
        if ( this.declaration.equals( declaration ) )
        {
            return getParameterObject();
        }

        return super.get( declaration );
    }

    /** Retrieve all declarations participating in this tuple.
     *
     *  @return Set of all declarations.
     */
    public Set getDeclarations()
    {
        Set decls = new HashSet();

        decls.add( getParameterDeclaration() );

        decls.addAll( super.getDeclarations() );

        return decls;
    }
}
