package org.drools.smf;

import org.drools.rule.Rule;
import org.drools.spi.Extractor;

public interface ExtractorFactory
{
    Extractor newExtractor(Configuration config, Rule rule) throws FactoryException;    
}