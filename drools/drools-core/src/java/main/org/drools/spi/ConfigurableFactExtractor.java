package org.drools.spi;

import org.drools.rule.Declaration;

public interface ConfigurableFactExtractor extends FactExtractor
{
    void configure(String text,
                   Declaration[] decls) throws ConfigurationException;
}
