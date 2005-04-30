package org.drools.spi;

import java.io.Serializable;
import java.util.Set;



public interface Importer extends Serializable
{

    public abstract Set getImports();

    public abstract void addImport(ImportEntry importEntry);

    public abstract Class importClass(ClassLoader cl,
                                      String className) throws ClassNotFoundException, Error;    
    
    public Set getImports(Class clazz); 
    
    public boolean isEmpty();

}