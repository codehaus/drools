package org.drools.spring.pojorule;

import org.drools.rule.Rule;
import org.drools.spi.DefaultKnowledgeHelper;
import org.drools.spi.Tuple;

public class KnowledgeHelperArgument implements Argument {

    private final Rule rule;

    public KnowledgeHelperArgument(Rule rule) {
        this.rule = rule;
    }

    public Object getValue(Tuple tuple) {
        return new DefaultKnowledgeHelper(rule, tuple);
    }
}
