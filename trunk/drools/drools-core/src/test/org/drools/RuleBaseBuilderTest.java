package org.drools;

public class RuleBaseBuilderTest
    extends DroolsTestCase
{
    public void testBuild_Empty()
        throws Exception
    {
        RuleBaseBuilder builder = new RuleBaseBuilder();

        RuleBase ruleBase = builder.build();

        assertNotNull( ruleBase );
    }
}
