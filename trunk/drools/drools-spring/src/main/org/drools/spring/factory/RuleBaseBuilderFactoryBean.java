package org.drools.spring.factory;

import org.drools.DroolsException;
import org.drools.RuleBaseBuilder;
import org.drools.reteoo.FactHandleFactory;
import org.drools.spi.ConflictResolver;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class RuleBaseBuilderFactoryBean implements FactoryBean, InitializingBean {

    private ConflictResolver conflictResolver;
    private FactHandleFactory factHandleFactory;
    private RuleBaseBuilder ruleBaseBuilder;

    public void setConflictResolver(ConflictResolver conflictResolver) {
        this.conflictResolver = conflictResolver;
    }

    public void setFactHandleFactory(FactHandleFactory factHandleFactory) {
        this.factHandleFactory = factHandleFactory;
    }

    public void afterPropertiesSet() throws Exception {
    }

    private RuleBaseBuilder createObject() throws DroolsException {
        RuleBaseBuilder builder = new RuleBaseBuilder();
        if (conflictResolver != null) {
            builder.setConflictResolver(conflictResolver);
        }
        if (factHandleFactory != null) {
            builder.setFactHandleFactory(factHandleFactory);
        }
        return builder;
    }

    public Class getObjectType() {
        return RuleBaseBuilder.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public Object getObject() throws Exception {
        if (ruleBaseBuilder == null) {
            ruleBaseBuilder = createObject();
        }
        return ruleBaseBuilder;
    }
}
