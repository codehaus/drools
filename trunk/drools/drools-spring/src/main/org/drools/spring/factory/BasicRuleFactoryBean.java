package org.drools.spring.factory;

import org.drools.spring.metadata.ArgumentMetadataSource;
import org.drools.spring.metadata.ChainedArgumentMetadataSource;
import org.drools.spring.metadata.ChainedMethodMetadataSource;
import org.drools.spring.metadata.ChainedRuleMetadataSource;
import org.drools.spring.metadata.MethodMetadataSource;
import org.drools.spring.metadata.RuleMetadataSource;

public class BasicRuleFactoryBean extends AbstractRuleFactoryBean {

    private ChainedRuleMetadataSource ruleMetadataSource;
    private ChainedMethodMetadataSource methodMetadataSource;
    private ChainedArgumentMetadataSource argumentMetadataSource;

    public void setRuleMetadataSourceChain(RuleMetadataSource[] chain) {
        this.ruleMetadataSource = new ChainedRuleMetadataSource(chain);
    }

    public void setMethodMetadataSourceChain(MethodMetadataSource[] chain) {
        this.methodMetadataSource = new ChainedMethodMetadataSource(chain);
    }

    public void setArgumentMetadataSourceChain(ArgumentMetadataSource[] chain) {
        this.argumentMetadataSource = new ChainedArgumentMetadataSource(chain);
    }

    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
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
