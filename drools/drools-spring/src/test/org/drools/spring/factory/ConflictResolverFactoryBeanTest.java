package org.drools.spring.factory;
import java.util.ArrayList;

import junit.framework.TestCase;

import org.drools.conflict.FifoConflictResolver;
import org.drools.spi.Activation;
import org.drools.spi.ConflictResolver;
import org.drools.spring.factory.ConflictResolverFactoryBean;
import org.easymock.MockControl;
import org.easymock.container.EasymockContainer;

public class ConflictResolverFactoryBeanTest extends TestCase {

    private EasymockContainer mocks = new EasymockContainer();

    private ConflictResolverFactoryBean factory = new ConflictResolverFactoryBean();

    private MockControl setMockResolverStrategy() throws Exception {
        MockControl resolverControl = mocks.createControl(ConflictResolver.class);
        final ConflictResolver resolverMock = (ConflictResolver) resolverControl.getMock();
        factory.setResolverStrategies(new ArrayList() {{
            add(resolverMock);
        }});
        return resolverControl;
    }

    public void testGetObjectType() throws Exception {
        setMockResolverStrategy();
        factory.afterPropertiesSet();

        assertSame(ConflictResolver.class, factory.getObjectType());
    }

    public void testIsSingleton() throws Exception {
        setMockResolverStrategy();
        factory.afterPropertiesSet();

        assertTrue(factory.isSingleton());
        assertSame(factory.getObject(), factory.getObject());
    }

    public void testNoResolverStrategies() throws Exception {
        try {
            factory.afterPropertiesSet();
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testResolverStrategyNotInstanceOfConflictResolver() throws Exception {
        factory.setResolverStrategies(new ArrayList() {{
            add(FifoConflictResolver.getInstance());
            add(new Object());
        }});
        try {
            factory.afterPropertiesSet();
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testSetResolverStraties() throws Exception {
        MockControl leftActivationControl = mocks.createControl(Activation.class);
        Activation leftActivationMock = (Activation) leftActivationControl.getMock();
        MockControl rightActivationControl = mocks.createControl(Activation.class);
        Activation rightActivationMock = (Activation) rightActivationControl.getMock();

        MockControl resolverControl_1 = mocks.createControl(ConflictResolver.class);
        final ConflictResolver resolverMock_1 = (ConflictResolver) resolverControl_1.getMock();
        MockControl resolverControl_2 = mocks.createControl(ConflictResolver.class);
        final ConflictResolver resolverMock_2 = (ConflictResolver) resolverControl_2.getMock();

        // Return value needs to be zero for the next resolver to be invoked
        resolverControl_1.expectAndReturn(resolverMock_1.compare(leftActivationMock, rightActivationMock), 0);
        resolverControl_2.expectAndReturn(resolverMock_2.compare(leftActivationMock, rightActivationMock), 0);

        factory.setResolverStrategies(new ArrayList() {{
            add(resolverMock_1);
            add(resolverMock_2);
        }});

        mocks.replay();
        factory.afterPropertiesSet();

        ConflictResolver resolver = (ConflictResolver) factory.getObject();
        resolver.compare(leftActivationMock, rightActivationMock);

        mocks.verify();
    }
}
