package org.drools.smf;

import org.drools.rule.Declaration;
import org.drools.spi.Extractor;

public interface ConfigurableExtractor extends Extractor
{
    void configure(String text,
                   Declaration[] decls) throws ConfigurationException;
}
