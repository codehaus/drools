
package org.drools.reteoo;

import org.drools.WorkingMemory;
import org.drools.AssertionException;
import org.drools.RetractionException;
import org.drools.ModificationException;

import org.drools.spi.Declaration;
import org.drools.spi.Tuple;

import java.util.Set;
import java.util.Collections;

/** Receives <code>Objects</code> from an {@link ObjectTypeNode},
 *  and creates a {@link Tuple}, passing the result to the following node.
 *
 *  <p>
 *  The <code>ParameterNode</code> is the first node that works in
 *  terms of <code>Tuples</code>.  An instance of <code>ParameterNode</code>
 *  exists for each <i>root fact object</i> parameter of each rule.
 *  </p>
 *
 *  @see ObjectTypeNode
 *  @see Tuple
 *  @see TupleSink
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class ParameterNode extends TupleSource
{
    private Declaration declaration;

    public ParameterNode(ObjectTypeNode inputNode,
                         Declaration declaration)
    {
        this.declaration = declaration;

        if ( inputNode != null )
        {
            inputNode.addParameterNode( this );
        }
    }

    public Declaration getDeclaration()
    {
        return this.declaration;
    }

    public Set getTupleDeclarations()
    {
        return Collections.singleton( this.declaration );
    }

    protected void assertObject(Object object,
                                WorkingMemory workingMemory) throws AssertionException
    {
        Tuple tuple = new ParameterTuple( getDeclaration(),
                                          object );

        propagateAssertTuple( tuple,
                              workingMemory );
    }

    protected void retractObject(Object object,
                                 WorkingMemory workingMemory) throws RetractionException
    {
        propagateRetractObject( object,
                                workingMemory );
    }

    protected void modifyObject(Object object,
                                WorkingMemory workingMemory) throws ModificationException
    {
    }
}
