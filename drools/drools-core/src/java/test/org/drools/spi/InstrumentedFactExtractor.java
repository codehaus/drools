
package org.drools.spi;

import org.drools.rule.Declaration;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

public class InstrumentedFactExtractor implements FactExtractor
{
    private Object value;
    private Set    decls;

    public InstrumentedFactExtractor()
    {
        this.value = null;
        this.decls = new HashSet(); 
    }

    public InstrumentedFactExtractor(Object value)
    {
        this.value = value;
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

    public Set getDeclarations()
    {
        return this.decls;
    }

    public Object extractFact(Tuple tuple)
    {
        return this.value;
    }
}
