
package org.drools.spi;

/** A typed, named variable for {@link Condition} evaluation.
 *
 *  @see ObjectType
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class Declaration
{
    /** The type of the variable. */
    private ObjectType objectType;

    /** The identifier for the variable. */
    private String     identifier;

    /** Construct.
     *
     *  @param objectType The type of this variable declaration.
     *  @param identifier The name of the variable.
     */
    public Declaration(ObjectType objectType,
                       String identifier)
    {
        this.objectType = objectType;
        this.identifier = identifier;
    }

    /** Retrieve the <code>ObjectType</code> of this <code>Declaration</code>.
     *
     *  @return The <code>ObjectType</code> of this <code>Declaration</code>.
     */
    public ObjectType getObjectType()
    {
        return this.objectType;
    }

    /** Retrieve the variable's identifier.
     *
     *  @return The variable's identifier.
     */
    public String getIdentifier()
    {
        return this.identifier;
    }

    /** Determine if another <code>Declaration</code> is
     *  <b>semantically</b> equivelent to this one.
     */
    public boolean equals(Object thatObj)
    {
        Declaration that = (Declaration) thatObj;

        return ( this.objectType.equals( that.objectType )
                 &&
                 this.identifier.equals( that.identifier ) );
    }

    /** Produce output suitable for debugging.
     */
    public String toString()
    {
        return "[Declaration: identifier='" + getIdentifier() + "'; objectType=" + getObjectType() + "]";
    }
}
