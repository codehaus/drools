
package org.drools.spi;

import java.util.Set;

/** A mapping of {@link Declaration}s to current fact values.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public interface Tuple
{
    /** Retrieve the value bound to a particular <code>Declaration</code>.
     *
     *  @param declaration The <code>Declaration</code> key.
     *
     *  @return The currently bound <code>Object</code> value.
     */
    Object get(Declaration declaration);

    /** Bind a value to a particular <code>Declaration</code>.
     *
     *  @param declaration The <code>Declaration</code> key.
     *  @param value The value to bind.
     */
    void put(Declaration declaration,
             Object value);

    /** Retrieve the <code>Collection</code> of all
     *  <code>Declarations</code> active in this tuple.
     *
     *  @return The <code>Collection</code> of all
     *          <code>Declarations</code> in this tuple.
     */
    Set getDeclarations();
        
}
