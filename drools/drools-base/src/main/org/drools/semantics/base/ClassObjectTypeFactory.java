package org.drools.semantics.base;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.drools.rule.Imports;
import org.drools.smf.Configuration;
import org.drools.smf.FactoryException;
import org.drools.smf.ObjectTypeFactory;
import org.drools.spi.ImportEntry;
import org.drools.spi.ObjectType;

public class ClassObjectTypeFactory implements ObjectTypeFactory
{
    private static final ClassObjectTypeFactory INSTANCE = new ClassObjectTypeFactory( );

    public static ClassObjectTypeFactory getInstance()
    {
        return INSTANCE;
    }

    public ObjectType newObjectType(Configuration config, Set imports) throws FactoryException
    {        
        String className = config.getText( ).trim();

        if ( className == null || className.trim( ).equals( "" ) )
        {
            throw new FactoryException( "no class name specified" );
        }
       
        //get imports
        Set importSet = new HashSet(); 
        if (imports != null)
        {
                              
            Iterator it = imports.iterator();
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
        
        return new ClassObjectType( clazz );
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