package org.drools.spring.factory;

import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class WorkingMemoryFactoryBean implements FactoryBean, InitializingBean {

    private RuleBase ruleBase;
    // TODO Async exception hanlder
    private Map applicationData ;
    private WorkingMemory workingMemory;

    public void setRuleBase(RuleBase ruleBase) {
        this.ruleBase = ruleBase;
    }

    public void setApplicationData(Map data) {
        this.applicationData = data;
    }

    public void afterPropertiesSet() throws Exception {
        validateProperties();
        createObject();
    }

    private void validateProperties() {
        if (ruleBase == null) {
            throw new IllegalArgumentException("ruleBase property not specified");
        }
    }

    private void createObject() {
        workingMemory = ruleBase.newWorkingMemory();
        if (applicationData != null) {
            for (Iterator iter = applicationData.entrySet().iterator(); iter.hasNext();) {
                Map.Entry entry = (Entry) iter.next();
                workingMemory.setApplicationData((String)entry.getKey(), entry.getValue());
            }
        }
    }

    public Class getObjectType() {
        return WorkingMemory.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public Object getObject() throws Exception {
        return workingMemory;
    }
}
