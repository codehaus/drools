package org.drools.spi;

import org.drools.FactHandle;
import org.drools.rule.Declaration;
import org.drools.WorkingMemory;
import org.drools.rule.Rule;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;

public class MockTuple implements Tuple
{
    
    private Rule rule;    
    private WorkingMemory workingMemory;
    
    private Map tuple;

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
}
