package org.drools.spring.metadata;

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



import java.lang.reflect.Method;

import junit.framework.TestCase;

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
            new ChainedMethodMetadataSource(null);
            fail("expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    public void testNewNullDelegateElement() throws Exception {
        try {
            new ChainedMethodMetadataSource(
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
        MethodMetadata expectedMetadata = new MethodMetadata(MethodMetadata.METHOD_CONDITION);
        delegateControl_1.expectAndReturn(
                delegateMock_1.getMethodMetadata(conditionMethod), expectedMetadata);

        mocks.replay();

        MethodMetadata metadata = source.getMethodMetadata(conditionMethod);

        mocks.verify();
        assertSame(expectedMetadata, metadata);
    }

    public void testGetRuleMetadataFromSecondDelegate() throws Exception {
        MethodMetadata expectedMetadata = new MethodMetadata(MethodMetadata.METHOD_CONDITION);
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

