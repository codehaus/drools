package org.drools.spi;

import org.drools.FactHandle;
import org.drools.rule.Declaration;
import org.drools.rule.ParameterBindings;

public interface LiteralExpressionConstraint
{       
    public boolean isAllowed(Object object,
                             ConstraintComparator comparator);    
}
