package org.drools.semantics.python;

import org.drools.spi.Consequence;
import org.drools.smf.ConsequenceFactory;
import org.drools.smf.Configuration;
import org.drools.smf.FactoryException;

public class BlockConsequenceFactory
    implements ConsequenceFactory
{

    private static final BlockConsequenceFactory INSTANCE = new BlockConsequenceFactory();

    public static BlockConsequenceFactory getInstance()
    {
        return INSTANCE;
    }

    public Consequence newConsequence(Configuration config)
        throws FactoryException
    {
        return new BlockConsequence( config.getText() );
    }
}
