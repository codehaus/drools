package org.drools.smf;

import org.drools.rule.Declaration;
import org.drools.spi.Consequence;

public interface ConfigurableConsequence extends Consequence
{
    void configure(String text,
                   Declaration[] decls) throws ConfigurationException;
}
