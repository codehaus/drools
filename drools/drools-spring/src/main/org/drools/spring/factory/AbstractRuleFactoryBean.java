package org.drools.spring.factory;

import org.drools.DroolsException;
import org.drools.rule.Rule;
import org.drools.spring.metadata.ArgumentMetadataSource;
import org.drools.spring.metadata.MethodMetadataSource;
import org.drools.spring.metadata.RuleMetadata;
import org.drools.spring.metadata.RuleMetadataSource;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public abstract class AbstractRuleFactoryBean implements FactoryBean, InitializingBean {

    private static RuleBuilder builder = new RuleBuilder();

    private Object pojo;
    private Rule rule;

    protected abstract RuleMetadataSource getRuleMetadataSource();
    protected abstract MethodMetadataSource getMethodMetadataSource();
    protected abstract ArgumentMetadataSource getArgumentMetadataSource();

    public final void setPojo(Object pojo) {
        this.pojo = pojo;
    }

    public void afterPropertiesSet() throws Exception {
        validateProperties();
        createObject();
    }

    private void validateProperties() {
        if (pojo == null) {
            throw new IllegalArgumentException("pojo property not set");
        }
    }

    private void createObject() throws DroolsException {
        RuleMetadata ruleMetadata = getRuleMetadataSource().getRuleMetadata(pojo.getClass());
        rule = new Rule(ruleMetadata.getName());
        setRuleProperties(rule, ruleMetadata);
        // TODO Change builder so it takes these in populateRule method.
        builder.setMethodMetadataSource(getMethodMetadataSource());
        builder.setArgumentMetadataSource(getArgumentMetadataSource());
        rule = builder.buildRule(rule, pojo);
    }

    private void setRuleProperties(Rule rule, RuleMetadata ruleMetadata) {
        if (ruleMetadata.getDocumentation() != null) {
            rule.setDocumentation(ruleMetadata.getDocumentation());
        }
        if (ruleMetadata.getSalience() != null) {
            rule.setSalience(ruleMetadata.getSalience().intValue());
        }
        if (ruleMetadata.getDuration() != null) {
            rule.setDuration(ruleMetadata.getDuration().longValue());
        }
        if (ruleMetadata.getNoLoop() != null) {
            rule.setNoLoop(ruleMetadata.getNoLoop().booleanValue());
        }
    }

    public final Class getObjectType() {
        return Rule.class;
    }

    public final boolean isSingleton() {
        return true;
    }

    public final Object getObject() throws Exception {
        return rule;
    }

}
