package org.drools.spi;

public interface ConfigurableObjectType extends ObjectType
{
    void configure(String text) throws ConfigurationException;
}
