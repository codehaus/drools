package org.drools.spi;

public interface ConflictResolutionStrategy
{
    int compare(Activation activationOne,
                Activation activationTwo);
}
