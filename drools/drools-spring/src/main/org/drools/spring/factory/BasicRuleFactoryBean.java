package org.drools.spring.factory;

import org.drools.spring.metadata.ArgumentMetadataSource;
import org.drools.spring.metadata.MethodMetadataSource;
import org.drools.spring.metadata.RuleMetadataSource;

public class BasicRuleFactoryBean extends AbstractRuleFactoryBean {

    private RuleMetadataSource ruleMetadataSource;
    private MethodMetadataSource methodMetadataSource;
    private ArgumentMetadataSource argumentMetadataSource;

    public void setRuleMetadataSource(RuleMetadataSource ruleMetadataSource) {
        this.ruleMetadataSource = ruleMetadataSource;
    }

    public void setMethodMetadataSource(MethodMetadataSource methodMetadataSource) {
        this.methodMetadataSource = methodMetadataSource;
    }

    public void setArgumentMetadataSource(ArgumentMetadataSource argumentMetadataSource) {
        this.argumentMetadataSource = argumentMetadataSource;
    }

    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        validateProperties();
    }

    private void validateProperties() {
        if (ruleMetadataSource == null) {
            throw new IllegalArgumentException("'ruleMetadataSource' property not specified");
        }
        if (methodMetadataSource == null) {
            throw new IllegalArgumentException("'methodMetadataSource' property not specified");
        }
        if (argumentMetadataSource == null) {
            throw new IllegalArgumentException("'argumentMetadataSource' property not specified");
        }
    }

    protected RuleMetadataSource getRuleMetadataSource() {
        return ruleMetadataSource;
    }

    protected MethodMetadataSource getMethodMetadataSource() {
        return methodMetadataSource;
    }

    protected ArgumentMetadataSource getArgumentMetadataSource() {
        return argumentMetadataSource;
    }
}
