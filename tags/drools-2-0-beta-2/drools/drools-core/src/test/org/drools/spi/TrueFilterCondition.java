
package org.drools.spi;

public class TrueFilterCondition implements FilterCondition
{
    public boolean isAllowed(Tuple tuple)
    {
        return true;
    }

    public Declaration[] getRequiredTupleMembers()
    {
        return new Declaration[]
            {
                /* empty */
            };
    }
}
