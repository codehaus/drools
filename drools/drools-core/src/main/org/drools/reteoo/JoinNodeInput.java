package org.drools.reteoo;

/*
 $Id: JoinNodeInput.java,v 1.12 2004-09-16 23:43:03 mproctor Exp $

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

import org.drools.AssertionException;
import org.drools.FactException;
import org.drools.FactHandle;
import org.drools.RetractionException;

/** Input <code>TupleSinkImpl</code> for a <code>JoinNodeImpl</code>.
 *
 *  @see JoinNode
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
class JoinNodeInput
    implements TupleSink
{
    // ------------------------------------------------------------
    //     Constants
    // ------------------------------------------------------------

    /** Left-side input. */
    static final int LEFT  = 41;

    /** Right-side input. */
    static final int RIGHT = 42;

    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Join node. */
    private JoinNode joinNode;

    /** Side. */
    private int side;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     *
     *  @param joinNode Join node.
     *  @param side Side marker.
     */
    JoinNodeInput(JoinNode joinNode,
                  int side)
    {
        this.joinNode = joinNode;
        this.side     = side;
    }

    public String toString()
    {
        if ( this.side == LEFT )
        {
            return "LEFT: " + getJoinNode().toString();
        }
        else
        {
            return "RIGHT: " + getJoinNode().toString();
        }
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Retrieve the side marker.
     *
     *  @return The side marker.
     */
    int getSide()
    {
        return this.side;
    }

    /** Retrieve the destination join node.
     *
     *  @return The join node.
     */
    JoinNode getJoinNode()
    {
        return this.joinNode;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //     org.drools.reteoo.impl.TupleSink
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /** Assert a new <code>Tuple</code>.
     *
     *  @param tuple The <code>Tuple</code> being asserted.
     *  @param workingMemory The working memory seesion.
     *
     *  @throws AssertionException If an error occurs while asserting.
     */
    public void assertTuple(ReteTuple tuple,
                            WorkingMemoryImpl workingMemory) throws AssertionException
    {
        if ( this.side == LEFT )
        {
            getJoinNode().assertLeftTuple( tuple,
                                           workingMemory  );
        }
        else
        {
            getJoinNode().assertRightTuple( tuple,
                                            workingMemory );
        }
    }

    /** Retract tuples.
     *
     *  @param key The tuple key.
     *  @param workingMemory The working memory seesion.
     *
     *  @throws RetractionException If an error occurs while retracting.
     */
    public void retractTuples(TupleKey key,
                              WorkingMemoryImpl workingMemory) throws RetractionException
    {
        getJoinNode().retractTuples( key,
                                     workingMemory );
    }

    /** Modify tuples.
     *
     *  @param trigger The root fact object handle.
     *  @param newTuples Modification replacement tuples.
     *  @param workingMemory The working memory session.
     *
     *  @throws FactException If an error occurs while modifying.
     */
    public void modifyTuples(FactHandle trigger,
                             TupleSet newTuples,
                             WorkingMemoryImpl workingMemory) throws FactException
    {
        if ( this.side == LEFT )
        {
            getJoinNode().modifyLeftTuples( trigger,
                                            newTuples,
                                            workingMemory );
        }
        else
        {
            getJoinNode().modifyRightTuples( trigger,
                                             newTuples,
                                             workingMemory );
        }
    }
}
