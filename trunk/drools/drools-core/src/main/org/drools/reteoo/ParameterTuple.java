package org.drools.reteoo;

import org.drools.rule.Declaration;

import java.util.Set;
import java.util.HashSet;

/** Implementation of <code>ReteTuple</code> with a single column,
 *  based upon a <i>root fact object parameter</i> of a rule.
 *
 *  @see ReteTuple
 *  @see ParameterNode
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
class ParameterTuple
    extends ReteTuple
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** The parameter <code>Declaration</code>. */
    private Declaration declaration;

    /** The object bound to the parameter. */
    private Object      object;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     *
     *  @param declaration The parameter <code>Declaration</code>.
     *  @param object The object bound to the <code>Declaration</code>.
     */
    ParameterTuple(Declaration declaration,
                   Object object)
    {
        this.declaration = declaration;
        this.object      = object;

        // addRootFactObject( object );
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

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
    Declaration getParameterDeclaration()
    {
        return this.declaration;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     org.drools.reteoo.ReteTuple
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Retrieve the value of a particular declaration column.
     *
     *  @param declaration The declaration.
     *
     *  @return The value.
     */
    public Object get(Declaration declaration)
    {
        if ( this.declaration.equals( declaration ) )
        {
            return getParameterObject();
        }

        return super.get( declaration );
    }

    /** Retrieve all declarations participating in this tuple.
     *
     *  @return Set of all declarations.
     */
    public Set getDeclarations()
    {
        Set decls = new HashSet();

        decls.add( getParameterDeclaration() );

        decls.addAll( super.getDeclarations() );

        return decls;
    }
}
