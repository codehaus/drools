package org.drools.semantics.java;

import org.drools.rule.Rule;
import org.drools.smf.Configuration;
import org.drools.smf.ConsequenceFactory;
import org.drools.smf.FactoryException;
import org.drools.spi.Consequence;

public class JavaBlockConsequenceFactory
    implements
    ConsequenceFactory
{
    private static final JavaBlockConsequenceFactory INSTANCE = new JavaBlockConsequenceFactory( );

    public static JavaBlockConsequenceFactory getInstance()
    {
        return INSTANCE;
    }

    public Consequence newConsequence(Configuration config,
                                      Rule rule ) throws FactoryException
    {
        try
        {
            return new JavaBlockConsequence( config.getText( ),
                                             rule );
        }
        catch ( Exception e )
        {
            throw new FactoryException( e );
        }
    }
}