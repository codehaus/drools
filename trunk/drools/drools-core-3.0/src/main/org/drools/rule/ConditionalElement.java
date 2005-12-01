package org.drools.rule;

import java.util.ArrayList;
import java.util.List;

public abstract class ConditionalElement
{
    private List children = new ArrayList( );    
    
    public void addChild(Object child)
    {
        children.add( child );
    }
    
    public List getChildren()
    {
        return this.children;
    }            
    
    public int hashCode()
    {
        return children.hashCode();
    }
}
