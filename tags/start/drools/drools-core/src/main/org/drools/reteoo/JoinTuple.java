
package org.drools.reteoo;

import org.drools.spi.Tuple;

public class JoinTuple extends ReteTuple
{
    public JoinTuple(Tuple left,
                     Tuple right)
    {
        this.putAll( left );
        this.putAll( right );
    }
}
