package org.drools.reteoo;

public abstract class BaseNode
{
    protected final int id;
        
    public BaseNode(int id)
    {
        super( );
        this.id = id;
    }

    public int getId()
    {
        return this.id;
    }
    
}
