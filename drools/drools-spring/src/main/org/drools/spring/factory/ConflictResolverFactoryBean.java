package org.drools.spring.factory;

import java.util.Iterator;
import java.util.List;

import org.drools.conflict.CompositeConflictResolver;
import org.drools.spi.ConflictResolver;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class ConflictResolverFactoryBean implements FactoryBean, InitializingBean {

    private List resolverStrategies;
    private CompositeConflictResolver resolver;

    public void setResolverStrategies(List resolverStrategies) {
        this.resolverStrategies = resolverStrategies;
    }

    public void afterPropertiesSet() throws Exception {
        validateProperties();
        createObject();
    }

    private void validateProperties() {
        if (resolverStrategies == null || resolverStrategies.isEmpty()) {
            throw new IllegalArgumentException("resolverStrategies property not specified or is empty");
        }
        for (Iterator iter = resolverStrategies.iterator(); iter.hasNext();) {
            Object object = iter.next();
            if (!(object instanceof ConflictResolver)) {
                throw new IllegalArgumentException("resolverStrategies item not instanceof ConflictResolver: " + object.getClass());
            }
        }
    }

    private void createObject() {
        ConflictResolver[] resolvers = (ConflictResolver[]) resolverStrategies.toArray(new ConflictResolver[resolverStrategies.size()]);
        resolver = new CompositeConflictResolver(resolvers);
    }

    public Class getObjectType() {
        return ConflictResolver.class;
    }

    public boolean isSingleton() {
        return true;
    }

    public Object getObject() throws Exception {
        return resolver;
    }

}
