package org.drools;

public interface RuleBase
{
    /** Create a new <code>WorkingMemory</code> session for
     *  this <code>RuleBase</code>.
     *
     *  @see WorkingMemory
     *
     *  @return A newly initialized <code>WorkingMemory</code>.
     */
    WorkingMemory newWorkingMemory();
}
