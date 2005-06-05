package org.drools.spring.metadata;

public class NullRuleMetadata implements RuleMetadata {

    public String getName() {
        return null;
    }

    public String getDocumentation() {
        return null;
    }

    public Integer getSalience() {
        return null;
    }

    public Long getDuration() {
        return null;
    }

    public Boolean getNoLoop() {
        return null;
    }
}