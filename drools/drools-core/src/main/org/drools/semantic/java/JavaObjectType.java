
package org.drools.semantic.java;

import org.drools.spi.ObjectType;

public class JavaObjectType implements ObjectType
{
    private Class objectClass;

    public JavaObjectType(Class objectClass)
    {
        this.objectClass = objectClass;
    }

    public Class getObjectClass()
    {
        return this.objectClass;
    }

    public boolean matches(Object object)
    {
        return getObjectClass().isAssignableFrom( object.getClass() );
    }

    public boolean equals(Object thatObj)
    {
        if ( ! ( thatObj instanceof JavaObjectType ) )
        {
            return false;
        }

        JavaObjectType that = (JavaObjectType) thatObj;

        return this.objectClass.equals( that.objectClass );
    }

    public String toString()
    {
        return "[JavaObjectType: class=" + getObjectClass() +"]";
    }
}
