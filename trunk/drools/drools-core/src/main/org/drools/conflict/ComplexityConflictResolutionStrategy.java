package org.drools.conflict;

import org.drools.rule.Rule;
import org.drools.spi.Activation;
import org.drools.spi.ConflictResolutionStrategy;

public class ComplexityConflictResolutionStrategy
    extends SalienceConflictResolutionStrategy
{
    private static final ConflictResolutionStrategy INSTANCE = new ComplexityConflictResolutionStrategy();

    public static ConflictResolutionStrategy getInstance()
    {
        return INSTANCE;
    }

    public ComplexityConflictResolutionStrategy()
    {

    }

    public int compare(Activation activationOne,
                       Activation activationTwo)
    {
        Rule ruleOne = activationOne.getRule();
        Rule ruleTwo = activationTwo.getRule();

        int numConditionsOne = ruleOne.getConditions().length;
        int numConditionsTwo = ruleTwo.getConditions().length;

        if ( numConditionsOne > numConditionsTwo )
        {
            return -1;
        }
        if ( numConditionsOne < numConditionsTwo )
        {
            return 1;
        }
        else
        {
            return super.compare( activationOne,
                                  activationTwo );
        }
    }
}
