package org.drools.spi;

public class MockObjectType implements ObjectType
{
    private boolean predetermined;

    private boolean matches;

    private Class   type;

    public MockObjectType(boolean matches)
    {
        this.predetermined = true;
        this.matches = matches;
    }

    public MockObjectType()
    {

    }    

    public MockObjectType(Class type)
    {
        this.type = type;
    }

    public boolean matches(Object object)
    {
        if ( this.predetermined )
        {
            return this.predetermined;
        }

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
            MockObjectType that = ( MockObjectType ) thatObj;

            if ( this.type == null && that.type == null )
            {
                return true;
            }

            if ( this.type == null || that.type == null )
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

        return this.type.hashCode( );
    }

}