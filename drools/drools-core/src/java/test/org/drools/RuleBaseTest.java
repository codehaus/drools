
package org.drools;

import junit.framework.TestCase;

public class RuleBaseTest extends TestCase
{
    private RuleBase ruleBase;

    public RuleBaseTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
        this.ruleBase = new RuleBase();
    }

    public void tearDown()
    {
        this.ruleBase = null;
    }

    public void testCreateWorkingMemory()
    {
        WorkingMemory memory = this.ruleBase.newWorkingMemory();

        assertNotNull( memory );

        assertSame( this.ruleBase,
                    memory.getRuleBase() );
    }
}
