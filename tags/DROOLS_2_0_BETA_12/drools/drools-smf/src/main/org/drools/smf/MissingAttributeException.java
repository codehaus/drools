package org.drools.smf;

public class MissingAttributeException
    extends FactoryException
{
    private String name;

    public MissingAttributeException(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return this.name;
    }

    public String getMessage()
    {
        return "missing attribute '" + getName() + "'";
    }
}
