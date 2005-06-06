package org.drools.spring.event;

import org.drools.WorkingMemory;
import org.drools.event.WorkingMemoryEventListener;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.ListableBeanFactory;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;

public abstract class AbstractWorkingMemoryContextRefreshedRegistrar implements ApplicationListener {

    protected abstract WorkingMemory getWorkingMemory();

    public void onApplicationEvent(ApplicationEvent event) {
        if (ContextRefreshedEvent.class.isAssignableFrom(event.getClass())) {
            ListableBeanFactory factory = (ListableBeanFactory) event.getSource();
            registerListeners(factory);
        }
    }

    private void registerListeners(ListableBeanFactory factory) throws BeansException {
        String[] names = factory.getBeanNamesForType(WorkingMemoryEventListener.class);
        for (int i = 0; i < names.length; i++) {
            WorkingMemoryEventListener listener = (WorkingMemoryEventListener) factory.getBean(names[i]);
            getWorkingMemory().addEventListener(listener);
        }
    }
}
