package org.drools.spring.factory;

import java.lang.reflect.InvocationTargetException;

import org.drools.WorkingMemory;
import org.springframework.beans.factory.config.MethodInvokingFactoryBean;

/**
 * <pre>
 * &lt;bean parent="assertFact"&gt;
 *     &lt;property name="fact"&gt;
 *         &lt;bean class="org.blackboxtrader.liquidityarbitrage.fact.RequiredQuoteQuantityFact">
 *             &lt;property name="value" value="${requiredQuoteQuantity}" />
 *         &lt;/bean>
 *     &lt;/property>
 * &lt;/bean&gt;
 * </pre>
 */
public class FactFactoryBean extends MethodInvokingFactoryBean {

    private WorkingMemory workingMemory;
    private Object fact;

    public void setWorkingMemory(WorkingMemory workingMemory) {
        this.workingMemory = workingMemory;
    }

    public void setFact(Object fact) {
        this.fact = fact;
    }

    public void afterPropertiesSet() throws ClassNotFoundException, NoSuchMethodException, InvocationTargetException, IllegalAccessException {
        if (workingMemory == null) {
            throw new IllegalArgumentException("WorkingMemory not set");
        }
        if (fact == null) {
            throw new IllegalArgumentException("Fact not set");
        }
        setTargetObject(workingMemory);
        setTargetMethod("assertObject");
        setArguments(new Object[]{fact});
        super.afterPropertiesSet();
    }
}
