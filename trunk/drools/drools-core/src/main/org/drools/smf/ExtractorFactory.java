package org.drools.smf;

import org.drools.spi.Extractor;

public interface ExtractorFactory
{
    Extractor newExtractor(Configuration config)
        throws FactoryException;
}
