package org.drools.reteoo;

/*
 $Id: ParameterNode.java,v 1.16 2003-11-21 04:18:13 bob Exp $

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

import org.drools.FactHandle;
import org.drools.FactException;
import org.drools.AssertionException;
import org.drools.RetractionException;
import org.drools.rule.Declaration;

import java.io.Serializable;
import java.util.Set;
import java.util.Collections;

/** Receives <code>Objects</code> from an <code>ObjectTypeNode</code>,
 *  and creates a <code>ReteTuple</code>, passing the result to the following node.
 *
 *  <p>
 *  The <code>ParameterNode</code> is the first node that works in
 *  terms of <code>Tuples</code>.  An instance of <code>ParameterNode</code>
 *  exists for each <i>root fact object</i> parameter of each rule.
 *  </p>
 *
 *  @see ObjectTypeNode
 *  @see TupleSink
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
class ParameterNode
    extends TupleSource
    implements Serializable
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** The parameter declaration. */
    private Declaration declaration;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     *
     *  @param inputNode The <code>ObjectTypeNode</code> input to this.
     *  @param declaration The root fact object <code>Declaration</code>.
     */
    public ParameterNode(ObjectTypeNode inputNode,
                         Declaration declaration)
    {
        this.declaration = declaration;

        if ( inputNode != null )
        {
            inputNode.addParameterNode( this );
        }
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Assert a new fact object into this <code>RuleBase</code>
     *  and the specified <code>WorkingMemory</code>.
     *
     *  @param handle The fact handle.
     *  @param object The object to assert.
     *  @param workingMemory The working memory session.
     *
     *  @throws AssertionException if an error occurs during assertion.
     */
    void assertObject(FactHandle handle,
                      Object object,
                      WorkingMemoryImpl workingMemory)
        throws AssertionException
    {
        ReteTuple tuple = new ReteTuple( getDeclaration(),
                                         handle,
                                         object );

        propagateAssertTuple( tuple,
                              workingMemory );
    }

    /** Retract a fact object from this <code>RuleBase</code>
     *  and the specified <code>WorkingMemory</code>.
     *
     *  @param handle The handle to the fact to retract.
     *  @param workingMemory The working memory session.
     *
     *  @throws RetractionException if an error occurs during retraction.
     */
    void retractObject(FactHandle handle,
                       WorkingMemoryImpl workingMemory)
        throws RetractionException
    {
        TupleKey key = new TupleKey( handle );

        propagateRetractTuples( key,
                                workingMemory );
    }

    /** Modify a fact object in this <code>RuleBase</code>
     *  and the specified <code>WorkingMemory</code>.
     *
     *  With the exception of time-based nodes, modification of
     *  a fact object is semantically equivelent to retracting and
     *  re-asserting it.
     *
     *  @param handle The fact handle.
     *  @param object The new fact value object.
     *  @param workingMemory The working memory session.
     *
     *  @throws FactException if an error occurs during modification.
     */
    void modifyObject(FactHandle handle,
                      Object object,
                      WorkingMemoryImpl workingMemory)
        throws FactException
    {
        ReteTuple tuple = new ReteTuple( getDeclaration(),
                                         handle,
                                         object );
        
        TupleSet tupleSet = new TupleSet( tuple );

        propagateModifyTuples( handle,
                               tupleSet,
                               workingMemory );
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     org.drools.reteoo.ParameterNode
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Retrieve the root fact object <code>Declaration</code>.
     *
     *  @return The <code>Declaration</code>.
     */
    public Declaration getDeclaration()
    {
        return this.declaration;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     org.drools.reteoo.impl.TupleSource
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Retrieve the <code>Set</code> of <code>Declaration</code>s
     *  in the propagated <code>Tuples</code>.
     *
     *  @return The <code>Set</code> of <code>Declarations</code>
     *          in progated <code>Tuples</code>.
     */
    public Set getTupleDeclarations()
    {
        return Collections.singleton( this.declaration );
    }
}
