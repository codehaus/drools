package org.drools.util;

import java.util.HashMap;

    public PrimitiveLongMap()
    {
        this(32, 8);
    }

    public PrimitiveLongMap(int bucketSize)
    {
        this(bucketSize, 8);
    }

    public PrimitiveLongMap(int bucketSize,
                            int indexIntervals)
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
        this.firstTopNode = new TopNode( null,
                                         this.lastTopNodeId,
                                         this.bucketSize );
        //create an index of one
        topNodeIndex = new TopNode[]{this.firstTopNode};

        //our first node is also our last node
        this.lastTopNode = this.firstTopNode;
    }

    public boolean put(long key,
                       Object value)
    {
        //keys must be created linearly
        //if the id is greater+1 than the
        //highest recorded key then
        //return false
        if ( key > (this.lastKey + 1) )
        {
            return false;
        }

        //determine TopNode
        long nodeId = (key >> this.doubleShifts);
        TopNode node;
        if ( nodeId > this.lastTopNodeId )
        {
            node = new TopNode( this.lastTopNode,
                                ++this.lastTopNodeId,
                                this.bucketSize );
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

        return node.put( key,
                         value );
    }

    public Object get(long key)
    {
        //determine TopNode
        long nodeId = (key >> this.doubleShifts);

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
        System.arraycopy( this.topNodeIndex,
                          0,
                          newIndex,
                          0,
                          size );
        newIndex[size] = this.lastTopNode;
        this.topNodeIndex = newIndex;
    }

    public static class TopNode
    {
        TopNode nextSibling;
        TopNode previousSibling;
        Object[][] branch;
        int bucketSize;
        int nodeSize;
        int nodeId;
        private int shifts;

        public TopNode(TopNode previousSibling,
                       int nodeId,
                       int bucketSize)
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
            this.bucketSize = bucketSize;
            this.nodeSize = bucketSize * bucketSize;

            //initiate tree;
            this.branch = new Object[bucketSize][bucketSize];
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
            key = key - (this.nodeSize * this.nodeId);

            //determine branch number
            int branchNumber = (int) Math.abs( key >> this.shifts );

            //determine offset
            int offset = branchNumber << this.shifts;

            //get leaf position
            int leaf = (int) key - offset;
            return this.branch[branchNumber][leaf];
        }

        public boolean put(long key,
                           Object value)
        {
            //normalise key
            key = key - (this.nodeSize * this.nodeId);

            //determine branch number
            int branchNumber = (int) Math.abs( key >> this.shifts );

            //determine offset
            int offset = branchNumber << this.shifts;

            //get leaf position
            int leaf = (int) key - offset;
            this.branch[branchNumber][leaf] = value;
            return true;
        }
    }

}