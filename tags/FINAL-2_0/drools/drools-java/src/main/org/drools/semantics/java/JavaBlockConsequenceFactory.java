package org.drools.semantics.java;

import org.drools.rule.Rule;
import org.drools.smf.Configuration;
import org.drools.smf.ConsequenceFactory;
import org.drools.smf.FactoryException;
import org.drools.spi.Consequence;
import org.drools.spi.RuleBaseContext;

public class JavaBlockConsequenceFactory
    implements
    ConsequenceFactory
{
    public Consequence newConsequence( Rule rule,
                                       RuleBaseContext context,
                                       Configuration config) throws FactoryException
    {
        try
        {
            Integer id = (Integer) context.get( "java-consequence-id" );
            if (id == null)
            {
                id = new Integer( 0 );
            }
            context.put("java-consequence-id", new Integer(id.intValue() + 1));
            
            return new JavaBlockConsequence( rule,
                                             id.intValue(),
                                             config.getText( ) );

        }
        catch ( Exception e )
        {
            throw new FactoryException( e );
        }
    }
}