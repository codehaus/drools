package org.drools.semantics.annotation.model;

import org.drools.spi.Tuple;

public class ApplicationDataArgument implements Argument
{
    private final String name;
    private final Class< ? > clazz;

    public ApplicationDataArgument(String name, Class< ? > clazz)
    {
        if (name == null)
        {
            throw new IllegalArgumentException( "Null 'name' argument" );
        }
        if (clazz == null)
        {
            throw new IllegalArgumentException( "Null 'clazz' argument" );
        }
        this.name = name;
        this.clazz = clazz;
    }

    public Object getValue( Tuple tuple )
    {
        Object value = tuple.getWorkingMemory( ).getApplicationData( name );
        if (!clazz.isAssignableFrom( value.getClass( ) ))
        {
            // TODO What is the apprioriate drools exception to throw here?
            throw new IllegalStateException( "Application data class different than declaration"
                    + ": app-data-name = " + name + ", expected class = " + clazz
                    + ", actual class = " + value.getClass( ) );
        }
        return value;
    }
}
