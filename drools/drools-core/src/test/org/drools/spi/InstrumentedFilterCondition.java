
package org.drools.spi;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

public class InstrumentedFilterCondition implements FilterCondition
{
    private Set     decls;
    private boolean isAllowed;

    public InstrumentedFilterCondition()
    {
        this.decls = new HashSet();
    }

    public Declaration[] getRequiredTupleMembers()
    {
        Declaration[] declArray = new Declaration[ this.decls.size() ];

        Iterator declIter = this.decls.iterator();

        int i = 0;

        while ( declIter.hasNext() )
        {
            declArray[i++] = (Declaration) declIter.next();
        }

        return declArray;
    }

    public void addDeclaration(Declaration decl)
    {
        this.decls.add( decl );
    }

    public void isAllowed(boolean isAllowed)
    {
        this.isAllowed = isAllowed;
    }

    public boolean isAllowed(Tuple tuple)
    {
        return this.isAllowed;
    }
}
