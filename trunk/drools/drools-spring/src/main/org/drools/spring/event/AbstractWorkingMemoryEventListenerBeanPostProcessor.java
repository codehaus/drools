package org.drools.spring.event;

import org.drools.WorkingMemory;
import org.drools.event.WorkingMemoryEventListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;

public abstract class AbstractWorkingMemoryEventListenerBeanPostProcessor implements BeanPostProcessor {

    protected abstract WorkingMemory getWorkingMemory();

    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }

    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (bean instanceof WorkingMemoryEventListener) {
            getWorkingMemory().addEventListener((WorkingMemoryEventListener)bean);
        }
        return bean;
    }
}
