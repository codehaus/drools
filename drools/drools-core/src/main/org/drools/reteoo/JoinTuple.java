
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

        // System.err.println( "\n\n\n\n left = " + left.getRootFactObjects() );
        // System.err.println( "\n\n\n\n right = " + right.getRootFactObjects() );
        this.putAll( left );
        this.putAll( right );

        addAllRootFactObjects( left.getRootFactObjects() );
        addAllRootFactObjects( right.getRootFactObjects() );
    }
}
