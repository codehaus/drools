package org.drools;

public class NoSuchFactObjectException
    extends FactException
{
    private FactHandle handle;

    public NoSuchFactObjectException(FactHandle handle)
    {
        this.handle = handle;
    }

    public FactHandle getHandle()
    {
        return this.handle;
    }

    public String getMessage()
    {
        return "No such fact object for handle: " + getHandle();
    }
}

