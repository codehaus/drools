package org.drools.semantics.java;

import org.drools.rule.Declaration;
import org.drools.spi.Extractor;
import org.drools.smf.ExtractorFactory;
import org.drools.smf.Configuration;
import org.drools.smf.FactoryException;

public class ExprExtractorFactory
{
    private static final ExprExtractorFactory INSTANCE = new ExprExtractorFactory();

    public ExprExtractorFactory getInstance()
    {
        return INSTANCE;
    }

    public Extractor newExtractor(Declaration[] availDecls,
                                  Configuration config)
        throws FactoryException
    {
        try
        {
            return new ExprExtractor( config.getText(),
                                      availDecls );
        }
        catch (Exception e)
        {
            throw new FactoryException( e );
        }
    }
}
