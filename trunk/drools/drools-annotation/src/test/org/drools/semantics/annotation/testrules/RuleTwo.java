package org.drools.semantics.annotation.testrules;

import org.drools.FactException;
import org.drools.semantics.annotation.*;
import org.drools.spi.KnowledgeHelper;

@Rule()
public class RuleTwo {

    @Condition
    public boolean condition(@Parameter("fooBar1") FooBar fooBar) {
        return fooBar.getMin() < 10;
    }

    @Consequence
    public void consequence(KnowledgeHelper knowledgeHelper,
                            @Parameter("fooBar1") FooBar fooBar) throws FactException {
        fooBar.setMin(10);
        fooBar.setMax(100);
        knowledgeHelper.modifyObject(fooBar);
    }
}
