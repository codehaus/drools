package org.drools.spring.metadata;

import org.drools.rule.Rule;
import org.drools.spi.KnowledgeHelper;
import org.drools.spring.pojorule.Argument;
import org.drools.spring.pojorule.KnowledgeHelperArgument;

public class KnowledgeHelperArgumentMetadata implements ArgumentMetadata {

    public Class getParameterClass() {
        return KnowledgeHelper.class;
    }

    public Argument createArgument(Rule rule) {
        return new KnowledgeHelperArgument(rule);
    }
}
