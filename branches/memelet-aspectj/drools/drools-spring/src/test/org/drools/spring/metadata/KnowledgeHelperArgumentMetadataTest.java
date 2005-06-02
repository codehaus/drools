package org.drools.spring.metadata;

import junit.framework.TestCase;

import org.drools.rule.Rule;
import org.drools.spring.metadata.KnowledgeHelperArgumentMetadata;
import org.drools.spring.pojorule.Argument;
import org.drools.spring.pojorule.KnowledgeHelperArgument;

public class KnowledgeHelperArgumentMetadataTest extends TestCase {

    public void testCreate() throws Exception {
        Rule rule = new Rule("for-test");
        KnowledgeHelperArgumentMetadata metadata = new KnowledgeHelperArgumentMetadata();

        Argument argument = metadata.createArgument(rule);

        assertTrue(argument instanceof KnowledgeHelperArgument);
    }
}
