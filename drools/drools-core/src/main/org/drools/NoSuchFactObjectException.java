package org.drools;

public class NoSuchFactObjectException
    extends FactException
{
    private FactHandle handle;

    public NoSuchFactObjectException(FactHandle handle)
    {
        this.handle = handle;
    }

    public FactHandle getFactHandle()
    {
        return this.handle;
    }

    public String getMessage()
    {
        return "no such fact object for handle: " + getFactHandle().toExternalForm();
    }
}

