package org.drools;

public class MockFactHandle
    implements FactHandle
{
    private int id;

    public MockFactHandle(int id)
    {
        this.id = id;
    }

    public String toExternalForm()
    {
        return "[fact:" + id + "]";
    }

    public int hashCode()
    {
        return id;
    }

    public boolean equals(Object thatObj)
    {
        if ( thatObj instanceof MockFactHandle )
        {
            return ((MockFactHandle)thatObj).id == this.id;
        }

        return false;
    }
}
