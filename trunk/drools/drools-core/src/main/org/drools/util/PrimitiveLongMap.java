package org.drools.util;

/*
* $Id: PrimitiveLongMap.java,v 1.2 2004-11-14 00:40:45 simon Exp $
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

public class PrimitiveLongMap
{
    private final int indexIntervals;
    private final int intervalShifts;
    private final int bucketSize;
    private final int shifts;
    private final int doubleShifts;
    private final TopNode firstTopNode;
    private long lastKey;
    private int lastTopNodeId;
    private TopNode[] topNodeIndex;
    private TopNode lastTopNode;

    public PrimitiveLongMap()
    {
        this(32, 8);
    }

    public PrimitiveLongMap(int bucketSize)
    {
        this(bucketSize, 8);
    }

    public PrimitiveLongMap(int bucketSize, int indexIntervals )
    {
        //determine number of shifts for intervals
        int i = 1;
        int size = 2;
        while ( size < indexIntervals )
        {
            size = size << 1;
            ++i;
        }
        this.indexIntervals = size;
        this.intervalShifts = i;

        //determine number of shifts for bucketSize
        i = 1;
        size = 2;
        while ( size < bucketSize )
        {
            size = size << 1;
            ++i;
        }
        this.bucketSize = size;
        this.shifts = i;
        this.doubleShifts = this.shifts << 1;

        this.lastKey = 0;
        this.lastTopNodeId = 0;

        //instantiate the first node
        //previous sibling of first node is null
        //next sibling of last node is null
        this.firstTopNode = new TopNode( null, this.lastTopNodeId, this.bucketSize );
        //create an index of one
        topNodeIndex = new TopNode[]{this.firstTopNode};

        //our first node is also our last node
        this.lastTopNode = this.firstTopNode;
    }

    public boolean put(long key, Object value )
    {
        //keys must be created linearly
        //if the id is greater+1 than the
        //highest recorded key then
        //return false
        if ( key > this.lastKey + 1 )
        {
            return false;
        }

        //determine TopNode
        long nodeId = key >> this.doubleShifts;
        TopNode node;
        if ( nodeId > this.lastTopNodeId )
        {
            node = new TopNode( this.lastTopNode, ++this.lastTopNodeId, this.bucketSize );
            this.lastTopNode = node;

            //expand index if new TopNode matches interval
            if ( node.getNodeId( ) % this.indexIntervals == 0 )
            {
                expandIndex( );
            }
        }
        else
        {
            // get nearest node from index
            node = topNodeIndex[(int) Math.abs( nodeId >> intervalShifts )];

            //find node
            for ( int x = node.getNodeId( ); x < nodeId; x++ )
            {
                node = node.getNextSibling( );
            }
        }

        //hold a reference to new id, if we have created a new key id
        if ( key > lastKey )
        {
            this.lastKey = key;
        }

        return node.put( key, value );
    }

    public Object get(long key)
    {
        //determine TopNode
        long nodeId = key >> this.doubleShifts;

        // get nearest node from index
        TopNode node = topNodeIndex[(int) Math.abs( nodeId >> intervalShifts )];

        //find node
        for ( int x = node.getNodeId( ); x < nodeId; x++ )
        {
            node = node.getNextSibling( );
        }

        return node.get( key );
    }

    public void expandIndex()
    {
        //expand the index by 1 node
        int size = this.topNodeIndex.length;
        TopNode[] newIndex = new TopNode[size + 1];
        System.arraycopy( this.topNodeIndex, 0, newIndex, 0, size );
        newIndex[ size] = this.lastTopNode;
        this.topNodeIndex = newIndex;
    }

    static class TopNode
    {
        private final TopNode previousSibling;
        private final Object[][] branch;
        private final int nodeSize;
        private final int nodeId;
        private final int shifts;
        private TopNode nextSibling;

        TopNode(TopNode previousSibling, int nodeId, int bucketSize )
        {
            //determine number of shifts
            int i = 1;
            int size = 2;
            while ( size < bucketSize )
            {
                size = size << 1;
                ++i;
            }
            //make sure bucket size is valid
            bucketSize = size;
            this.shifts = i;

            // create bi-directional link
            this.previousSibling = previousSibling;
            if ( this.previousSibling != null )
            {
                this.previousSibling.setNextSibling( this );
            }
            this.nodeId = nodeId;
            this.nodeSize = bucketSize * bucketSize;

            //initiate tree;
            this.branch = new Object[ bucketSize ][ bucketSize ];
        }

        public int getNodeId()
        {
            return this.nodeId;
        }

        void setNextSibling(TopNode nextSibling)
        {
            this.nextSibling = nextSibling;
        }

        public TopNode getNextSibling()
        {
            return this.nextSibling;
        }

        public TopNode getPreviousSibling()
        {
            return this.previousSibling;
        }

        public Object get(long key)
        {
            //normalise key
            key = key - this.nodeSize * this.nodeId;

            //determine branch number
            int branchNumber = (int) Math.abs( key >> this.shifts );

            //determine offset
            int offset = branchNumber << this.shifts;

            //get leaf position
            int leaf = (int) key - offset;
            return this.branch[ branchNumber ][ leaf ];
        }

        public boolean put(long key, Object value )
        {
            //normalise key
            key = key - this.nodeSize * this.nodeId;

            //determine branch number
            int branchNumber = (int) Math.abs( key >> this.shifts );

            //determine offset
            int offset = branchNumber << this.shifts;

            //get leaf position
            int leaf = (int) key - offset;
            this.branch[ branchNumber ][ leaf ] = value;
            return true;
        }
    }
}