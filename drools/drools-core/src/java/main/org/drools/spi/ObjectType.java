
package org.drools.spi;

/** Semantic object type differentiator.
 *
 *  @see Declaration
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public interface ObjectType
{
    /** Determine if the passed <code>Object</code>
     *  belongs to the object type defined by this
     *  <code>objectType</code> instance.
     *
     *  @param object The <code>Object</code> to test.
     *
     *  @return <code>true</code> if the <code>Object</code>
     *          matches this object type, else <code>false</code>.
     */
    boolean matches(Object object);
}
