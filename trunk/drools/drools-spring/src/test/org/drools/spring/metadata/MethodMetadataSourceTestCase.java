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

import org.drools.spi.KnowledgeHelper;
import org.drools.spring.metadata.MethodMetadata;
import org.drools.spring.metadata.MethodMetadataSource;

public abstract class MethodMetadataSourceTestCase extends TestCase {

    private static class PojoRule {
        private boolean privateBooleanMethod() { return false; }
        private void privateVoidMethod() { }
        public int publicNonVoidAndNonBooleanMethod() {
            // Call these two methods just to get rid the unused-warnings.
            privateBooleanMethod();
            privateVoidMethod();
            return 0;
        }
        public PojoRule publicNonVoidAndNonPrimitiveMethod() { return new PojoRule(); }
        public boolean publicBooleanMethodWithNoParameters() { return false; } // need args to be a condition

        /** @Condition */
        public boolean publicBooleanMethodWithParameters(String fact, String data) { return false; }
        /** @Consequence */
        public void publicVoidMethodWithNoParameters() { }
        /** @Consequence */
        public void publicVoidMethodWithParameters(String fact, String data, KnowledgeHelper kh) { }
    }

    protected Class getPojoRuleClass() {
        return PojoRule.class;
    }

    private Method privateBooleanMethod;
    private Method privateVoidMethod;
    private Method publicNonVoidAndNonBooleanMethod;
    private Method publicNonVoidAndNonPrimitiveMethod;
    private Method publicBooleanMethodWithNoParameters;
    private Method publicBooleanMethodWithParameters;
    private Method publicVoidMethodWithNoParameters;
    private Method publicVoidMethodWithParameters;

    protected abstract MethodMetadataSource getSourceUnderTest();

    protected MethodMetadataSource concreteSource;

    protected void setUp() throws Exception {
        super.setUp();
        
        Class pojoRuleClass = getPojoRuleClass();
        privateBooleanMethod = pojoRuleClass.getDeclaredMethod("privateBooleanMethod", new Class[0]);
        privateVoidMethod = pojoRuleClass.getDeclaredMethod("privateVoidMethod", new Class[0]);
        publicNonVoidAndNonBooleanMethod = pojoRuleClass.getDeclaredMethod("publicNonVoidAndNonBooleanMethod", new Class[0]);
        publicNonVoidAndNonPrimitiveMethod = pojoRuleClass.getDeclaredMethod("publicNonVoidAndNonPrimitiveMethod", new Class[0]);
        publicBooleanMethodWithNoParameters = pojoRuleClass.getDeclaredMethod("publicBooleanMethodWithNoParameters", new Class[0]);
        publicBooleanMethodWithParameters = pojoRuleClass.getDeclaredMethod("publicBooleanMethodWithParameters", new Class[]{String.class, String.class});
        publicVoidMethodWithNoParameters = pojoRuleClass.getDeclaredMethod("publicVoidMethodWithNoParameters", new Class[0]);
        publicVoidMethodWithParameters = pojoRuleClass.getDeclaredMethod("publicVoidMethodWithParameters", new Class[]{String.class, String.class, KnowledgeHelper.class});
        
        concreteSource = getSourceUnderTest();
    }

    public void testNonRuleMethods() throws Exception {
        assertNull(concreteSource.getMethodMetadata(privateBooleanMethod));
        assertNull(concreteSource.getMethodMetadata(privateVoidMethod));
        assertNull(concreteSource.getMethodMetadata(publicNonVoidAndNonBooleanMethod));
        assertNull(concreteSource.getMethodMetadata(publicBooleanMethodWithNoParameters));
    }

    public void testConditionMethods() throws Exception {
        assertEquals(
                "expected CONDITION",
                MethodMetadata.METHOD_CONDITION,
                concreteSource.getMethodMetadata(publicBooleanMethodWithParameters).getMethodType());
    }

    public void testPojoConditionMethods() throws Exception {
        assertEquals(
                "expected POJO_CONDITION",
                MethodMetadata.OBJECT_CONDITION,
                concreteSource.getMethodMetadata(publicNonVoidAndNonPrimitiveMethod).getMethodType());
    }
    
    public void testConsequenceMethods() throws Exception {
        assertEquals(
                "expected CONSEQUENCE",
                MethodMetadata.METHOD_CONSEQUENCE,
                concreteSource.getMethodMetadata(publicVoidMethodWithNoParameters).getMethodType());
        assertEquals(
                "expected CONSEQUENCE",
                MethodMetadata.METHOD_CONSEQUENCE,
                concreteSource.getMethodMetadata(publicVoidMethodWithParameters).getMethodType());
    }
}

