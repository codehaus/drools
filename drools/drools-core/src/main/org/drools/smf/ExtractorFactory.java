package org.drools.smf;

import org.drools.rule.Declaration;
import org.drools.spi.Extractor;

public interface ExtractorFactory
{
    Extractor newExtractor(Declaration[] availDecls,
                           Configuration config)
        throws FactoryException;
}
