package org.drools.reteoo;

import org.drools.rule.Rule;
import org.drools.spi.Activation;

public class PropagationContext
{
    public static final int ASSERTION = 0;
    public static final int RETRACTION = 1 ;
    public static final int MODIFICATION = 2;
    
    private int type;
    
    private Rule rule;
    
    private Activation activation;

    public PropagationContext( int type, 
                               Rule rule,
                               Activation activation)
    {
        this.type = type;
        this.rule = rule;
        this.activation = activation;
    }

    public Rule getRuleOrigin()
    {
        return this.rule;
    }
    
    public Activation getActivationOrigin()
    {
        return activation;
    }

    public int getType()
    {
        return this.type;
    }
               
}
