
package org.drools.semantic.java;

import org.drools.spi.FactExtractor;
import org.drools.spi.Declaration;
import org.drools.spi.Tuple;
import org.drools.spi.FactExtractionException;

/** Bare-bones compile-time Java object fact extractor.
 *
 *  <p>
 *  This is an abstract class with a single method ({@link #extractFactFromObject})
 *  that needs defining for subclasses.
 *  </p>
 *
 *  <p>
 *  This class is only suitable when the fact extraction requires
 *  a single object available in the <code>Tuple</code>.
 *  </p>
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public abstract class JavaFactExtractor implements FactExtractor
{
    private Declaration objectDecl;

    protected JavaFactExtractor(Declaration objectDecl)
    {
        this.objectDecl = objectDecl;
    }

    public Declaration getObjectDeclaration()
    {
        return this.objectDecl;
    }

    public Declaration[] getRequiredTupleMembers()
    {
        return new Declaration[]
            {
                getObjectDeclaration()
            };
    }

    public Object extractFact(Tuple tuple) throws FactExtractionException
    {
        Declaration decl = getObjectDeclaration();

        Object obj = tuple.get( decl );

        if ( obj == null )
        {
            return null;
        }

        return extractFactFromObject( obj );
    }

    /** Extract the fact from the given <code>Object</code>.
     *
     *  @param object The <code>Object</code> to extract from,
     *         guaranteed to not be <code>null</code>.
     *
     *  @return The extracted value.
     *
     *  @throws FactExtractionException if an error occurs during extraction.
     */
    abstract public Object extractFactFromObject(Object object) throws FactExtractionException;
}
