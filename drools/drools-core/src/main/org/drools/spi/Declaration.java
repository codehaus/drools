
package org.drools.spi;

/** A typed, named variable for {@link Condition} evaluation.
 *
 *  @see ObjectType
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class Declaration
{
    private ObjectType objectType;
    private String     identifier;

    public Declaration(ObjectType objectType,
                       String identifier)
    {
        this.objectType = objectType;
        this.identifier = identifier;
    }

    public ObjectType getObjectType()
    {
        return this.objectType;
    }

    public String getIdentifier()
    {
        return this.identifier;
    }

    public boolean equals(Object thatObj)
    {
        Declaration that = (Declaration) thatObj;

        return ( this.objectType.equals( that.objectType )
                 &&
                 this.identifier.equals( that.identifier ) );
    }

    public String toString()
    {
        return "[Declaration: identifier='" + getIdentifier() + "'; objectType=" + getObjectType() + "]";
    }
}
