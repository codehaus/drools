package org.drools.spring.factory;

import java.util.Iterator;
import java.util.Set;

import org.drools.rule.DuplicateRuleNameException;
import org.drools.rule.InvalidRuleException;
import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class RuleSetFactoryBean implements FactoryBean, BeanNameAware, BeanFactoryAware, InitializingBean {

    private String name;
    private Set rules;
    private BeanFactory beanFactory;
    RuleSet ruleSet;

    public void setName(String name) {
        this.name = name;
    }

    public void setRules(Set rules) {
        this.rules = rules;
    }

    public void setBeanName(String name) {
        if (name == null) {
            this.name = name;
        }
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }

    public void afterPropertiesSet() throws Exception {
        if (rules == null || rules.isEmpty()) {
            throw new IllegalArgumentException("rules property not specified or is empty");
        }
    }

    private RuleSet createObject() throws DuplicateRuleNameException, InvalidRuleException {
        RuleSet ruleSet = new RuleSet(name);
        for (Iterator iter = rules.iterator(); iter.hasNext();) {
            Object ruleOrName = iter.next();
            Rule rule;
            if (ruleOrName instanceof Rule) {
                rule = (Rule)ruleOrName;
            } else if (ruleOrName instanceof String) {
                rule = (Rule) beanFactory.getBean((String)ruleOrName);
            } else {
                throw new IllegalArgumentException("Rules property must contain either Rule instances or Rule bean names");
            }
            ruleSet.addRule(rule);
        }
        return ruleSet;
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
