package org.drools.reteoo;

import org.drools.DroolsTestCase;
import org.drools.WorkingMemory;

public class RuleBaseImplTest extends DroolsTestCase
{
    public void testWeakReference() throws Exception
    {
        RuleBaseImpl ruleBase = new RuleBaseImpl( new Rete() );
        
        WorkingMemory wm1 = ruleBase.newWorkingMemory();
        WorkingMemory wm2 = ruleBase.newWorkingMemory();
        WorkingMemory wm3 = ruleBase.newWorkingMemory();
        WorkingMemory wm4 = ruleBase.newWorkingMemory();
        
        /* Make sure the RuleBase is referencing all 4 Working Memories */
        assertLength( 4,
                      ruleBase.getWorkingMemories() );        
        assertTrue( ruleBase.getWorkingMemories().contains( wm1 ) );
        assertTrue( ruleBase.getWorkingMemories().contains( wm2 ) );
        assertTrue( ruleBase.getWorkingMemories().contains( wm3 ) );
        assertTrue( ruleBase.getWorkingMemories().contains( wm4 ) );        
        
        /* nulling these two so the keys should get garbage collected */
        wm2 = null;        
        wm4 = null;
        
        /* Run GC */
        System.gc();        
        Thread.sleep( 200 ); //Shouldn't need to sleep, but put it in anyway        
        
        /* Check we now only have two keys */
        assertLength( 2,
                      ruleBase.getWorkingMemories() );
        
        /* Make sure the correct keys were removed */
        assertTrue( ruleBase.getWorkingMemories().contains( wm1 ) );
        assertFalse( ruleBase.getWorkingMemories().contains( wm2 ) );
        assertTrue( ruleBase.getWorkingMemories().contains( wm3 ) );
        assertFalse( ruleBase.getWorkingMemories().contains( wm4 ) );
        
        /* Now lets test the dispose method on the WorkingMemory itself.
         * dispose doesn't need GC */
        wm3.dispose();                
                
        /* Check onl wm1 is left */
        assertLength( 1,
                      ruleBase.getWorkingMemories() );        
        assertTrue( ruleBase.getWorkingMemories().contains( wm1 ) );        
    }

}
