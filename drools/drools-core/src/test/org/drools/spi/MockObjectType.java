package org.drools.spi;

public class MockObjectType implements ObjectType
{
    private boolean alwaysMatch;

    private Class   type;

    public MockObjectType()
    {
        this( false );
    }

    public MockObjectType( boolean alwaysMatch )
    {
        this.alwaysMatch = alwaysMatch;
    }

    public MockObjectType(Class type)
    {
        this.type = type;
    }

    public boolean matches(Object object)
    {
        if ( this.alwaysMatch )
        {
            return true;
        }

        if ( this.type == null )
        {
            return true;
        }

        return this.type.isInstance( object );
    }

    public boolean equals(Object object)
    {
        if ( this == object )
        {
            return true;
        }

        if ( object == null || getClass() != object.getClass() )
        {
            return false;
        }

        MockObjectType other = ( MockObjectType ) object;

        if ( this.type == null && other.type == null )
        {
            return true;
        }

        if ( this.type == null || other.type == null )
        {
            return false;
        }

        return this.type.equals( other.type );
    }

    public int hashCode()
    {
        if ( this.type == null )
        {
            return 0;
        }

        return this.type.hashCode( );
    }
}
