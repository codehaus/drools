package org.drools.reteoo;

import org.drools.FactHandle;
import org.drools.FactException;
import org.drools.AssertionException;
import org.drools.RetractionException;
import org.drools.spi.ObjectType;

import java.util.Set;
import java.util.HashSet;
import java.util.Collections;
import java.util.Iterator;

/** Filters <code>Objects</code> coming from the <code>Rete</code>
 *  using a <code>ObjectType</code> semantic module.
 *
 *  <p>
 *  It receives <code>Objects</code> from the <code>Rete</code>,
 *  uses a <code>ObjectType</code> instance to determine membership,
 *  and propagates matching <code>Objects</code> further to all
 *  matching <code>ParameterNode</code>s.
 *  </p>
 *
 *  @see ObjectType
 *  @see ParameterNode
 *  @see Rete
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob@eng.werken.com</a>
 */
class ObjectTypeNode
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** The <code>ObjectType</code> semantic module. */
    private ObjectType objectType;

    /** The <code>ParameterNode</code> children. */
    private Set        parameterNodes;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct given a semantic <code>ObjectType</code>.
     *
     *  @param objectType The semantic object-type differentiator.
     */
    public ObjectTypeNode(ObjectType objectType)
    {
        this.objectType = objectType;

        this.parameterNodes = Collections.EMPTY_SET;
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

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
    void addParameterNode(ParameterNode node)
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
    Set getParameterNodes()
    {
        return this.parameterNodes;
    }

    /** Retreive an <code>Iterator</code> over <code>ParameterNode</code>
     *  children of this node.
     *
     *  @return An <code>Iterator</code> over <code>ParameterNode</code>
     *          children of this node.
     */
    Iterator getParameterNodeIterator()
    {
        return this.parameterNodes.iterator();
    }

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
        ObjectType objectType = getObjectType();

        if ( ! objectType.matches( object ) )
        {
            return;
        }

        Iterator          nodeIter = getParameterNodeIterator();
        ParameterNode eachNode = null;

        while ( nodeIter.hasNext() )
        {
            eachNode = (ParameterNode) nodeIter.next();

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
     *  @throws RetractionException if an error occurs during assertion.
     */
    void retractObject(FactHandle handle,
                       Object object,
                       WorkingMemoryImpl workingMemory)
        throws RetractionException
    {
        ObjectType objectType = getObjectType();

        if ( ! objectType.matches( object ) )
        {
            return;
        }

        Iterator          nodeIter = getParameterNodeIterator();
        ParameterNode eachNode = null;

        while ( nodeIter.hasNext() )
        {
            eachNode = (ParameterNode) nodeIter.next();

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
     *  @throws FactException if an error occurs during assertion.
     */
    void modifyObject(FactHandle handle,
                      Object object,
                      WorkingMemoryImpl workingMemory)
        throws FactException
    {
        ObjectType objectType = getObjectType();

        Iterator          nodeIter = getParameterNodeIterator();
        ParameterNode eachNode = null;

        if ( ! objectType.matches( object ) )
        {
            while ( nodeIter.hasNext() )
            {
                eachNode = (ParameterNode) nodeIter.next();
                
                eachNode.retractObject( handle,
                                        workingMemory );
            }
        }
        else
        {
            while ( nodeIter.hasNext() )
            {
                eachNode = (ParameterNode) nodeIter.next();
                
                eachNode.modifyObject( handle,
                                       object,
                                       workingMemory );
            }
        }
    }
}
