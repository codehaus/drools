
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
        WorkingMemory memory = this.ruleBase.createWorkingMemory();

        assertNotNull( memory );

        assertSame( this.ruleBase,
                    memory.getRuleBase() );
    }

    public void testCreateTransactionalWorkingMemory()
    {
        TransactionalWorkingMemory memory = this.ruleBase.createTransactionalWorkingMemory();

        assertNotNull( memory );

        assertSame( this.ruleBase,
                    memory.getRuleBase() );
    }
}
