package org.drools.reteoo;


abstract class AbstractBetaNodeDecorator
{

    private boolean merge;
    private BetaNode betaNode;
    
    AbstractBetaNodeDecorator(BetaNode betaNode)
    {
        this.betaNode = betaNode;
    }

    boolean shouldMerge()
    {
        return this.merge;
    }    
        
    BetaNode getBetaNode()
    {
        return this.betaNode;
    }
    
}
