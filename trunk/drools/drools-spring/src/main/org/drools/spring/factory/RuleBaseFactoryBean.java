package org.drools.spring.factory;

import java.util.Iterator;
import java.util.List;

import org.drools.DroolsException;
import org.drools.RuleBase;
import org.drools.RuleBaseBuilder;
import org.drools.reteoo.FactHandleFactory;
import org.drools.rule.RuleSet;
import org.drools.spi.ConflictResolver;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class RuleBaseFactoryBean implements FactoryBean, InitializingBean {

    private ConflictResolver conflictResolver;
    private FactHandleFactory factHandleFactory;
    private List ruleSets;
    private RuleBase ruleBase;

    public void setConflictResolver(ConflictResolver conflictResolver) {
        this.conflictResolver = conflictResolver;
    }

    public void setFactHandleFactory(FactHandleFactory factHandleFactory) {
        this.factHandleFactory = factHandleFactory;
    }

    public void setRuleSets(List ruleSets) {
        this.ruleSets = ruleSets;
    }

    public void afterPropertiesSet() throws Exception {
        validateProperties();
        createObject();
    }

    private void validateProperties() {
        if (ruleSets == null || ruleSets.isEmpty()) {
            throw new IllegalArgumentException("ruleSets property not specified or is empty");
        }
        for (Iterator iter = ruleSets.iterator(); iter.hasNext();) {
            Object object = iter.next();
            if (!(object instanceof RuleSet)) {
                throw new IllegalArgumentException("ruleSets item not instanceof RuleSet: "
                        + object.getClass());
            }
        }
    }

    private void createObject() throws DroolsException {
        RuleBaseBuilder builder = new RuleBaseBuilder();
        if (conflictResolver != null) {
            builder.setConflictResolver(conflictResolver);
        }
        if (factHandleFactory != null) {
            builder.setFactHandleFactory(factHandleFactory);
        }
        for (Iterator iter = ruleSets.iterator(); iter.hasNext();) {
            RuleSet ruleSet = (RuleSet) iter.next();
            builder.addRuleSet(ruleSet);
        }
        ruleBase = builder.build();
    }

    public Class getObjectType() {
        return RuleBase.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public Object getObject() throws Exception {
        return ruleBase;
    }
}
