
package org.drools.reteoo;

/** A {@link ReteTuple} created by joining two other <code>Tuples</code>.
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
    public JoinTuple(ReteTuple left,
                     ReteTuple right)
    {

        this.putAllKeyColumns( left.getKeyColumns() );
        this.putAllKeyColumns( right.getKeyColumns() );

        this.putAllOtherColumns( left.getOtherColumns() );
        this.putAllOtherColumns( right.getOtherColumns() );
    }
}
