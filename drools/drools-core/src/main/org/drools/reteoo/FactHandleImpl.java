package org.drools.reteoo;

import org.drools.FactHandle;

class FactHandleImpl
    implements FactHandle
{
    private long id;

    FactHandleImpl(long id)
    {
        this.id = id;
    }

    public boolean equals(Object thatObj)
    {
        if ( thatObj instanceof FactHandleImpl )
        {
            return ((FactHandleImpl)thatObj).id == this.id;
        }

        return false;
    }

    public int hashCode()
    {
        return (int) ( this.id % (long) Integer.MAX_VALUE );
    }

    public String toString()
    {
        return "[FactHandle:" + this.id + "]";
    }
}
