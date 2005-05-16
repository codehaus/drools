package org.drools.semantics.annotation.spring;

import java.util.Set;

import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class SpringAnnotationRuleSetFactory implements FactoryBean, InitializingBean
{
    private String name;
    private Set<Rule> rules;

    public void setName( String name )
    {
        this.name = name;
    }

    public void setRules( Set<Rule> rules )
    {
        this.rules = rules;
    }

    public void afterPropertiesSet( ) throws Exception
    {
        if (name == null)
        {
            throw new IllegalArgumentException( "RuleSet property 'name' must be specified." );
        }
    }

    public Object getObject( ) throws Exception
    {
        RuleSet ruleSet = new RuleSet( name );
        for (Rule rule : rules)
        {
            ruleSet.addRule( rule );
        }
        return ruleSet;
    }

    public Class getObjectType( )
    {
        return RuleSet.class;
    }

    public boolean isSingleton( )
    {
        return false;
    }
}
