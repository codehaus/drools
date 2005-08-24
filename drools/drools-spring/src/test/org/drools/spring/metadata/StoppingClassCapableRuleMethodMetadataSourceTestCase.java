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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public abstract class StoppingClassCapableRuleMethodMetadataSourceTestCase extends MethodMetadataSourceTestCase {

    private static class PojoSuperSuperClassRule {
        /** @Condition */
        public boolean superSuperCondition(String fact) { return false; }
        /** @Consequence */
        public void superSuperConsequence() { }
    }
    
    private static class PojoSuperClassRule extends PojoSuperSuperClassRule {
        /** @Condition */
        public boolean superCondition(String fact) { return false; }
        /** @Consequence */
        public void superConsequence() { }
    }
    
    private static class PojoSubClassRule extends PojoSuperClassRule {
        /** @Condition */
        public boolean subCondition(String fact) { return false; }
        /** @Consequence */
        public void subConsequence() { }        
    }

    private Method superSuperConditionMethod;
    private Method superSuperConsequenceMethod;
    private Method superConditionMethod;
    private Method superConsequenceMethod;
    private Method subConditionMethod;
    private Method subConsequenceMethod;
    
    private List ruleMethods;
    
    private List getRuleMethods() {
        ArrayList methods = new ArrayList();
        Class ruleClass = PojoSubClassRule.class;
        do {
            methods.addAll(Arrays.asList(ruleClass.getDeclaredMethods()));
            ruleClass = ruleClass.getSuperclass();
        } while (ruleClass != null);
        return methods;
    }
    
    protected void setUp() throws Exception {
        super.setUp();
        
        superSuperConditionMethod = PojoSuperSuperClassRule.class.getDeclaredMethod("superSuperCondition", new Class[]{String.class});
        superSuperConsequenceMethod = PojoSuperSuperClassRule.class.getDeclaredMethod("superSuperConsequence", new Class[0]);
        superConditionMethod = PojoSuperClassRule.class.getDeclaredMethod("superCondition", new Class[]{String.class});
        superConsequenceMethod = PojoSuperClassRule.class.getDeclaredMethod("superConsequence", new Class[0]);
        subConditionMethod = PojoSubClassRule.class.getDeclaredMethod("subCondition", new Class[]{String.class});
        subConsequenceMethod = PojoSubClassRule.class.getDeclaredMethod("subConsequence", new Class[0]);
        
        ruleMethods = getRuleMethods();
    }
    
    private void assertMethods(Set expectedConditionMethods, Set expectedConsequenceMethods) {
        for (Iterator iter = ruleMethods.iterator(); iter.hasNext();) {
            Method method = (Method) iter.next();
            if (expectedConditionMethods.contains(method)) {
                assertEquals(
                        "expected CONDITION",
                        MethodMetadata.METHOD_CONDITION,
                        concreteSource.getMethodMetadata(method).getMethodType());

            } else if (expectedConsequenceMethods.contains(method)) {
                assertEquals(
                        "expected CONSEQUENCE",
                        MethodMetadata.METHOD_CONSEQUENCE,
                        concreteSource.getMethodMetadata(method).getMethodType());
            } else {
                assertNull(
                        "unexpected rule method: " + method,
                        concreteSource.getMethodMetadata(method));
            }            
        }
    }

    public void testStoppingClass() throws Exception {
        Set expectedConditionMethods = new HashSet();
        expectedConditionMethods.add(superConditionMethod);
        expectedConditionMethods.add(subConditionMethod);
        
        Set expectedConsequenceMethods = new HashSet();
        expectedConsequenceMethods.add(superConsequenceMethod);
        expectedConsequenceMethods.add(subConsequenceMethod);
        
        ((StoppingClassCapable)concreteSource).setStoppingClass(PojoSuperSuperClassRule.class);
        assertMethods(expectedConditionMethods, expectedConsequenceMethods);
    }

    public void testDefaultStoppingClass() throws Exception {
        Set expectedConditionMethods = new HashSet();
        expectedConditionMethods.add(superSuperConditionMethod);
        expectedConditionMethods.add(superConditionMethod);
        expectedConditionMethods.add(subConditionMethod);
        
        Set expectedConsequenceMethods = new HashSet();
        expectedConsequenceMethods.add(superSuperConsequenceMethod);
        expectedConsequenceMethods.add(superConsequenceMethod);
        expectedConsequenceMethods.add(subConsequenceMethod);
        
        assertMethods(expectedConditionMethods, expectedConsequenceMethods);
    }
}

