package org.drools.reteoo;

import org.drools.spi.ConflictResolutionStrategy;
import org.drools.spi.Activation;

import java.util.Comparator;

public class ConflictResolutionComparator
    implements Comparator
{
    private ConflictResolutionStrategy strategy;

    public ConflictResolutionComparator(ConflictResolutionStrategy strategy)
    {
        this.strategy = strategy;
    }

    public int compare(Object lhs,
                       Object rhs)
    {
        return this.strategy.compare( (Activation) lhs,
                                      (Activation) rhs );
    }
}
