package org.drools;

import javax.rules.Handle;

public class FactHandle
    implements Handle
{
    private long id;

    FactHandle(long id)
    {
        this.id = id;
    }

    public boolean equals(Object thatObj)
    {
        if ( thatObj instanceof FactHandle )
        {
            return ((FactHandle)thatObj).id == this.id;
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
