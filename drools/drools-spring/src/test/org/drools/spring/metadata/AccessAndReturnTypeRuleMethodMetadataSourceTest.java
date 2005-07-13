package org.drools.spring.metadata;

public class AccessAndReturnTypeRuleMethodMetadataSourceTest extends StoppingClassCapableRuleMethodMetadataSourceTestCase {

    protected MethodMetadataSource getSourceUnderTest() {
        return new AccessAndReturnTypeMethodMetadataSource();
    }
}
