package org.drools.spring.metadata;

import org.drools.spring.metadata.AccessAndReturnTypeMethodMetadataSource;
import org.drools.spring.metadata.MethodMetadataSource;

public class AccessAndReturnTypeRuleMethodMetadataSourceTest extends MethodMetadataSourceTestCase {

    protected MethodMetadataSource getSourceUnderTest() {
        return new AccessAndReturnTypeMethodMetadataSource();
    }
}
