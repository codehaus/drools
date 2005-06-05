package org.drools.spring.factory;

import org.drools.spring.metadata.ArgumentMetadataSource;
import org.drools.spring.metadata.BasicRuleMetadata;
import org.drools.spring.metadata.ChainedArgumentMetadataSource;
import org.drools.spring.metadata.ChainedMethodMetadataSource;
import org.drools.spring.metadata.MethodMetadataSource;
import org.drools.spring.metadata.RuleMetadata;
import org.drools.spring.metadata.RuleMetadataSource;
import org.springframework.beans.factory.BeanNameAware;

public class BeanRuleMetadataRuleFactoryBean extends AbstractRuleFactoryBean implements BeanNameAware {

    private MethodMetadataSource methodMetadataSource;
    private ArgumentMetadataSource argumentMetadataSource;

    private BasicRuleMetadata ruleMetadata = new BasicRuleMetadata();

    public void setMethodMetadataSourceChain(MethodMetadataSource[] chain) {
        this.methodMetadataSource = new ChainedMethodMetadataSource(chain);
    }

    public void setArgumentMetadataSourceChain(ArgumentMetadataSource[] chain) {
        this.argumentMetadataSource = new ChainedArgumentMetadataSource(chain);
    }

    public void setName(String name) {
        ruleMetadata.setName(name);
    }

    public void setDocumentation(String documentation) {
        ruleMetadata.setDocumentation(documentation);
    }

    public void setSalience(Integer salience) {
        ruleMetadata.setSalience(salience);
    }

    public void setDuration(Long duration) {
        ruleMetadata.setDuration(duration);
    }

    public void setNoLoop(Boolean noloop) {
        ruleMetadata.setNoLoop(noloop);
    }

    public void setBeanName(String name) {
        if (ruleMetadata.getName() == null) {
            ruleMetadata.setName(name);
        }
    }

    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
        if (methodMetadataSource == null) {
            throw new IllegalArgumentException("'methodMetadataSource' property not specified");
        }
        if (argumentMetadataSource == null) {
            throw new IllegalArgumentException("'argumentMetadataSource' property not specified");
        }
    }

    protected RuleMetadataSource getRuleMetadataSource() {
        return new RuleMetadataSource() {
            public RuleMetadata getRuleMetadata(Class pojoClass) {
                return ruleMetadata;
            }
        };
    }

    protected MethodMetadataSource getMethodMetadataSource() {
        return methodMetadataSource;
    }

    protected ArgumentMetadataSource getArgumentMetadataSource() {
        return argumentMetadataSource;
    }
}
