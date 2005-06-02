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

    protected static Method privateBooleanMethod;
    protected static Method privateVoidMethod;
    protected static Method publicNonVoidAndNonBooleanMethod;
    protected static Method publicBooleanMethodWithNoParameters;
    protected static Method publicBooleanMethodWithParameters;
    protected static Method publicVoidMethodWithNoParameters;
    protected static Method publicVoidMethodWithParameters;

    private static boolean isStaticSetUp;

    protected abstract MethodMetadataSource getSourceUnderTest();

    private MethodMetadataSource concreteSource;

    protected void setUp() throws Exception {
        super.setUp();
        staticSetUp();
        concreteSource = getSourceUnderTest();
    }

    protected void staticSetUp() throws Exception {
        if (!isStaticSetUp) {
            try {
                Class pojoRuleClass = getPojoRuleClass();
                privateBooleanMethod = pojoRuleClass.getDeclaredMethod("privateBooleanMethod", new Class[0]);
                privateVoidMethod = pojoRuleClass.getDeclaredMethod("privateVoidMethod", new Class[0]);
                publicNonVoidAndNonBooleanMethod = pojoRuleClass.getDeclaredMethod("publicNonVoidAndNonBooleanMethod", new Class[0]);
                publicBooleanMethodWithNoParameters = pojoRuleClass.getDeclaredMethod("publicBooleanMethodWithNoParameters", new Class[0]);
                publicBooleanMethodWithParameters = pojoRuleClass.getDeclaredMethod("publicBooleanMethodWithParameters", new Class[]{String.class, String.class});
                publicVoidMethodWithNoParameters = pojoRuleClass.getDeclaredMethod("publicVoidMethodWithNoParameters", new Class[0]);
                publicVoidMethodWithParameters = pojoRuleClass.getDeclaredMethod("publicVoidMethodWithParameters", new Class[]{String.class, String.class, KnowledgeHelper.class});
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            isStaticSetUp = true;
        }
    }

    public void testNonRuleMethods() throws Exception {
        assertNull(concreteSource.getMethodMetadata(privateBooleanMethod));
        assertNull(concreteSource.getMethodMetadata(publicNonVoidAndNonBooleanMethod));
        assertNull(concreteSource.getMethodMetadata(publicBooleanMethodWithNoParameters));
    }

    public void testConditionMethods() throws Exception {
        assertEquals(
                "expected CONDITION",
                MethodMetadata.CONDITION,
                concreteSource.getMethodMetadata(publicBooleanMethodWithParameters).getMethodType());
    }

    public void testConsequenceMethods() throws Exception {
        assertEquals(
                "expected CONSEQUENCE",
                MethodMetadata.CONSEQUENCE,
                concreteSource.getMethodMetadata(publicVoidMethodWithNoParameters).getMethodType());
        assertEquals(
                "expected CONSEQUENCE",
                MethodMetadata.CONSEQUENCE,
                concreteSource.getMethodMetadata(publicVoidMethodWithParameters).getMethodType());
    }
}
