package org.drools.spring.factory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryAware;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class DeferedCreationWorkingMemoryFactoryBean implements FactoryBean, InitializingBean, BeanFactoryAware {

    private BeanFactory beanFactory;
    private String ruleBaseName;
    // TODO Async exception hanlder
    private Map applicationData ;
    private WorkingMemory workingMemoryProxy;

    private InvocationHandler invocationHandler = new InvocationHandler() {

        private WorkingMemory workingMemory;

        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (workingMemory == null) {
                workingMemory = createObject();
            }
            return method.invoke(workingMemory, args);
        }

        private WorkingMemory createObject() {
            RuleBase ruleBase = (RuleBase) beanFactory.getBean(ruleBaseName);
            WorkingMemory workingMemory = ruleBase.newWorkingMemory();
            if (applicationData != null) {
                for (Iterator iter = applicationData.entrySet().iterator(); iter.hasNext();) {
                    Map.Entry entry = (Entry) iter.next();
                    workingMemory.setApplicationData((String)entry.getKey(), entry.getValue());
                }
            }
            return workingMemory;
        }

    };

    public void setRuleBase(String ruleBaseName) {
        this.ruleBaseName = ruleBaseName;
    }

    public void setApplicationData(Map data) {
        this.applicationData = data;
    }

    public void afterPropertiesSet() throws Exception {
        if (ruleBaseName == null) {
            throw new IllegalArgumentException("RuleBase property not specified");
        }
    }

    public Class getObjectType() {
        return WorkingMemory.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public Object getObject() throws Exception {
        if (workingMemoryProxy == null) {
            workingMemoryProxy  = (WorkingMemory) Proxy.newProxyInstance(
                    this.getClass().getClassLoader(),
                    new Class[]{WorkingMemory.class},
                    invocationHandler);
        }
        return workingMemoryProxy;
    }

    public void setBeanFactory(BeanFactory beanFactory) throws BeansException {
        this.beanFactory = beanFactory;
    }
}
