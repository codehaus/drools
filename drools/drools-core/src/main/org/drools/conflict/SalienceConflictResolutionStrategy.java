package org.drools.conflict;

import org.drools.spi.ConflictResolutionStrategy;
import org.drools.spi.Firing;

public class SalienceConflictResolutionStrategy
    implements ConflictResolutionStrategy
{
    private static final SalienceConflictResolutionStrategy INSTANCE = new SalienceConflictResolutionStrategy();

    public static SalienceConflictResolutionStrategy getInstance()
    {
        return INSTANCE;
    }

    public SalienceConflictResolutionStrategy()
    {
        // intentionally left blank
    }

    public int compare(Firing firingOne,
                       Firing firingTwo)
    {
        int salienceOne = firingOne.getRule().getSalience();
        int salienceTwo = firingTwo.getRule().getSalience();

        if ( salienceOne > salienceTwo )
        {
            return -1;
        }

        if ( salienceOne < salienceTwo )
        {
            return 1;
        }

        return 0;
    }
}

