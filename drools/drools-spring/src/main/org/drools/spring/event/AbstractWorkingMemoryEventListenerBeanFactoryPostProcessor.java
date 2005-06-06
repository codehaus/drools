package org.drools.spring.event;

import org.drools.WorkingMemory;
import org.drools.event.WorkingMemoryEventListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public abstract class AbstractWorkingMemoryEventListenerBeanFactoryPostProcessor implements BeanFactoryPostProcessor {

    protected abstract WorkingMemory getWorkingMemory();

    public void postProcessBeanFactory(ConfigurableListableBeanFactory factory) throws BeansException {
        String[] names = factory.getBeanNamesForType(WorkingMemoryEventListener.class);
        for (int i = 0; i < names.length; i++) {
            WorkingMemoryEventListener listener = (WorkingMemoryEventListener) factory.getBean(names[i]);
            getWorkingMemory().addEventListener(listener);
        }
    }
}
