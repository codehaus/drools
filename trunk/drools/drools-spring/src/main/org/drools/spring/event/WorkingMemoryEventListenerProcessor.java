package org.drools.spring.event;

import java.util.Iterator;
import java.util.Map;

import org.drools.WorkingMemory;
import org.drools.event.WorkingMemoryEventListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanFactoryPostProcessor;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;

public class WorkingMemoryEventListenerProcessor implements BeanFactoryPostProcessor {

    private WorkingMemory workingMemory;

    public void setWorkingMemory(WorkingMemory workingMemory) {
        this.workingMemory = workingMemory;
    }

    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        if (workingMemory == null) {
            workingMemory = (WorkingMemory) beanFactory.getBean("workingMemory");
        }
        Map listeners = beanFactory.getBeansOfType(WorkingMemoryEventListener.class);
        for (Iterator iter = listeners.values().iterator(); iter.hasNext();) {
            WorkingMemoryEventListener listener = (WorkingMemoryEventListener) iter.next();
            workingMemory.addEventListener(listener);
        }
    }
}
