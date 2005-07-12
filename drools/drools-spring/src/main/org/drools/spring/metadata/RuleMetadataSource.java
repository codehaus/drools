package org.drools.spring.metadata;

public interface RuleMetadataSource {

    /**
     * If 'pojoClass' is a rule, return RuleMetadata. Otherwise return null.
     */
    RuleMetadata getRuleMetadata(Class pojoClass);

}
