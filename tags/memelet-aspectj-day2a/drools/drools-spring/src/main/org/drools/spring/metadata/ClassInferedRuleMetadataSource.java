package org.drools.spring.metadata;

public class ClassInferedRuleMetadataSource implements RuleMetadataSource {

    private static class ClassInferedRuleMetadata extends NullRuleMetadata {
        private final String name;

        public ClassInferedRuleMetadata(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }

    public RuleMetadata getRuleMetadata(Class pojoClass) {
        return new ClassInferedRuleMetadata(pojoClass.getName());
    }
}
