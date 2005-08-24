package org.drools.spring.factory;

/*
 * Copyright 2005 (C) The Werken Company. All Rights Reserved.
 *
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. The name "drools" must not be used to endorse or promote products derived
 * from this Software without prior written permission of The Werken Company.
 * For written permission, please contact bob@werken.com.
 *
 * 4. Products derived from this Software may not be called "drools" nor may
 * "drools" appear in their names without prior written permission of The Werken
 * Company. "drools" is a registered trademark of The Werken Company.
 *
 * 5. Due credit should be given to The Werken Company.
 * (http://drools.werken.com/).
 *
 * THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE WERKEN COMPANY OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */


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

