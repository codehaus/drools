
package org.drools.reteoo;

import org.drools.WorkingMemory;
import org.drools.AssertionException;
import org.drools.RetractionException;
import org.drools.ModificationException;
import org.drools.spi.ObjectType;

import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/** The root node in the Rete-OO network.
 *
 *  This node accepts an <code>Object</code>, and simply
 *  propagates it to all {@link ObjectTypeNode}s for
 *  type testings.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class RootNode
{
    /** The set of <code>ObjectTypeNodes</code>. */
    private Map objectTypeNodes;

    /** Construct.
     */
    public RootNode()
    {
        this.objectTypeNodes = Collections.EMPTY_MAP;
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
        Iterator       nodeIter = getObjectTypeNodeIterator();
        ObjectTypeNode eachNode = null;

        while ( nodeIter.hasNext() )
        {
            eachNode = (ObjectTypeNode) nodeIter.next();

            eachNode.assertObject( object,
                                   workingMemory );
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
        Iterator       nodeIter = getObjectTypeNodeIterator();
        ObjectTypeNode eachNode = null;

        while ( nodeIter.hasNext() )
        {
            eachNode = (ObjectTypeNode) nodeIter.next();

            eachNode.retractObject( object,
                                    workingMemory );
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
        Iterator       nodeIter = getObjectTypeNodeIterator();
        ObjectTypeNode eachNode = null;

        while ( nodeIter.hasNext() )
        {
            eachNode = (ObjectTypeNode) nodeIter.next();

            eachNode.modifyObject( object,
                                   workingMemory );
        }
    }

    /** Add an <code>ObjectTypeNode</code> child to
     *  this <code>RootNode</code>.
     *
     *  @param node The node to add.
     */
    protected void addObjectTypeNode(ObjectTypeNode node)
    {
        if ( this.objectTypeNodes == Collections.EMPTY_MAP )
        {
            this.objectTypeNodes = new HashMap();
        }

        this.objectTypeNodes.put( node.getObjectType(),
                                  node );
    }

    /** Retrieve all <code>ObjectTypeNode</code> children
     *  of this node.
     *
     *  @return The <code>Set</code> of <code>ObjectTypeNodes</code>.
     */
    protected Collection getObjectTypeNodes()
    {
        return this.objectTypeNodes.values();
    }

    /** Retrieve an <code>Iterator</code> over the
     *  <code>ObjectTypeNode</code> children of this
     *  node.
     *
     *  @return An <code>Iterator</code> over <code>ObjectTypeNodes</code>.
     */
    protected Iterator getObjectTypeNodeIterator()
    {
        return this.objectTypeNodes.values().iterator();
    }

    /** Retrieve an {@link ObjectTypeNode} keyed by {@link ObjectType}.
     *
     *  @param objectType The <code>ObjectType</code> key.
     *
     *  @return The matching <code>ObjectTypeNode</code> if one has already
     *          been created, else <code>null</code>.
     */
    protected ObjectTypeNode getObjectTypeNode(ObjectType objectType)
    {
        return (ObjectTypeNode) this.objectTypeNodes.get( objectType );
    }

    /** Retrieve an {@link ObjectTypeNode} keyed by {@link ObjectType},
     *  creating one, if necessary.
     *
     *  @param objectType The <code>ObjectType</code> key.
     *
     *  @return The matching <code>ObjectTypeNode</code>.
     */
    protected ObjectTypeNode getOrCreateObjectTypeNode(ObjectType objectType)
    {
        ObjectTypeNode node = getObjectTypeNode( objectType );

        if ( node == null )
        {
            node = new ObjectTypeNode( objectType );

            addObjectTypeNode( node );
        }

        return node;
    }
}
