package org.drools.reteoo;

/** A <code>ReteTuple</code> created by joining two other <code>Tuples</code>.
 *
 *  @see ReteTuple
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
class JoinTuple
    extends ReteTuple
{
    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     *
     *  @param left The left-side <code>Tuple</code>.
     *  @param right The right-side <code>Tuple</code>.
     */
    JoinTuple(ReteTuple left,
              ReteTuple right)
    {

        putAll( left );
        putAll( right );
    }
}
