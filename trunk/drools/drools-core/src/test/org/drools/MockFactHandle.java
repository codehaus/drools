package org.drools;

public class MockFactHandle implements FactHandle
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

    public boolean equals( Object object )
    {
        if ( this == object )
        {
            return true;
        }

        if ( object == null || getClass( ) != object.getClass( ) )
        {
            return false;
        }

        return ( ( MockFactHandle ) object ).id == this.id;
    }

    public long getId()
    {
        return this.id;
    }
}