package org.drools.semantics.base;

import org.drools.smf.Configuration;
import org.drools.smf.FactoryException;
import org.drools.smf.ObjectTypeFactory;
import org.drools.spi.ObjectType;

public class ClassObjectTypeFactory implements ObjectTypeFactory
{
    private static final ClassObjectTypeFactory INSTANCE = new ClassObjectTypeFactory( );

    public static ClassObjectTypeFactory getInstance()
    {
        return INSTANCE;
    }

    public ObjectType newObjectType(Configuration config) throws FactoryException
    {
        String className = config.getText( );

        if ( className == null || className.trim( ).equals( "" ) )
        {
            throw new FactoryException( "no class name specified" );
        }

        ClassLoader cl = Thread.currentThread( ).getContextClassLoader( );

        try
        {
            Class objectTypeClass = cl.loadClass( className.trim( ) );

            return new ClassObjectType( objectTypeClass );
        }
        catch ( ClassNotFoundException e )
        {
            throw new FactoryException( e );
        }
    }
}