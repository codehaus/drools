package org.drools.rule;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


public abstract class ConditionalElement implements Cloneable
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

    /**
     * Clones all Conditional Elements but references the expressions
     * 
     * @param e1
     * @param e2
     * @return
     */
    public Object clone()
    {
        ConditionalElement cloned = null;
        
        try
        {
            cloned = (ConditionalElement) this.getClass().newInstance();
        }
        catch ( InstantiationException e )
        {
            throw new RuntimeException("Could not clone '" + this.getClass().getName() + "'" );
        }
        catch ( IllegalAccessException e )
        {
            throw new RuntimeException("Could not clone '" + this.getClass().getName() + "'" );
        }
        
        for (Iterator it = this.children.iterator(); it.hasNext();)
        {
            Object object = it.next();
            if (object instanceof ConditionalElement)
            {
                object = ( (ConditionalElement) object).clone();
            }
            cloned.addChild( object );
                       
        }
        
        return cloned;        
    }
    
}
