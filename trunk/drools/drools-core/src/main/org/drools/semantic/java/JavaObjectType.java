
package org.drools.semantic.java;

import org.drools.spi.ObjectType;

/** <code>ObjectType</code> implementing Java <code>Class</code> semantics
 *  for object type delineation.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class JavaObjectType implements ObjectType
{
    /** The java class of the type. */
    private Class objectClass;

    /** Construct.
     *
     *  @param objectClass The Java class of this type.
     */
    public JavaObjectType(Class objectClass)
    {
        this.objectClass = objectClass;
    }

    /** Retrieve the Java class of this type.
     */
    public Class getObjectClass()
    {
        return this.objectClass;
    }

    public boolean matches(Object object)
    {
        return getObjectClass().isAssignableFrom( object.getClass() );
    }

    /** Determine if this <code>JavaObjectType</code> is
     *  semantically equal to another.
     *
     *  @param thatObj The object to compare.
     *
     *  @return <code>true</code> if <code>thatObj</code> is
     *          semantically equal to this <code>JavaObjectType</code>.
     */
    public boolean equals(Object thatObj)
    {
        if ( ! ( thatObj instanceof JavaObjectType ) )
        {
            return false;
        }

        JavaObjectType that = (JavaObjectType) thatObj;

        return this.objectClass.equals( that.objectClass );
    }

    /** Produce output suitable for debugging.
     */
    public String toString()
    {
        return "[JavaObjectType: class=" + getObjectClass() +"]";
    }
}
