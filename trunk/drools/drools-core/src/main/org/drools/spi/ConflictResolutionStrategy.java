package org.drools.spi;

public interface ConflictResolutionStrategy
{
    int compare(Firing firingOne,
                Firing firingTwo);
}
