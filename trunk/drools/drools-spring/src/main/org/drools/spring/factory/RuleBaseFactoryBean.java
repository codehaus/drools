package org.drools.spring.factory;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.drools.RuleBase;
import org.drools.RuleBaseBuilder;
import org.drools.reteoo.FactHandleFactory;
import org.drools.rule.RuleSet;
import org.drools.spi.ConflictResolver;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.ListableBeanFactory;

public class RuleBaseFactoryBean implements FactoryBean, BeanFactoryAware {

    private static Log log = LogFactory.getLog(RuleBaseFactoryBean.class);
    
    private ConflictResolver conflictResolver;
    private FactHandleFactory factHandleFactory;
    private boolean autoDetectRuleSets;
    private Set ruleSets;
    private BeanFactory beanFactory;
    
    private RuleBase ruleBase;

    public void setConflictResolver(ConflictResolver conflictResolver) {
        this.conflictResolver = conflictResolver;
    }

    public void setFactHandleFactory(FactHandleFactory factHandleFactory) {
        this.factHandleFactory = factHandleFactory;
    }
    
    public void setAutoDetectRuleSets(boolean autoDetectRuleSets) {
        this.autoDetectRuleSets = autoDetectRuleSets;
    }
    
    public void setRuleSets(Set ruleSets) {
        this.ruleSets = ruleSets;
    }
    
    public void setBeanFactory(BeanFactory factory) throws BeansException {
        this.beanFactory = factory;
    }
    
    private RuleBase createObject() {
        RuleBaseBuilder builder = new RuleBaseBuilder();
        if (conflictResolver != null) {
            builder.setConflictResolver(conflictResolver);
        }
        if (factHandleFactory != null) {
            builder.setFactHandleFactory(factHandleFactory);
        }
        if (ruleSets == null) {
            ruleSets = new HashSet();
        }
        if (autoDetectRuleSets) {
            autoDetectRuleSets(ruleSets);
        }
        for (Iterator iter = ruleSets.iterator(); iter.hasNext();) {
            RuleSet ruleSet = (RuleSet) iter.next();
            try {
                builder.addRuleSet(ruleSet);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return builder.build();
    }

    private void autoDetectRuleSets(Set ruleSets) {
        if (!(beanFactory instanceof ListableBeanFactory)) {
            log.warn("Cannot auto-detect RuleSets, beanFactory is not instanceof ListableBeanFactory: beanFactory.class=" + beanFactory.getClass());
            return;
        }
        ListableBeanFactory factory = (ListableBeanFactory) beanFactory;
        String[] factoryNames = factory.getBeanNamesForType(RuleSetFactoryBean.class);
        for (int i = 0; i < factoryNames.length; i++) {
            String beanName = factoryNames[i].substring(1);
            RuleSet ruleSet = (RuleSet) factory.getBean(beanName);
            ruleSets.add(ruleSet);
        }
    }

    public Class getObjectType() {
        return RuleBase.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public Object getObject() throws Exception {
        if (ruleBase == null) {
            ruleBase = createObject();
        }
        return ruleBase;
    }
}
