package org.drools.reteoo;

/*
 $Id: Rete.java,v 1.15 2004-08-08 05:48:37 dbarnett Exp $

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
import org.drools.spi.ObjectType;

import java.io.Serializable;
import java.util.Map;
import java.util.HashMap;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import java.io.IOException;
import java.io.ObjectInputStream;

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
     *  @param handle The fact handle.
     *  @param object The object to assert.
     *  @param workingMemory The working memory session.
     *
     *  @throws FactException if an error occurs during assertion.
     */
    void assertObject(FactHandle handle,
                      Object object,
                      WorkingMemoryImpl workingMemory)
        throws FactException
    {
        Iterator       nodeIter = getObjectTypeNodeIterator();
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
     *  @param handle The handle of the fact to retract.
     *  @param workingMemory The working memory session.
     *
     *  @throws FactException if an error occurs during retraction.
     */
    void retractObject(FactHandle handle,
                       WorkingMemoryImpl workingMemory)
        throws FactException
    {
        Iterator       nodeIter = getObjectTypeNodeIterator();
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
     *  @param handle The fact handle.
     *  @param object The modified value object.
     *  @param workingMemory The working memory session.
     *
     *  @throws FactException if an error occurs during modification.
     */
    void modifyObject(FactHandle handle,
                      Object object,
                      WorkingMemoryImpl workingMemory)
        throws FactException
    {
        Iterator       nodeIter = getObjectTypeNodeIterator();
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
     *  @return The matching <code>ObjectTypeNode</codeb>.
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
