package org.drools.spring.factory;

import java.util.Iterator;
import java.util.Set;

import org.drools.rule.DuplicateRuleNameException;
import org.drools.rule.InvalidRuleException;
import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.springframework.beans.factory.BeanNameAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class RuleSetFactoryBean implements FactoryBean, BeanNameAware, InitializingBean {

    private String name;
    private Set rules;
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

    public void afterPropertiesSet() throws Exception {
        createObject();
    }

    private void createObject() throws DuplicateRuleNameException, InvalidRuleException {
        ruleSet = new RuleSet(name);
        for (Iterator iter = rules.iterator(); iter.hasNext();) {
            Rule rule = (Rule) iter.next();
            ruleSet.addRule(rule);
        }
    }

    public Object getObject() throws Exception {
        return ruleSet;
    }

    public Class getObjectType() {
        return RuleSet.class;
    }

    public boolean isSingleton() {
        return true;
    }

}
