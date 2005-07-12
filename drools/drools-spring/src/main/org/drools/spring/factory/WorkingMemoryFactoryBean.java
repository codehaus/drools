package org.drools.spring.factory;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.event.WorkingMemoryEventListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.ListableBeanFactory;

public class WorkingMemoryFactoryBean implements FactoryBean, InitializingBean, BeanFactoryAware {

    private static Log log = LogFactory.getLog(WorkingMemoryFactoryBean.class);
    
    private RuleBase ruleBase;
    private boolean autoDetectListeners;
    private Set listeners;
    
    private BeanFactory beanFactory;
    // TODO Async exception handler
    private Map applicationData ;
    private WorkingMemory workingMemory;

    public void setRuleBase(RuleBase ruleBase) {
        this.ruleBase = ruleBase;
    }
    
    public void setAutoDetectListeners(boolean autoRegisterListeners) {
        this.autoDetectListeners = autoRegisterListeners;
    }
    
    public void setListeners(Set listeners) {
        this.listeners = listeners;
    }
    
    public void setApplicationData(Map data) {
        this.applicationData = data;
    } 

    public void afterPropertiesSet() throws Exception {
        if (ruleBase == null) {
            throw new IllegalArgumentException("ruleBase not set");
        }
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
    
    private WorkingMemory createObject() {
        WorkingMemory workingMemory = ruleBase.newWorkingMemory();
        if (applicationData != null) {
            for (Iterator iter = applicationData.entrySet().iterator(); iter.hasNext();) {
                Map.Entry entry = (Entry) iter.next();
                workingMemory.setApplicationData((String)entry.getKey(), entry.getValue());
            }
        }
        if (listeners == null) {
            listeners = new HashSet();
        }
        if (autoDetectListeners) {
            autoDetectListeners(workingMemory);
        }
        registerListeners(workingMemory);
        return workingMemory;
    }

    private void autoDetectListeners(WorkingMemory workingMemory) throws BeansException {
        if (!(beanFactory instanceof ListableBeanFactory)) {
            log.warn("Cannot register WorkingMemoryListeners; beanFactory is not instanceof ListableBeanFactory: beanFactory.class=" + beanFactory.getClass());
            return;
        }
        ListableBeanFactory factory = (ListableBeanFactory) beanFactory;
        Map listenerBeans = factory.getBeansOfType(WorkingMemoryEventListener.class);
        for (Iterator iter = listenerBeans.values().iterator(); iter.hasNext();) {
            WorkingMemoryEventListener listener = (WorkingMemoryEventListener) iter.next();
            listeners.add(listener);
        }
    }
    
    private void registerListeners(WorkingMemory workingMemory) {
        for (Iterator iter = listeners.iterator(); iter.hasNext();) {
            WorkingMemoryEventListener listener = (WorkingMemoryEventListener) iter.next();
            workingMemory.addEventListener(listener);
        }
    }

    public Class getObjectType() {
        return WorkingMemory.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public Object getObject() throws Exception {
        if (workingMemory == null) {
            workingMemory = createObject();
        }
        return workingMemory;
    }
}
