package org.drools.rule;

import java.util.ArrayList;
import java.util.List;

public class And  extends ConditionalElement
{
    private List children = new ArrayList( );
    
    public void addChild(Object child)
    {
        children.add( child );
    }
    
}