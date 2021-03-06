package org.drools.reteoo;

/*
 * $Id: ObjectTypeNode.java,v 1.29 2005-02-02 00:23:22 mproctor Exp $
 *
 * Copyright 2001-2003 (C) The Werken Company. All Rights Reserved.
 *
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. The name "drools" must not be used to endorse or promote products derived
 * from this Software without prior written permission of The Werken Company.
 * For written permission, please contact bob@werken.com.
 *
 * 4. Products derived from this Software may not be called "drools" nor may
 * "drools" appear in their names without prior written permission of The Werken
 * Company. "drools" is a trademark of The Werken Company.
 *
 * 5. Due credit should be given to The Werken Company. (http://werken.com/)
 *
 * THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE WERKEN COMPANY OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

import org.drools.FactException;
import org.drools.FactHandle;
import org.drools.spi.ObjectType;

/**
 * Filters <code>Objects</code> coming from the <code>Rete</code> using a
 * <code>ObjectType</code> semantic module.
 *
 * <p>
 * It receives <code>Objects</code> from the <code>Rete</code>, uses a
 * <code>ObjectType</code> instance to determine membership, and propagates
 * matching <code>Objects</code> further to all matching
 * <code>ParameterNode</code>s.
 * </p>
 *
 * @see ObjectType
 * @see ParameterNode
 * @see Rete
 *
 * @author <a href="mailto:bob@eng.werken.com">bob@eng.werken.com </a>
 */
class ObjectTypeNode
    implements
    Serializable
{
    // ------------------------------------------------------------
    // Instance members
    // ------------------------------------------------------------

    /** The <code>ObjectType</code> semantic module. */
    private final ObjectType    objectType;

    /** The <code>ParameterNode</code> children. */
    private final List          parameterNodes = new ArrayList( 1 );

    // ------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------

    /**
     * Construct given a semantic <code>ObjectType</code>.
     *
     * @param objectType
     *            The semantic object-type differentiator.
     */
    public ObjectTypeNode( ObjectType objectType )
    {
        this.objectType = objectType;
    }

    // ------------------------------------------------------------
    // Instance methods
    // ------------------------------------------------------------

    /**
     * Retrieve the semantic <code>ObjectType</code> differentiator.
     *
     * @return The semantic <code>ObjectType</code> differentiator.
     */
    public ObjectType getObjectType()
    {
        return this.objectType;
    }

    /**
     * Add a <code>ParameterNode</code> child to this node.
     *
     * @param node The <code>ParameterNode</code> child to add.
     */
    void addParameterNode( ParameterNode node )
    {
        if ( !this.parameterNodes.contains( node ) )
        {
            this.parameterNodes.add( node );
        }
    }

    /**
     * Retrieve the <code>Set</code> of <code>ParameterNodes/code>
     *  children of this node.
     *
     *  @return The <code>Set</code> of <code>ParameterNode</code>
     *          children.
     * TODO: Remove this.
     */
    Collection getParameterNodes()
    {
        return this.parameterNodes;
    }

    /**
     * Retreive an <code>Iterator</code> over <code>ParameterNode</code>
     * children of this node.
     *
     * @return An <code>Iterator</code> over <code>ParameterNode</code>
     *         children of this node.
     */
    Iterator getParameterNodeIterator()
    {
        return Collections.unmodifiableList( this.parameterNodes ).iterator( );
    }

    /**
     * Assert a new fact object into this <code>RuleBase</code> and the
     * specified <code>WorkingMemory</code>.
     *
     * @param handle The fact handle.
     * @param object The object to assert.
     * @param workingMemory The working memory session.
     *
     * @throws FactException if an error occurs during assertion.
     */
    void assertObject( FactHandle handle,
                       Object object,
                       WorkingMemoryImpl workingMemory ) throws FactException
    {
        if ( !this.objectType.matches( object ) )
        {
            return;
        }

        for ( int i = 0, size = this.parameterNodes.size( ); i < size; i++ )
        {
            ( ( ParameterNode ) this.parameterNodes.get( i ) ).assertObject( handle,
                                                                             object,
                                                                             workingMemory );
        }
    }

    /**
     * Retract a fact object from this <code>RuleBase</code> and the specified
     * <code>WorkingMemory</code>.
     *
     * @param handle The handle of the fact to retract.
     * @param workingMemory The working memory session.
     *
     * @throws FactException if an error occurs during assertion.
     */
    void retractObject( FactHandle handle,
                        WorkingMemoryImpl workingMemory ) throws FactException
    {
        if ( !this.objectType.matches( workingMemory.getObject( handle ) ) )
        {
            return;
        }


        for ( int i = 0, size = this.parameterNodes.size( ); i < size; i++ )
        {
            ( ( ParameterNode ) this.parameterNodes.get( i ) ).retractObject( handle,
                                                                              workingMemory );
        }
    }
}
