package org.drools.semantics.annotation.spring;

import org.drools.rule.Rule;

import junit.framework.TestCase;

public class SpringAnnotationRuleFactoryTest extends TestCase
{
    private SpringAnnotationRuleFactory factory = new SpringAnnotationRuleFactory( );

    public void testGetObjectType( ) throws Exception
    {
        assertSame( Rule.class, factory.getObjectType( ) );
    }

    public void testIsSingleton( ) throws Exception
    {
        assertFalse( factory.isSingleton( ) );
    }
}
