package org.drools.smf;

import org.drools.rule.Declaration;
import org.drools.spi.Condition;

public interface ConfigurableCondition extends Condition
{
    void configure(String text,
                   Declaration[] decls) throws ConfigurationException;
}
