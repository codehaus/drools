
package org.drools.reteoo;

import org.drools.spi.Tuple;

/** A {@link Tuple} created by joining two other <code>Tuples</code>.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class JoinTuple extends ReteTuple
{
    /** Construct.
     *
     *  @param left The left-side <code>Tuple</code>.
     *  @param right The right-side <code>Tuple</code>.
     */
    public JoinTuple(Tuple left,
                     Tuple right)
    {
        this.putAll( left );
        this.putAll( right );
    }
}
