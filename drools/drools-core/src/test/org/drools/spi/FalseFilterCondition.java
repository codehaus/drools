
package org.drools.spi;

public class FalseFilterCondition implements FilterCondition
{
    public boolean isAllowed(Tuple tuple)
    {
        return false;
    }

    public Declaration[] getRequiredTupleMembers()
    {
        return new Declaration[]
            {
                /* empty */
            };
    }
}
