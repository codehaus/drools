package org.drools.spring.metadata;

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
