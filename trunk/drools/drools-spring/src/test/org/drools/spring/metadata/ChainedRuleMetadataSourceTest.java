package org.drools.spring.metadata;

import junit.framework.TestCase;

import org.easymock.MockControl;
import org.easymock.container.EasymockContainer;

public class ChainedRuleMetadataSourceTest extends TestCase {

    private static class PojoRule {}

    private EasymockContainer mocks = new EasymockContainer();

    private MockControl delegateControl_1 = mocks.createControl(RuleMetadataSource.class);
    private RuleMetadataSource delegateMock_1 = (RuleMetadataSource) delegateControl_1.getMock();
    private MockControl delegateControl_2 = mocks.createControl(RuleMetadataSource.class);
    private RuleMetadataSource delegateMock_2 = (RuleMetadataSource) delegateControl_2.getMock();

    private ChainedRuleMetadataSource source = new ChainedRuleMetadataSource(
            new RuleMetadataSource[] { delegateMock_1, delegateMock_2 });

    public void testNewNullDelegates() throws Exception {
        try {
            ChainedRuleMetadataSource source = new ChainedRuleMetadataSource(null);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testNewNullDelegateElement() throws Exception {
        try {
            ChainedRuleMetadataSource source = new ChainedRuleMetadataSource(
                    new RuleMetadataSource[] { delegateMock_1, null });
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testGetRuleMetadataNull() throws Exception {
        delegateControl_1.expectAndReturn(
                delegateMock_1.getRuleMetadata(PojoRule.class), null);
        delegateControl_2.expectAndReturn(
                delegateMock_2.getRuleMetadata(PojoRule.class), null);

        mocks.replay();

        RuleMetadata metadata = source.getRuleMetadata(PojoRule.class);

        mocks.verify();
        assertNull(metadata);
    }

    public void testGetRuleMetadataFromFirstDelegate() throws Exception {
        RuleMetadata expectedMetadata = new BasicRuleMetadata();
        delegateControl_1.expectAndReturn(
                delegateMock_1.getRuleMetadata(PojoRule.class), expectedMetadata);

        mocks.replay();

        RuleMetadata metadata = source.getRuleMetadata(PojoRule.class);

        mocks.verify();
        assertSame(expectedMetadata, metadata);
    }

    public void testGetRuleMetadataFromSecondDelegate() throws Exception {
        RuleMetadata expectedMetadata = new BasicRuleMetadata();
        delegateControl_1.expectAndReturn(
                delegateMock_1.getRuleMetadata(PojoRule.class), null);
        delegateControl_2.expectAndReturn(
                delegateMock_2.getRuleMetadata(PojoRule.class), expectedMetadata);

        mocks.replay();

        RuleMetadata metadata = source.getRuleMetadata(PojoRule.class);

        mocks.verify();
        assertSame(expectedMetadata, metadata);
    }
}
