package org.drools.semantics.annotation.testrules;

import org.drools.FactException;
import org.drools.semantics.annotation.DroolsRule;
import org.drools.semantics.annotation.DroolsCondition;
import org.drools.semantics.annotation.*;

@DroolsRule
public class InjectedRule 
{   
    private int conditionMinValue;
    private int conditionMaxValue;
    private int consequenceValue;
    private AuditService auditService;
    
    public void setAuditService(AuditService service) {
        this.auditService = service;
    }
    
    public void setConditionMinValue(int value) {
        this.conditionMinValue = value;
    }
    
    public void setConditionMaxValue(int value) {
        this.conditionMaxValue = value;
    }
    
    public void setConsequenceValue(int value) {
        this.consequenceValue = value;
    }
    
    @DroolsCondition
    public boolean condition(@DroolsParameter("fooBar1") FooBar fooBar) {
        return fooBar.getMin() > conditionMinValue && fooBar.getMax() < conditionMaxValue; 
    }
    
    @DroolsConsequence
    public void consequence(DroolsContext drools, 
                            @DroolsParameter("fooBar1") FooBar fooBar) throws FactException {
        
        fooBar.setValue(consequenceValue); 
        drools.retractObject(fooBar);
        
        String auditComment = drools.getRuleName() + ":" + fooBar.getName();
        auditService.auditValue(auditComment, consequenceValue);
    }
}
