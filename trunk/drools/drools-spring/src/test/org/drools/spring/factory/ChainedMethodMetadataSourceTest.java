package org.drools.spring.factory;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.drools.spring.metadata.MethodMetadata;
import org.drools.spring.metadata.MethodMetadataSource;
import org.easymock.MockControl;
import org.easymock.container.EasymockContainer;

public class ChainedMethodMetadataSourceTest extends TestCase {

    private static class PojoRule {
        public boolean condition(String foo) { return false; }
    }

    private EasymockContainer mocks = new EasymockContainer();

    private MockControl delegateControl_1 = mocks.createControl(MethodMetadataSource.class);
    private MethodMetadataSource delegateMock_1 = (MethodMetadataSource) delegateControl_1.getMock();
    private MockControl delegateControl_2 = mocks.createControl(MethodMetadataSource.class);
    private MethodMetadataSource delegateMock_2 = (MethodMetadataSource) delegateControl_2.getMock();

    private Method conditionMethod;

    private ChainedMethodMetadataSource source = new ChainedMethodMetadataSource(
            new MethodMetadataSource[] { delegateMock_1, delegateMock_2 });

    protected void setUp() throws Exception {
        super.setUp();
        conditionMethod = PojoRule.class.getMethod("condition", new Class[]{String.class});
    }

    public void testNewNullDelegates() throws Exception {
        try {
            ChainedMethodMetadataSource source = new ChainedMethodMetadataSource(null);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testNewNullDelegateElement() throws Exception {
        try {
            ChainedMethodMetadataSource source = new ChainedMethodMetadataSource(
                    new MethodMetadataSource[] { delegateMock_1, null });
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testGetRuleMetadataNull() throws Exception {
        delegateControl_1.expectAndReturn(
                delegateMock_1.getMethodMetadata(conditionMethod), null);
        delegateControl_2.expectAndReturn(
                delegateMock_2.getMethodMetadata(conditionMethod), null);

        mocks.replay();

        MethodMetadata metadata = source.getMethodMetadata(conditionMethod);

        mocks.verify();
        assertNull(metadata);
    }

    public void testGetRuleMetadataFromFirstDelegate() throws Exception {
        MethodMetadata expectedMetadata = new MethodMetadata(MethodMetadata.CONDITION);
        delegateControl_1.expectAndReturn(
                delegateMock_1.getMethodMetadata(conditionMethod), expectedMetadata);

        mocks.replay();

        MethodMetadata metadata = source.getMethodMetadata(conditionMethod);

        mocks.verify();
        assertSame(expectedMetadata, metadata);
    }

    public void testGetRuleMetadataFromSecondDelegate() throws Exception {
        MethodMetadata expectedMetadata = new MethodMetadata(MethodMetadata.CONDITION);
        delegateControl_1.expectAndReturn(
                delegateMock_1.getMethodMetadata(conditionMethod), null);
        delegateControl_2.expectAndReturn(
                delegateMock_2.getMethodMetadata(conditionMethod), expectedMetadata);

        mocks.replay();

        MethodMetadata metadata = source.getMethodMetadata(conditionMethod);

        mocks.verify();
        assertSame(expectedMetadata, metadata);
    }
}
