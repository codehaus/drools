package org.drools.conflict;

import org.drools.spi.ConflictResolutionStrategy;
import org.drools.spi.Activation;

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

    public int compare(Activation activationOne,
                       Activation activationTwo)
    {
        int salienceOne = activationOne.getRule().getSalience();
        int salienceTwo = activationTwo.getRule().getSalience();

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

