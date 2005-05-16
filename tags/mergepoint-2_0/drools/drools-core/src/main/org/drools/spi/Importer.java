package org.drools.spi;

import java.io.Serializable;
import java.util.Set;



/**
 * <p>
 * Importer holds a Set of ImportEntries and facilitates the
 * <code> <import/> </code> tag within a <code> <rule-set/> </code> by allowing
 * Classes to be loaded using a specified ClassLoader
 * 
 * @author <a href="mailto:mproctor@codehaus.org"> mark proctor </a>
 */
public interface Importer extends Serializable
{
    /**
     * <p>
     * Adds an ImportEntry to a Set
     * 
     * @param importEntry -
     *            the importEntry
     */
    public abstract void addImport(ImportEntry importEntry);

    /**
     * Imports a Class using the given ClassLoader
     * 
     * @param cl -
     *            the ClassLoader to use
     * @param className -
     *            the name of the Class to import
     * @return - the loaded class
     * @throws ClassNotFoundException -
     *             Thrown if a Class is not found
     * @throws Error
     *             Thrown is a class is ambiguously imported.
     */
    public abstract Class importClass(ClassLoader cl,
                                      String className) throws ClassNotFoundException, Error;    
    /**
     * @return - The Set of ImportEntries
     */
    public Set getImportEntries();
    
    /**
     * @return - the Set of imports, in text form.
     */
    public Set getImports(); 
    
    /**
     * @return - true if no ImportEntries have been added
     */
    public boolean isEmpty();

}