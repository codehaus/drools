package org.drools.semantics.java;

import org.drools.rule.Rule;
import org.drools.smf.Configuration;
import org.drools.smf.ExtractorFactory;
import org.drools.smf.FactoryException;
import org.drools.spi.Extractor;

public class ExprExtractorFactory implements ExtractorFactory
{
    private static final ExprExtractorFactory INSTANCE = new ExprExtractorFactory( );

    public ExprExtractorFactory getInstance()
    {
        return INSTANCE;
    }

    public Extractor newExtractor(Configuration config, Rule rule) throws FactoryException
    {
        try
        {
            return new ExprExtractor( config.getText( ), rule.getAllDeclarations() );
        }
        catch ( Exception e )
        {
            throw new FactoryException( e );
        }
    }
}