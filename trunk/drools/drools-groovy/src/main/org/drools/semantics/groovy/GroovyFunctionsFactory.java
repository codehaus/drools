package org.drools.semantics.groovy;

import org.drools.rule.RuleSet;
import org.drools.smf.Configuration;
import org.drools.smf.FunctionsFactory;
import org.drools.smf.FactoryException;
import org.drools.spi.Functions;
import org.drools.spi.RuleBaseContext;

public class GroovyFunctionsFactory implements FunctionsFactory
{

    private static final FunctionsFactory INSTANCE = new GroovyFunctionsFactory( );

    public static FunctionsFactory getInstance()
    {
        return INSTANCE;
    }

    public Functions newFunctions(RuleSet ruleSet,
                                  RuleBaseContext context,
                                  Configuration config) throws FactoryException
    {
        return new GroovyFunctions( config.getAttribute("name"), config.getText( ) );
    }
}