package org.drools.smf;

import org.drools.spi.ObjectType;

public interface ConfigurableObjectType extends ObjectType
{
    void configure(String text) throws ConfigurationException;
}
