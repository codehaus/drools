package org.drools.spi;

import org.drools.rule.Declaration;

public interface ConfigurableCondition extends Condition
{
    void configure(String text,
                   Declaration[] decls) throws ConfigurationException;
}
