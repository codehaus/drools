
package org.drools.reteoo;

import org.drools.spi.Tuple;
import org.drools.spi.Declaration;

import java.util.Map;
import java.util.HashMap;
import java.util.Set;
import java.util.Iterator;

/** Base Rete-OO {@link Tuple} implementation.
 *
 *  @see Tuple
 *  
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class ReteTuple implements Tuple
{
    private Map tuple;

    /** Construct.
     */
    public ReteTuple()
    {
        this.tuple = new HashMap();
    }

    /** Construct.
     *
     *  @param sizeHint Hint as to total number of entries
     *         in this <code>Tuple</code>.
     */
    public ReteTuple(int sizeHint)
    {
        this.tuple = new HashMap( sizeHint );
    }

    public void put(Declaration declaration,
                    Object value)
    {
        this.tuple.put( declaration,
                        value );
    }

    public Object get(Declaration declaration)
    {
        return this.tuple.get( declaration );
    }

    public void putAll(Tuple tuple)
    {
        Set decls = tuple.getDeclarations();

        Iterator    declIter = decls.iterator();
        Declaration eachDecl = null;

        while ( declIter.hasNext() )
        {
            eachDecl = (Declaration) declIter.next();

            put( eachDecl,
                 tuple.get( eachDecl ) );
        }
    }

    public Set getDeclarations()
    {
        return tuple.keySet();
    }

    public String toString()
    {
        return "[ReteTuple: tuple=" + this.tuple + "]";
    }
}
