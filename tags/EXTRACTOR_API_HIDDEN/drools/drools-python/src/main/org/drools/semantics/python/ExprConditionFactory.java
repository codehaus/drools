package org.drools.semantics.python;

import org.drools.rule.Rule;
import org.drools.smf.ConditionFactory;
import org.drools.smf.Configuration;
import org.drools.smf.FactoryException;
import org.drools.spi.Condition;

public class ExprConditionFactory implements ConditionFactory
{
    private static final ExprConditionFactory INSTANCE = new ExprConditionFactory( );

    public static ExprConditionFactory getInstance()
    {
        return INSTANCE;
    }

    public Condition newCondition(Configuration config, Rule rule) throws FactoryException
    {
        try
        {
            return new ExprCondition( config.getText( ), rule.getImports(), rule.getAllDeclarations());
        }
        catch ( Exception e )
        {
            throw new FactoryException( e );
        }
    }
}