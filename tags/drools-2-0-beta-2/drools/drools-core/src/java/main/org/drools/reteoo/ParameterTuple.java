
package org.drools.reteoo;

import org.drools.spi.Declaration;

import java.util.Set;
import java.util.HashSet;

/** Implementation of {@link ReteTuple} with a single column,
 *  based upon a <i>root fact object parameter</i> of a rule.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class ParameterTuple extends ReteTuple
{
    /** The parameter <code>Declaration</code>. */
    private Declaration declaration;

    /** The object bound to the parameter. */
    private Object      object;

    /** Construct.
     *
     *  @param declaration The parameter <code>Declaration</code>.
     *  @param object The object bound to the <code>Declaration</code>.
     */
    public ParameterTuple(Declaration declaration,
                          Object object)
    {
        this.declaration = declaration;
        this.object      = object;

        // addRootFactObject( object );
    }

    /** Retrieve the object for the parameter.
     *
     *  @return The current value of the parameter.
     */
    public Object getParameterObject()
    {
        return this.object;
    }

    /** Retrieve the root fact object <code>Declaration</code>.
     *
     *  @return The <code>Declaration</code>.
     */
    public Declaration getParameterDeclaration()
    {
        return this.declaration;
    }

    public Object get(Declaration declaration)
    {
        if ( this.declaration.equals( declaration ) )
        {
            return getParameterObject();
        }

        return super.get( declaration );
    }

    public Set getDeclarations()
    {
        Set decls = new HashSet();

        decls.add( getParameterDeclaration() );

        decls.addAll( super.getDeclarations() );

        return decls;
    }
}
