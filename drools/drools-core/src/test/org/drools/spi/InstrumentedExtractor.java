
package org.drools.spi;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.drools.rule.Declaration;

public class InstrumentedExtractor implements Extractor
{
    private Object value;
    private Set    decls;

    public InstrumentedExtractor()
    {
        this.value = null;
        this.decls = new HashSet(); 
    }

    public InstrumentedExtractor(Object value)
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
