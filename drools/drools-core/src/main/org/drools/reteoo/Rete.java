package org.drools.reteoo;

import org.drools.FactHandle;
import org.drools.FactException;
import org.drools.spi.ObjectType;

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

/** The Rete-OO network.
 *
 *  This node accepts an <code>Object</code>, and simply
 *  propagates it to all <code>ObjectTypeNode</code>s for
 *  type testings.
 *
 *  @see ObjectTypeNode
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
class Rete
    implements Serializable
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** The set of <code>ObjectTypeNodes</code>. */
    private Map objectTypeNodes;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    public Rete()
    {
        this.objectTypeNodes = Collections.EMPTY_MAP;
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
        throws FactException
    {
        Iterator           nodeIter = getObjectTypeNodeIterator();
        ObjectTypeNode eachNode = null;

        while ( nodeIter.hasNext() )
        {
            eachNode = (ObjectTypeNode) nodeIter.next();

            eachNode.assertObject( handle,
                                   object,
                                   workingMemory );
        }
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
        throws FactException
    {
        Iterator           nodeIter = getObjectTypeNodeIterator();
        ObjectTypeNode eachNode = null;

        while ( nodeIter.hasNext() )
        {
            eachNode = (ObjectTypeNode) nodeIter.next();

            eachNode.retractObject( handle,
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
     *  @throws FactException if an error occurs during modification.
     */
    void modifyObject(FactHandle handle,
                      Object object,
                      WorkingMemoryImpl workingMemory)
        throws FactException
    {
        Iterator           nodeIter = getObjectTypeNodeIterator();
        ObjectTypeNode eachNode = null;

        while ( nodeIter.hasNext() )
        {
            eachNode = (ObjectTypeNode) nodeIter.next();

            eachNode.modifyObject( handle,
                                   object,
                                   workingMemory );
        }
    }

    /** Add an <code>ObjectTypeNode</code> child to
     *  this <code>Rete</code>.
     *
     *  @param node The node to add.
     */
    void addObjectTypeNode(ObjectTypeNode node)
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
    Collection getObjectTypeNodes()
    {
        return this.objectTypeNodes.values();
    }

    /** Retrieve an <code>Iterator</code> over the
     *  <code>ObjectTypeNode</code> children of this
     *  node.
     *
     *  @return An <code>Iterator</code> over <code>ObjectTypeNodes</code>.
     */
    Iterator getObjectTypeNodeIterator()
    {
        return this.objectTypeNodes.values().iterator();
    }

    /** Retrieve an <code>ObjectTypeNode</code> keyed by <code>ObjectType</code>.
     *
     *  @param objectType The <code>ObjectType</code> key.
     *
     *  @return The matching <code>ObjectTypeNode</code> if one has already
     *          been created, else <code>null</code>.
     */
    ObjectTypeNode getObjectTypeNode(ObjectType objectType)
    {
        return (ObjectTypeNode) this.objectTypeNodes.get( objectType );
    }

    /** Retrieve an <code>ObjectTypeNode</code> keyed by <code>ObjectType</code>,
     *  creating one, if necessary.
     *
     *  @param objectType The <code>ObjectType</code> key.
     *
     *  @return The matching <code>ObjectTypeNode</code>.
     */
    ObjectTypeNode getOrCreateObjectTypeNode(ObjectType objectType)
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
