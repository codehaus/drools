package org.drools.semantics.annotation.model;

import org.drools.rule.Declaration;
import org.drools.spi.Tuple;

class TupleParameterValue implements ParameterValue
{
    private final Declaration declaration;

    public TupleParameterValue( final Declaration declaration )
    {
        if (declaration == null)
        {
            throw new IllegalArgumentException( "Null 'declaration' argument" );
        }
        this.declaration = declaration;
    }

    /**
     * @return The fact associated with the paramter declaration. The returned
     *         value may be null.
     */
    public Object getValue( Tuple tuple )
    {
        return tuple.get( declaration );
    }

    public Declaration getDeclaration( )
    {
        return declaration;
    }
}