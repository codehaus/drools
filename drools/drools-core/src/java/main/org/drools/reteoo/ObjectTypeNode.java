
package org.drools.reteoo;

import org.drools.WorkingMemory;
import org.drools.AssertionException;
import org.drools.RetractionException;
import org.drools.ModificationException;
import org.drools.spi.ObjectType;

import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.Iterator;

/** Filters <code>Objects</code> coming from the {@link RootNode}
 *  using a {@link ObjectType} semantic module.
 *
 *  <p>
 *  It receives <code>Objects</code> from the <code>RootNode</code>,
 *  uses a <code>ObjectType</code> instance to determine membership,
 *  and propagates matching <code>Objects</code> further to all
 *  matching {@link ParameterNode}s.
 *  </p>
 *
 *  @see ObjectType
 *  @see ParameterNode
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class ObjectTypeNode
{
    /** The <code>ObjectType</code> semantic module. */
    private ObjectType objectType;

    /** The <code>ParameterNode</code> children. */
    private Set        parameterNodes;

    /** Construct given a semantic <code>ObjectType</code>.
     *
     *  @param objectType The semantic object-type differentiator.
     */
    ObjectTypeNode(ObjectType objectType)
    {
        this.objectType = objectType;

        this.parameterNodes = Collections.EMPTY_SET;
    }

    /** Retrieve the semantic <code>ObjectType</code> differentiator.
     *
     *  @return The semantic <code>ObjectType</code> differentiator.
     */
    public ObjectType getObjectType()
    {
        return this.objectType;
    }

    /** Add a <code>ParameterNode</code> child to this node.
     *
     *  @param node The <code>ParameterNode</code> child to add.
     */
    protected void addParameterNode(ParameterNode node)
    {
        if ( this.parameterNodes == Collections.EMPTY_SET )
        {
            this.parameterNodes = new HashSet();
        }

        this.parameterNodes.add( node );
    }

    /** Retrieve the <code>Set</code> of <code>ParameterNodes/code>
     *  children of this node.
     *
     *  @return The <code>Set</code> of <code>ParameterNode</code>
     *          children.
     */
    protected Set getParameterNodes()
    {
        return this.parameterNodes;
    }

    /** Retreive an <code>Iterator</code> over <code>ParameterNode</code>
     *  children of this node.
     *
     *  @return An <code>Iterator</code> over <code>ParameterNode</code>
     *          children of this node.
     */
    protected Iterator getParameterNodeIterator()
    {
        return this.parameterNodes.iterator();
    }

    /** Assert a new fact object into this <code>RuleBase</code>
     *  and the specified <code>WorkingMemory</code>.
     *
     *  @param object The object to assert.
     *  @param workingMemory The working memory session.
     *
     *  @throws DroolsException if an error occurs during assertion.
     */
    public void assertObject(Object object,
                             WorkingMemory workingMemory) throws AssertionException
    {
        ObjectType objectType = getObjectType();

        Iterator      nodeIter = getParameterNodeIterator();
        ParameterNode eachNode = null;

        while ( nodeIter.hasNext() )
        {
            eachNode = (ParameterNode) nodeIter.next();

            if ( objectType.matches( object ) )
            {
                eachNode.assertObject( object,
                                       workingMemory );
            }
        }
    }

    /** Retract a fact object from this <code>RuleBase</code>
     *  and the specified <code>WorkingMemory</code>.
     *
     *  @param object The object to retract.
     *  @param workingMemory The working memory session.
     *
     *  @throws DroolsException if an error occurs during assertion.
     */
    public void retractObject(Object object,
                              WorkingMemory workingMemory) throws RetractionException
    {
        ObjectType objectType = getObjectType();

        Iterator      nodeIter = getParameterNodeIterator();
        ParameterNode eachNode = null;

        while ( nodeIter.hasNext() )
        {
            eachNode = (ParameterNode) nodeIter.next();

            if ( objectType.matches( object ) )
            {
                eachNode.retractObject( object,
                                        workingMemory );
            }
        }
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
     *  @throws DroolsException if an error occurs during assertion.
     */
    public void modifyObject(Object object,
                             WorkingMemory workingMemory) throws ModificationException
    {
        ObjectType objectType = getObjectType();

        Iterator      nodeIter = getParameterNodeIterator();
        ParameterNode eachNode = null;

        while ( nodeIter.hasNext() )
        {
            eachNode = (ParameterNode) nodeIter.next();

            if ( objectType.matches( object ) )
            {
                eachNode.modifyObject( object,
                                       workingMemory );
            }
        }
    }
}
