package org.drools.semantics.java;

import org.drools.rule.Declaration;
import org.drools.spi.Condition;
import org.drools.smf.ConditionFactory;
import org.drools.smf.Configuration;
import org.drools.smf.FactoryException;

public class ExprConditionFactory
    implements ConditionFactory
{
    private static final ExprConditionFactory INSTANCE = new ExprConditionFactory();

    public static ExprConditionFactory getInstance()
    {
        return INSTANCE;
    }

    public Condition newCondition(Configuration config,
                                  Declaration[] availDecls)
        throws FactoryException
    {
        try
        {
            return new ExprCondition( config.getText(),
                                      availDecls );
        }
        catch (Exception e)
        {
            throw new FactoryException( e );
        }
    }
}