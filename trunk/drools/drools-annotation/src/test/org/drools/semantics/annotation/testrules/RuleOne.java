package org.drools.semantics.annotation.testrules;

import org.drools.FactException;
import org.drools.semantics.annotation.*;

@Drools.Rule
public class RuleOne {
    
    @Drools.Condition
    public boolean condition(@Drools.Parameter("fooBar1") FooBar fooBar) {
        return fooBar.getMin() < 10; 
    }
    
    @Drools.Condition
    public boolean condition(@Drools.Parameter("fooBar1") FooBar fooBar,
                             @Drools.Parameter("fooBar2") FooBar fooBar2) {
        return fooBar.getMin() < fooBar2.getMin(); 
    }
    
    @Drools.Consequence
    public void consequence(DroolsContext drools, 
                            @Drools.Parameter("fooBar1") FooBar fooBar) throws FactException {
        fooBar.setMin(10); 
        fooBar.setMax(100); 
        drools.modifyObject(fooBar);
    }
}
