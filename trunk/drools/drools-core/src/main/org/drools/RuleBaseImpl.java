package org.drools;

import org.drools.reteoo.Builder;
import org.drools.reteoo.Rete;
import org.drools.reteoo.WorkingMemoryImpl;
import org.drools.rule.Rule;
import org.drools.rule.RuleSet;

import java.util.Iterator;

public class RuleBaseImpl
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
    RuleBaseImpl(Rete rete)
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

    /** Retrieve the Rete-OO network for this <code>RuleBase</code>.
     *
     *  @return The RETE-OO network. 
     */
    Rete getRete()
    {
        return this.rete;
    }

    public void assertObject(FactHandle handle,
                             Object object,
                             WorkingMemoryImpl workingMemory)
        throws FactException
    {
        getRete().assertObject( handle,
                                object,
                                workingMemory );
    }

    public void retractObject(FactHandle handle,
                              Object object,
                              WorkingMemoryImpl workingMemory)
        throws FactException
    {
        getRete().retractObject( handle,
                                 object,
                                 workingMemory );
    }

    public void modifyObject(FactHandle handle,
                             Object object,
                             WorkingMemoryImpl workingMemory)
        throws FactException
    {
        getRete().modifyObject( handle,
                                object,
                                workingMemory );
    }
}
