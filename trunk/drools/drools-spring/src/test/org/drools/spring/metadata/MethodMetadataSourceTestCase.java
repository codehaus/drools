package org.drools.spring.metadata;

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
