package org.drools.spring.factory;

/*
 * Copyright 2005 (C) The Werken Company. All Rights Reserved.
 *
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. The name "drools" must not be used to endorse or promote products derived
 * from this Software without prior written permission of The Werken Company.
 * For written permission, please contact bob@werken.com.
 *
 * 4. Products derived from this Software may not be called "drools" nor may
 * "drools" appear in their names without prior written permission of The Werken
 * Company. "drools" is a registered trademark of The Werken Company.
 *
 * 5. Due credit should be given to The Werken Company.
 * (http://drools.werken.com/).
 *
 * THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE WERKEN COMPANY OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */



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

