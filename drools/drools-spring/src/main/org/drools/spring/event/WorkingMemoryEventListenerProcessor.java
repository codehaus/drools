package org.drools.spring.event;

import org.drools.WorkingMemory;
import org.drools.event.WorkingMemoryEventListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.config.BeanPostProcessor;

public class WorkingMemoryEventListenerProcessor implements InitializingBean, BeanPostProcessor {

    private WorkingMemory workingMemory;

    public void setWorkingMemory(WorkingMemory workingMemory) {
        this.workingMemory = workingMemory;
    }

    public void afterPropertiesSet() throws Exception {
        if (workingMemory == null) {
            throw new IllegalArgumentException("WorkingMemory not set");
        }
    }

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof WorkingMemoryEventListener) {
            workingMemory.addEventListener((WorkingMemoryEventListener)bean);
        }
        return bean;
    }
}
