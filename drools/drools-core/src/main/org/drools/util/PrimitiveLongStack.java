package org.drools.util;
/*
* $Id: PrimitiveLongStack.java,v 1.2 2004-11-16 13:52:01 simon Exp $
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

public class PrimitiveLongStack
{
    private final int bucketSize;
    private int currentNodeId;
    private HeapNode currentNode;

    public PrimitiveLongStack()
    {
        this(256);
    }

    public PrimitiveLongStack(int bucketSize)
    {
        this.bucketSize = bucketSize;
        this.currentNodeId = 0;

        //instantiate the first node
        //previous sibling of first node is null
        //next sibling of last node is null
        this.currentNode = new HeapNode( null, this.currentNodeId, this.bucketSize );
    }

    public void push( long value )
    {
        if (this.currentNode.getPosition() == this.bucketSize-1)
        {

            HeapNode node = new HeapNode( this.currentNode, ++this.currentNodeId, this.bucketSize );
            this.currentNode = node;
        }

        this.currentNode.push(value);
    }


    public long pop()
    {
        if (this.currentNode.getPosition() == -1)
        {
            if (this.currentNodeId == 0)
            {
                throw new RuntimeException("Unable to pop");
            }

            HeapNode node = this.currentNode;
            this.currentNode = node.getPreviousSibling();
            this.currentNodeId--;
            node.remove();

        }

        return this.currentNode.pop();
    }

    public boolean isEmpty()
    {
        return this.currentNodeId == 0 && this.currentNode.getPosition( ) == -1;
    }

    private static final class HeapNode
    {
        private final int nodeId;
        private HeapNode nextSibling;
        private HeapNode previousSibling;
        private long[] branch;
        private int lastKey;


        HeapNode(HeapNode previousSibling, int nodeId, int bucketSize )
        {
            // create bi-directional link
            this.previousSibling = previousSibling;
            if ( this.previousSibling != null )
            {
                this.previousSibling.setNextSibling( this );
            }
            this.nodeId = nodeId;
            lastKey = -1;

            //initiate tree;
            this.branch = new long[ bucketSize ];
        }

        public int getNodeId()
        {
            return this.nodeId;
        }

        void setNextSibling(HeapNode nextSibling)
        {
            this.nextSibling = nextSibling;
        }

        public HeapNode getNextSibling()
        {
            return this.nextSibling;
        }

        public HeapNode getPreviousSibling()
        {
            return this.previousSibling;
        }

        public long pop()
        {
            return this.branch[ this.lastKey-- ];
        }

        public boolean push(long value )
        {
            this.branch[ ++this.lastKey ] = value;
            return true;
        }

        public int getPosition()
        {
            return this.lastKey;
        }

        void remove()
        {
            previousSibling.setNextSibling(null);
            this.previousSibling = null;
            this.branch = null;
        }
    }
}