
package org.drools.spi;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.drools.rule.Declaration;

public class InstrumentedCondition implements Condition
{
    private Set     decls;
    private boolean isAllowed;

    public InstrumentedCondition()
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
