package org.drools;

import org.drools.spi.ObjectType;

public class MockObjectType
    implements ObjectType
{
    private Class type;

    public MockObjectType()
    {
    }

    public MockObjectType(Class type)
    {
        this.type = type;
    }

    public boolean matches(Object object)
    {
        if ( this.type == null )
        {
            return true;
        }

        return this.type.isInstance( object );
    }

    public boolean equals(Object thatObj)
    {
        if ( thatObj instanceof MockObjectType )
        {
            MockObjectType that = (MockObjectType) thatObj;

            if ( this.type == null
                 &&
                 that.type == null )
            {
                return true;
            }

            if ( this.type == null
                 ||
                 that.type == null)
            {
                return false;
            }

            return this.type.equals( that.type );
        }

        return false;
    }

    public int hashCode()
    {
        if ( this.type == null )
        {
            return 0;
        }

        return this.type.hashCode();
    }
}
