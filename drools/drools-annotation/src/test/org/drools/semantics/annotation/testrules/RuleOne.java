package org.drools.semantics.annotation.testrules;

import org.drools.FactException;
import org.drools.semantics.annotation.*;

@DroolsRule
public class RuleOne {
    
    @DroolsCondition
    public boolean condition(@DroolsParameter("fooBar1") FooBar fooBar) {
        return fooBar.getMin() < 10; 
    }
    
    @DroolsCondition
    public boolean condition(@DroolsParameter("fooBar1") FooBar fooBar,
                             @DroolsParameter("fooBar2") FooBar fooBar2) {
        return fooBar.getMin() < fooBar2.getMin(); 
    }
    
    @DroolsConsequence
    public void consequence(Drools drools, 
                            @DroolsParameter("fooBar1") FooBar fooBar) throws FactException {
        fooBar.setMin(10); 
        fooBar.setMax(100); 
        drools.modifyObject(fooBar);
    }
}
