package org.drools.event;

import org.drools.WorkingMemory;
import org.drools.rule.Rule;
import org.drools.spi.Condition;
import org.drools.spi.Tuple;


public class ConditionTestedEvent
    extends WorkingMemoryEvent
{
    private Rule rule;
    private Condition condition;
    private Tuple tuple;
    private boolean passed;

    public ConditionTestedEvent(WorkingMemory workingMemory,
                                Rule rule,
                                Condition condition,
                                Tuple tuple,
                                boolean passed)
    {
        super( workingMemory );

        this.rule      = rule;
        this.condition = condition;
        this.tuple     = tuple;
        this.passed    = passed;
    }

    public Rule getRule()
    {
        return this.rule;
    }

    public Condition getCondition()
    {
        return this.condition;
    }

    public Tuple getTuple()
    {
        return this.tuple;
    }

    public boolean getPassed()
    {
        return this.passed;
    }

    public String toString()
    {
        return "[ConditionTested: rule=" + this.rule.getName() + "; condition=" + this.condition + "; passed=" + this.passed + "; tuple=" + this.tuple + "]";
    }
}

