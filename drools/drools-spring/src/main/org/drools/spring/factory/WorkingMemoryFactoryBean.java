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
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ConfigurableApplicationContext;

public class WorkingMemoryFactoryBean implements FactoryBean, InitializingBean, 
                                                 ApplicationContextAware {

    private static Log log = LogFactory.getLog(WorkingMemoryFactoryBean.class);
    
    private RuleBase ruleBase;
    private boolean autoDetectListeners;
    private Set listeners;
    
    private ApplicationContext applicationContext;
    
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

    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.applicationContext = applicationContext;
    }

    public void afterPropertiesSet() throws Exception {
        if (ruleBase == null) {
            throw new IllegalArgumentException("ruleBase not set");
        }
        if (autoDetectListeners) {
            addListenerAutodetectBeanPostProcessor();
        }
    }

    private void addListenerAutodetectBeanPostProcessor() {
        if (!(applicationContext instanceof ConfigurableApplicationContext)) {
            log.warn("Cannot autodetect WorkingMemoryListeners; beanFactory is not instanceof ConfigurableApplicationContext: beanFactory.class=" + applicationContext.getClass());
        } else {
            ConfigurableListableBeanFactory beanFactory = ((ConfigurableApplicationContext)applicationContext).getBeanFactory();
            beanFactory.addBeanPostProcessor(new BeanPostProcessor() {
                public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
                    return bean;
                }
                public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
                    if (bean == WorkingMemoryFactoryBean.this) {
                        autoDetectListeners();
                    }
                    return bean;
                }
            });
        }
    }

    private void autoDetectListeners() throws BeansException {
        if (listeners == null) {
            listeners = new HashSet();
        }
        Map listenerBeans = applicationContext.getBeansOfType(WorkingMemoryEventListener.class, false, false);
        for (Iterator iter = listenerBeans.values().iterator(); iter.hasNext();) {
            WorkingMemoryEventListener listener = (WorkingMemoryEventListener) iter.next();
            listeners.add(listener);
        }
    }
    
    private WorkingMemory createObject() {
        final WorkingMemory workingMemory = ruleBase.newWorkingMemory();
        addApplicationData(workingMemory);
        addListeners(workingMemory);
        return workingMemory;
    }

    private void addApplicationData(final WorkingMemory workingMemory) {
        if (applicationData != null) {
            for (Iterator iter = applicationData.entrySet().iterator(); iter.hasNext();) {
                Map.Entry entry = (Entry) iter.next();
                workingMemory.setApplicationData((String)entry.getKey(), entry.getValue());
            }
        }
    }

    private void addListeners(WorkingMemory workingMemory) {
        if (listeners != null) {
            for (Iterator iter = listeners.iterator(); iter.hasNext();) {
                WorkingMemoryEventListener listener = (WorkingMemoryEventListener) iter.next();
                workingMemory.addEventListener(listener);
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
        if (workingMemory == null) {
            workingMemory = createObject();
        }
        return workingMemory;
    }

}
