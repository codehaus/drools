package org.drools.semantics.annotation.testrules;

import org.drools.FactException;
import org.drools.spi.KnowledgeHelper;
import org.drools.semantics.annotation.Rule;
import org.drools.semantics.annotation.Condition;
import org.drools.semantics.annotation.*;

@Rule
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

    @Condition
    public boolean condition(@Parameter("fooBar1") FooBar fooBar) {
        return fooBar.getMin() > conditionMinValue && fooBar.getMax() < conditionMaxValue;
    }

    @Consequence
    public void consequence(KnowledgeHelper knowledgeHelper,
                            @Parameter("fooBar1") FooBar fooBar) throws FactException {

        fooBar.setValue(consequenceValue);
        knowledgeHelper.retractObject(fooBar);

        String auditComment = knowledgeHelper.getRuleName() + ":" + fooBar.getName();
        auditService.auditValue(auditComment, consequenceValue);
    }
}
