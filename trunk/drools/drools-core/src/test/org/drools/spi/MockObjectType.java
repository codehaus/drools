package org.drools.spi;

public class MockObjectType implements ObjectType
{
    private boolean matches;

    public MockObjectType(boolean matches)
    {
        this.matches = matches;
    }

    public boolean matches(Object object)
    {
        return this.matches;
    }
}
