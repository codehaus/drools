package org.drools.semantics.annotation.smf;

import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.semantics.annotation.model.AnnonatedPojoRuleBuilder;
import org.drools.semantics.base.BaseRuleFactory;
import org.drools.smf.Configuration;
import org.drools.smf.FactoryException;
import org.drools.spi.RuleBaseContext;

public class SMFAnnotationRuleFactory extends BaseRuleFactory
{
    private AnnonatedPojoRuleBuilder builder = new AnnonatedPojoRuleBuilder( );

    public Rule newRule( RuleSet ruleSet, RuleBaseContext context, Configuration config )
            throws FactoryException
    {

        Rule rule = super.newRule( ruleSet, context, config );
        String className = config.getAttribute( "class" );
        try
        {
            Object pojo = Class.forName( className ).newInstance( );
            builder.buildRule( rule, pojo );
        }
        catch (Exception e)
        {
            throw new FactoryException( e );
        }
        return rule;
    }
}
