package org.drools.spring.metadata;

public class ChainedRuleMetadataSource implements RuleMetadataSource {

    private final RuleMetadataSource[] delegates;

    public ChainedRuleMetadataSource(RuleMetadataSource[] delegates) {
        if (delegates == null) {
            throw new IllegalArgumentException("delegates argument must not be null");
        }
        for (int i = 0; i < delegates.length; i++) {
            if (delegates[i] == null) {
                throw new IllegalArgumentException("delegates[" + i + "] element must not be null");
            }
        }
        this.delegates = delegates;
    }

    public RuleMetadata getRuleMetadata(Class pojoClass) {
        for (int i = 0; i < delegates.length; i++) {
            RuleMetadata metadata = delegates[i].getRuleMetadata(pojoClass);
            if (metadata != null) {
                return metadata;
            }
        }
        return null;
    }
}
