package org.drools.spi;

import org.drools.rule.Declaration;

public interface ConfigurableConsequence extends Consequence
{
    void configure(String text,
                   Declaration[] decls) throws ConfigurationException;
}
