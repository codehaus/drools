package org.drools.reteoo;

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
     *  @param object The object to retract.
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
     *  @param object The object to modify.
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
