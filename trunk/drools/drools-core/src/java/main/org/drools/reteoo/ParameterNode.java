
package org.drools.reteoo;

import org.drools.WorkingMemory;
import org.drools.AssertionException;
import org.drools.RetractionException;
import org.drools.ModificationException;

import org.drools.spi.Declaration;

import java.util.Set;
import java.util.Collections;

/** Receives <code>Objects</code> from an {@link ObjectTypeNode},
 *  and creates a {@link ReteTuple}, passing the result to the following node.
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
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class ParameterNode extends TupleSource
{
    private Declaration declaration;

    /** Construct.
     *
     *  @param inputNode The <code>ObjectTypeNode</code> input to this.
     *  @param declaration The root fact object {@link Declaration}.
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

    /** Retrieve the root fact object <code>Declaration</code>.
     *
     *  @return The <code>Declaration</code>.
     */
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
        ReteTuple tuple = new ReteTuple();

        tuple.putKeyColumn( getDeclaration(),
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
        try
        {
            propagateRetractObject( object,
                                    workingMemory );
            
            assertObject( object,
                          workingMemory );
        }
        catch (RetractionException e)
        {
            throw new ModificationException( e );
        }
        catch (AssertionException e)
        {
            throw new ModificationException( e );
        }
    }
}
