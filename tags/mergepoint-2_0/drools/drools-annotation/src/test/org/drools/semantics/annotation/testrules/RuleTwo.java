package org.drools.semantics.annotation.testrules;

import org.drools.FactException;
import org.drools.semantics.annotation.*;
import org.drools.spi.KnowledgeHelper;

@DroolsRule()
public class RuleTwo {

    @DroolsCondition
    public boolean condition(@DroolsParameter("fooBar1") FooBar fooBar) {
        return fooBar.getMin() < 10;
    }

    @DroolsConsequence
    public void consequence(KnowledgeHelper knowledgeHelper,
                            @DroolsParameter("fooBar1") FooBar fooBar) throws FactException {
        fooBar.setMin(10);
        fooBar.setMax(100);
        knowledgeHelper.modifyObject(fooBar);
    }
}
