package org.drools.semantics.annotation.model;

import junit.framework.TestCase;

import org.drools.rule.Rule;
import org.drools.semantics.annotation.model.KnowledgeHelperArgumentSource;
import org.drools.spi.DefaultKnowledgeHelper;
import org.drools.spi.KnowledgeHelper;
import org.drools.spi.Tuple;
import org.easymock.container.EasymockContainer;
import org.easymock.container.EasymockContainer.Mock;

public class KnowledgeHelperArgumentSourceTest extends TestCase {
    private EasymockContainer mocks = new EasymockContainer();

    private Mock<Rule> mockRule = mocks.createMock(Rule.class);
    private Mock<Tuple> mockTuple = mocks.createMock(Tuple.class);

    public void testConstructionNullRule() {
        try {
            KnowledgeHelperArgumentSource arg = new KnowledgeHelperArgumentSource(null);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testGetValue() {
        KnowledgeHelperArgumentSource arg = new KnowledgeHelperArgumentSource(mockRule.object);
        mocks.replay();

        KnowledgeHelper drools = arg.getValue(mockTuple.object);

        // TODO Asserting the instanceof is a bit weak.
        assertTrue(drools instanceof DefaultKnowledgeHelper);
        mocks.verify();
    }
}
