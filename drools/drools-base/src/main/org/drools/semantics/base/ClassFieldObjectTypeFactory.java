package org.drools.semantics.base;

import java.lang.reflect.Method;

import org.drools.smf.Configuration;
import org.drools.smf.FactoryException;
import org.drools.smf.ObjectTypeFactory;
import org.drools.spi.ObjectType;

public class ClassFieldObjectTypeFactory implements ObjectTypeFactory
{
    private static final ClassFieldObjectTypeFactory INSTANCE = new ClassFieldObjectTypeFactory( );

    public static ClassFieldObjectTypeFactory getInstance()
    {
        return INSTANCE;
    }

    public ObjectType newObjectType(Configuration config) throws FactoryException
    {
        String className = config.getText( );
        String fieldName = config.getAttribute("field");
        String fieldValue = config.getAttribute("value");

        if ( className == null || className.trim( ).equals( "" ))
        {
            throw new FactoryException( "no class name specified" );
        }

        if ( fieldName == null || fieldName.trim( ).equals( "" ))
        {
            throw new FactoryException( "no field name specified" );
        }

        if ( fieldValue == null || fieldValue.trim( ).equals( "" ))
        {
            throw new FactoryException( "no field value specified" );
        }
        

        ClassLoader cl = Thread.currentThread( ).getContextClassLoader( );

        try
        {
            Class objectTypeClass = cl.loadClass( className.trim( ) );
            
            //make sure field getter exists
            String fieldGetter = "get" + fieldName.toUpperCase().charAt(0) 
            + fieldName.substring(1);
            Method getterMethod = objectTypeClass.getMethod(fieldGetter, null);       

            return new ClassFieldObjectType( objectTypeClass, fieldName, fieldValue);
        }
        catch ( ClassNotFoundException e )
        {
            throw new FactoryException( e );
        }
        catch ( SecurityException e )
        {
            throw new FactoryException( "Field " + fieldName + " does not accessible for Class " + className);
        }
        catch ( NoSuchMethodException e )
        {
            throw new FactoryException( "Field " + fieldName + " does not exist for Class " + className);
        }
    }
}