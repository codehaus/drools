package org.drools.semantics.python;

import org.drools.rule.RuleSet;
import org.drools.smf.Configuration;
import org.drools.smf.FunctionsFactory;
import org.drools.smf.FactoryException;
import org.drools.spi.Functions;
import org.drools.spi.RuleBaseContext;

public class PythonFunctionsFactory implements FunctionsFactory
{

    private static final FunctionsFactory INSTANCE = new PythonFunctionsFactory( );

    public static FunctionsFactory getInstance()
    {
        return INSTANCE;
    }

    public Functions newFunctions(RuleSet ruleSet,
                                  RuleBaseContext context,
                                  Configuration config) throws FactoryException
    {
        return new PythonFunctions( config.getAttribute("name"), config.getText( ) );
    }
}