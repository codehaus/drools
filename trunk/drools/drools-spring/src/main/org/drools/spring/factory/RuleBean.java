package org.drools.spring.factory;

import org.drools.spring.metadata.BasicRuleMetadata;
import org.drools.spring.metadata.RuleMetadata;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.InitializingBean;

public class RuleBean implements BeanNameAware, InitializingBean {

    private BasicRuleMetadata ruleMetadata = new BasicRuleMetadata();
    private Object pojo;
    
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
    
    public void setPojo(Object pojo) {
        this.pojo = pojo;
    }
    
    public Object getPojo() {
        return pojo;
    }

    public void afterPropertiesSet() throws Exception {
        if (pojo == null) {
            throw new IllegalArgumentException("pojo not set");
        }
    }

    public RuleMetadata getRuleMetadata() {
        return ruleMetadata;
    }
}
