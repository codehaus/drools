package org.drools.io;

import org.drools.DroolsTestCase;
import org.drools.RuleBase;

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
