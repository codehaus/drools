package org.drools.spi;

import org.drools.rule.Declaration;

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
