package org.drools.spi;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.drools.FactHandle;
import org.drools.WorkingMemory;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;

public class MockTuple implements Tuple
{
    private Rule rule;
    private WorkingMemory workingMemory;

    private Map tuple;
    
    private long mostRecentTimeStamp;
    private long leastRecentTimeStamp;    
    private long[] conditionTimeStamps;

    public MockTuple()
    {
        this.tuple = new HashMap();
    }

    public Object get(Declaration declaration)
    {
        return this.tuple.get( declaration );
    }

    public void put(Declaration declaration,
                    Object value)
    {
        this.tuple.put( declaration,
                        value );
    }

    public Set getDeclarations()
    {
        return this.tuple.keySet();
    }

    public FactHandle getFactHandleForObject(Object object)
    {
        return null;
    }

    public void setRule(Rule rule)
    {
        this.rule = rule;
    }

    public Rule getRule()
    {
        return this.rule;
    }

    public void setWorkingMemory(WorkingMemory workingMemory)
    {
        this.workingMemory = workingMemory;
    }

    public WorkingMemory getWorkingMemory()
    {
        return this.workingMemory;
    }
    
    public void setMostRecentFactTimeStamp(long timeStamp)
    {
        this.mostRecentTimeStamp = timeStamp;
    }

    public void setLeastRecentFactTimeStamp(long timeStamp)
    {
        this.leastRecentTimeStamp = timeStamp;
    }

    public void setConditionTimeStamps(long[] timeStamps)
    {
        this.conditionTimeStamps = timeStamps;
    }    

    public long getMostRecentFactTimeStamp()
    {
        return this.mostRecentTimeStamp;
    }

    public long getLeastRecentFactTimeStamp()
    {
        return this.leastRecentTimeStamp;
    }
 
    public void setConditionTimeStamp(int order, long timeStamp)
    {
        this.conditionTimeStamps[order] = timeStamp;
    }    

    public long getConditionTimeStamp(int order)
    {
        return this.conditionTimeStamps[order];
    }        

    public long[] getConditionTimeStamps()
    {
        return this.conditionTimeStamps;
    }    
}
