package org.drools.semantics.base;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.drools.rule.Imports;
import org.drools.smf.Configuration;
import org.drools.smf.FactoryException;
import org.drools.smf.ObjectTypeFactory;
import org.drools.spi.ImportEntry;
import org.drools.spi.ObjectType;

public class ClassFieldObjectTypeFactory implements ObjectTypeFactory
{
    private static final ClassFieldObjectTypeFactory INSTANCE = new ClassFieldObjectTypeFactory( );

    public static ClassFieldObjectTypeFactory getInstance()
    {
        return INSTANCE;
    }

    public ObjectType newObjectType(Configuration config, Imports imports) throws FactoryException
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
        
        try
        {
            //get imports
            Set importSet = new HashSet(); 
            if ((imports != null)&&(imports.getImportEntries() != null))
            {
                                  
                Iterator it = imports.getImportEntries().iterator();
                ImportEntry importEntry;
                while (it.hasNext())
                {
                    importEntry = (ImportEntry) it.next();
                    importSet.add(importEntry.getImportEntry());
                }                
            }        
            ClassLoader cl = Thread.currentThread( ).getContextClassLoader( );
            
            Class clazz = null;
            /* first try loading className */
            try
            {
                clazz = cl.loadClass( className );
            }
            catch ( Exception e )
            {
                //swallow
            }    
            
            /* Now try the className with each of the given imports */
            if (clazz == null)
            {
                Iterator it = importSet.iterator();
                String importEntry;
                Class objectTypeClass = null;
                while (it.hasNext() && (clazz == null))
                {
                    clazz = importClass(cl, (String) it.next(), className.trim( )) ;
                }  
            }
            /* We still can't find the class so throw an exception */
            if (clazz == null)
            {
                throw new FactoryException( "Unable to find class " + className);
            }
                        
            //make sure field getter exists
            String fieldGetter = "get" + fieldName.toUpperCase().charAt(0) 
            + fieldName.substring(1);
            Method getterMethod = clazz.getMethod(fieldGetter, null);       

            return new ClassFieldObjectType( clazz, fieldName, fieldValue);
        }
        catch ( SecurityException e )
        {
            throw new FactoryException( "Field " + fieldName + " is not accessible for Class " + className);
        }
        catch ( NoSuchMethodException e )
        {
            throw new FactoryException( "Field " + fieldName + " does not exist for Class " + className);
        }
    }
    
    private Class importClass(ClassLoader cl, String importText, String className)
    {
        String qualifiedClass = null;
        Class clazz = null;
        if (importText.startsWith("from ")) 
        {            
            importText = converPythonImport(importText);
        }               
        //not python
        if (importText.endsWith("*"))
        {
            qualifiedClass = importText.substring(0, importText.indexOf('*')) + className;
        }
        else if (importText.endsWith(className))
        {
            qualifiedClass = importText;
        }                        
        
        if (qualifiedClass != null)
        {            
            try
            {
                clazz = cl.loadClass( qualifiedClass );
            }
            catch ( Exception e )
            {
                //swallow
            }                                 
        }
        return clazz;
    }
    
    private String converPythonImport(String packageText)
    {
        int fromIndex = packageText.indexOf("from ");
        int importIndex = packageText.indexOf("import ");
        return packageText.substring(fromIndex + 5, importIndex).trim() + "." +
               packageText.substring(importIndex + 7).trim();
    }    
}