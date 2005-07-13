package org.drools.spring.factory;

import java.util.Iterator;
import java.util.Set;

import org.drools.DroolsException;
import org.drools.DroolsRuntimeException;
import org.drools.rule.DuplicateRuleNameException;
import org.drools.rule.InvalidRuleException;
import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.spring.metadata.ArgumentMetadataSource;
import org.drools.spring.metadata.ChainedArgumentMetadataSource;
import org.drools.spring.metadata.ChainedMethodMetadataSource;
import org.drools.spring.metadata.ChainedRuleMetadataSource;
import org.drools.spring.metadata.MethodMetadataSource;
import org.drools.spring.metadata.RuleMetadata;
import org.drools.spring.metadata.RuleMetadataSource;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class RuleSetFactoryBean implements FactoryBean, BeanNameAware, InitializingBean {

    private static RuleBuilder builder = new RuleBuilder();
    
    private String name;
    private ChainedRuleMetadataSource ruleMetadataSource;
    private ChainedMethodMetadataSource methodMetadataSource;
    private ChainedArgumentMetadataSource argumentMetadataSource;
    private Set rules;
    
    RuleSet ruleSet;

    public void setName(String name) {
        this.name = name;
    }

    public void setRuleMetadataSourceChain(RuleMetadataSource[] chain) {
        this.ruleMetadataSource = new ChainedRuleMetadataSource(chain);
    }

    public void setMethodMetadataSourceChain(MethodMetadataSource[] chain) {
        this.methodMetadataSource = new ChainedMethodMetadataSource(chain);
    }

    public void setArgumentMetadataSourceChain(ArgumentMetadataSource[] chain) {
        this.argumentMetadataSource = new ChainedArgumentMetadataSource(chain);
    }
    
    public void setRules(Set rules) {
        this.rules = rules;
    }

    public void setBeanName(String name) {
        if (this.name == null) {
            this.name = name;
        }
    }

    public void afterPropertiesSet() throws Exception {
        if (rules == null || rules.isEmpty()) {
            throw new IllegalArgumentException("rules property not set or is empty");
        }
    }

    private RuleSet createObject() throws DuplicateRuleNameException, InvalidRuleException {
        RuleSet ruleSet = new RuleSet(name);
        for (Iterator iter = rules.iterator(); iter.hasNext();) {
            Object rulesItem = iter.next();
            Rule rule = createRule(rulesItem);
            ruleSet.addRule(rule);
        }
        return ruleSet;
    }

    private Rule createRule(Object rulesItem) {
        Object pojo;
        RuleMetadata ruleMetadata;
        if (rulesItem instanceof RuleBean) {
            pojo = ((RuleBean)rulesItem).getPojo();
            ruleMetadata = ((RuleBean)rulesItem).getRuleMetadata();
        } else {
            pojo = rulesItem;
            ruleMetadata = ruleMetadataSource.getRuleMetadata(pojo.getClass());
        }
        Rule rule = new Rule(ruleMetadata.getName());
        setRuleProperties(rule, ruleMetadata);
        // TODO Change builder so it takes these in buildRule method.
        builder.setMethodMetadataSource(methodMetadataSource);
        builder.setArgumentMetadataSource(argumentMetadataSource);
        try {
            // TODO Change everything to DroolsRuntimeException
            return builder.buildRule(rule, pojo);
        } catch (DroolsException e) {
            throw new DroolsRuntimeException(e);
        }
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

    public Object getObject() throws Exception {
        if (ruleSet == null) {
            ruleSet = createObject();
        }
        return ruleSet;
    }

    public Class getObjectType() {
        return RuleSet.class;
    }

    public boolean isSingleton() {
        return true;
    }
}
