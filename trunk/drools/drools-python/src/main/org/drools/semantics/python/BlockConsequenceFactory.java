package org.drools.semantics.python;

import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.smf.Configuration;
import org.drools.smf.ConsequenceFactory;
import org.drools.smf.FactoryException;
import org.drools.spi.Consequence;

public class BlockConsequenceFactory implements ConsequenceFactory
{

    private static final BlockConsequenceFactory INSTANCE = new BlockConsequenceFactory( );

    public static BlockConsequenceFactory getInstance()
    {
        return INSTANCE;
    }

    public Consequence newConsequence(Configuration config,
                                      Rule rule) throws FactoryException
    {
        return new BlockConsequence( config.getText( ), rule.getImports() );
    }
}