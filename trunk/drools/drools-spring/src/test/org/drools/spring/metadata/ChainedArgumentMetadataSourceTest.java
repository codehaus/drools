package org.drools.spring.metadata;

import java.lang.reflect.Method;

import junit.framework.TestCase;

import org.easymock.MockControl;
import org.easymock.container.EasymockContainer;

public class ChainedArgumentMetadataSourceTest extends TestCase {

    private static class PojoRule {
        public boolean condition(String foo) { return false; }
    }

    private EasymockContainer mocks = new EasymockContainer();

    private MockControl delegateControl_1 = mocks.createControl(ArgumentMetadataSource.class);
    private ArgumentMetadataSource delegateMock_1 = (ArgumentMetadataSource) delegateControl_1.getMock();
    private MockControl delegateControl_2 = mocks.createControl(ArgumentMetadataSource.class);
    private ArgumentMetadataSource delegateMock_2 = (ArgumentMetadataSource) delegateControl_2.getMock();

    private MockControl argumentMetataControl = mocks.createControl(ArgumentMetadata.class);
    private ArgumentMetadata argumentMetadataMock = (ArgumentMetadata) argumentMetataControl.getMock();

    private Method conditionMethod;

    private ChainedArgumentMetadataSource source = new ChainedArgumentMetadataSource(
            new ArgumentMetadataSource[] { delegateMock_1, delegateMock_2 });

    protected void setUp() throws Exception {
        super.setUp();
        conditionMethod = PojoRule.class.getMethod("condition", new Class[]{String.class});
    }

    public void testNewNullDelegates() throws Exception {
        try {
            ChainedArgumentMetadataSource source = new ChainedArgumentMetadataSource(null);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testNewNullDelegateElement() throws Exception {
        try {
            ChainedArgumentMetadataSource source = new ChainedArgumentMetadataSource(
                    new ArgumentMetadataSource[] { delegateMock_1, null });
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testGetRuleMetadataNull() throws Exception {
        delegateControl_1.expectAndReturn(
                delegateMock_1.getArgumentMetadata(conditionMethod, String.class, 0), null);
        delegateControl_2.expectAndReturn(
                delegateMock_2.getArgumentMetadata(conditionMethod, String.class, 0), null);

        mocks.replay();

        ArgumentMetadata metadata = source.getArgumentMetadata(conditionMethod, String.class, 0);

        mocks.verify();
        assertNull(metadata);
    }

    public void testGetRuleMetadataFromFirstDelegate() throws Exception {
        delegateControl_1.expectAndReturn(
                delegateMock_1.getArgumentMetadata(conditionMethod, String.class, 0), argumentMetadataMock);

        mocks.replay();

        ArgumentMetadata metadata = source.getArgumentMetadata(conditionMethod, String.class, 0);

        mocks.verify();
        assertSame(argumentMetadataMock, metadata);
    }

    public void testGetRuleMetadataFromSecondDelegate() throws Exception {
        delegateControl_1.expectAndReturn(
                delegateMock_1.getArgumentMetadata(conditionMethod, String.class, 0), null);
        delegateControl_2.expectAndReturn(
                delegateMock_2.getArgumentMetadata(conditionMethod, String.class, 0), argumentMetadataMock);

        mocks.replay();

        ArgumentMetadata metadata = source.getArgumentMetadata(conditionMethod, String.class, 0);

        mocks.verify();
        assertSame(argumentMetadataMock, metadata);
    }
}
