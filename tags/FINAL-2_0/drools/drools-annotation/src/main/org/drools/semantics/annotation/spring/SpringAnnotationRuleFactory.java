package org.drools.semantics.annotation.spring;

import org.drools.rule.Rule;
import org.drools.semantics.annotation.model.AnnonatedPojoRuleBuilder;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

public class SpringAnnotationRuleFactory implements FactoryBean, InitializingBean
{
    private static AnnonatedPojoRuleBuilder builder = new AnnonatedPojoRuleBuilder( );

    private String name;
    private Integer salience;
    private Boolean noloop;
    private String documentation;
    private Object pojo;

    public void setName( String name )
    {
        this.name = name;
    }

    public void setSalience( Integer salience )
    {
        this.salience = salience;
    }

    public void setNoLoop( Boolean noloop )
    {
        this.noloop = noloop;
    }

    public void setDocumentation( String documentation )
    {
        this.documentation = documentation;
    }

    public void setPojo( Object pojo )
    {
        this.pojo = pojo;
    }

    public void afterPropertiesSet( ) throws Exception
    {
        if (name == null || name.trim( ).length( ) == 0)
        {
            throw new IllegalArgumentException( "Rule 'name' property not specified" );
        }
        if (pojo == null)
        {
            throw new IllegalArgumentException( "Rule 'pojo' property not specified" );
        }
    }

    public Object getObject( ) throws Exception
    {
        Rule rule = new Rule( name );
        if (salience != null)
        {
            rule.setSalience( salience );
        }
        if (noloop != null)
        {
            rule.setNoLoop( noloop );
        }
        if (documentation != null)
        {
            rule.setDocumentation( documentation );
        }
        builder.buildRule( rule, pojo );
        return rule;
    }

    public Class getObjectType( )
    {
        return Rule.class;
    }

    public boolean isSingleton( )
    {
        return false;
    }
}
