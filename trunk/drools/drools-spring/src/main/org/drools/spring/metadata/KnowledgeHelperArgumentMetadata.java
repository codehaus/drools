package org.drools.spring.metadata;

import org.drools.rule.Rule;
import org.drools.spring.pojorule.Argument;
import org.drools.spring.pojorule.KnowledgeHelperArgument;

public class KnowledgeHelperArgumentMetadata implements ArgumentMetadata {

    public Argument createArgument(Rule rule) {
        return new KnowledgeHelperArgument(rule);
    }

}
