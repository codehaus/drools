package org.drools.spi;

import org.drools.rule.Declaration;

import java.util.Set;
import java.util.Map;
import java.util.HashMap;

public class MockTuple implements Tuple
{
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
}
