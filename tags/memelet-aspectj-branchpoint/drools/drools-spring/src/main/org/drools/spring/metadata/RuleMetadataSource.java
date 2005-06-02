package org.drools.spring.metadata;

public interface RuleMetadataSource {

    RuleMetadata getRuleMetadata(Class pojoClass);

}
