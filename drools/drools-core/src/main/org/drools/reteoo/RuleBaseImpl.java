package org.drools.reteoo;

import org.drools.WorkingMemory;
import org.drools.FactHandle;
import org.drools.RuleBase;
import org.drools.FactException;
import org.drools.spi.ConflictResolutionStrategy;
import org.drools.rule.Rule;
import org.drools.rule.RuleSet;

import java.util.Iterator;

class RuleBaseImpl
    implements RuleBase
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** The root Rete-OO for this <code>RuleBase</code>. */
    private Rete rete;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    public RuleBaseImpl(Rete rete)
    {
        this.rete = rete;
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** @see RuleBase
     */
    public WorkingMemory newWorkingMemory()
    {
        return new WorkingMemoryImpl( this );
    }

    public WorkingMemory newWorkingMemory(ConflictResolutionStrategy strategy)
    {
        return new WorkingMemoryImpl( this,
                                      strategy );
    }

    /** Retrieve the Rete-OO network for this <code>RuleBase</code>.
     *
     *  @return The RETE-OO network. 
     */
    Rete getRete()
    {
        return this.rete;
    }

    void assertObject(FactHandle handle,
                      Object object,
                      WorkingMemoryImpl workingMemory)
        throws FactException
    {
        getRete().assertObject( handle,
                                object,
                                workingMemory );
    }
    
    void retractObject(FactHandle handle,
                       Object object,
                       WorkingMemoryImpl workingMemory)
        throws FactException
    {
        getRete().retractObject( handle,
                                 object,
                                 workingMemory );
    }

    void modifyObject(FactHandle handle,
                      Object object,
                      WorkingMemoryImpl workingMemory)
        throws FactException
    {
        getRete().modifyObject( handle,
                                object,
                                workingMemory );
    }
}
