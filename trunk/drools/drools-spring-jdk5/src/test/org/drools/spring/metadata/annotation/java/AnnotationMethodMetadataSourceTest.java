package org.drools.spring.metadata.annotation.java;

import org.drools.spi.KnowledgeHelper;
import org.drools.spring.metadata.MethodMetadataSource;
import org.drools.spring.metadata.MethodMetadataSourceTestCase;
import org.drools.spring.metadata.annotation.java.AnnotationMethodMetadataSource;
import org.drools.spring.metadata.annotation.java.Condition;
import org.drools.spring.metadata.annotation.java.Consequence;

public class AnnotationMethodMetadataSourceTest extends MethodMetadataSourceTestCase {

    private static class PojoRule {
        private boolean privateBooleanMethod() { return false; }
        private void privateVoidMethod() { }
        public int publicNonVoidAndNonBooleanMethod() { return 0; }
        public boolean publicBooleanMethodWithNoParameters() { return false; }

        @Condition
        public boolean publicBooleanMethodWithParameters(String fact, String data) { return false; }
        @Consequence
        public void publicVoidMethodWithNoParameters() { }
        @Consequence
        public void publicVoidMethodWithParameters(String fact, String data, KnowledgeHelper kh) { }
    }

    @Override
    protected Class getPojoRuleClass() {
        return PojoRule.class;
    }

    @Override
    protected MethodMetadataSource getSourceUnderTest() {
        return new AnnotationMethodMetadataSource();
    }
}
