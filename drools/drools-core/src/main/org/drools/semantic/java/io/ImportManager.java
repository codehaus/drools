
package org.drools.semantic.java.io;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

class ImportManager
{
    private List packages;
    private Map  classes;

    public ImportManager()
    {
        reset();
    }

    public void reset()
    {
        this.packages = new ArrayList();
        this.classes  = new HashMap();

        addPackageImport( "java.lang." );
    }

    public void addImport(String spec)
    {
        if ( spec.endsWith( "*" ) )
        {
            addPackageImport( spec.substring( 0,
                                              spec.length() - 1 ) );
        }
        else
        {
            addClassImport( spec );
        }
    }

    protected void addPackageImport(String pkg)
    {
        this.packages.add( pkg );
    }

    protected List getPackages()
    {
        return this.packages;
    }

    protected void addClassImport(String clazz)
    {
        int lastDot = clazz.lastIndexOf( "." );

        String className = null;

        if ( lastDot >= 0 )
        {
            className = clazz.substring( lastDot + 1 );
        }
        else
        {
            className = clazz;
        }

        this.classes.put( className,
                          clazz );
    }

    public Class checkPrimitives(String name)
    {
        if ( "int".equals( name ) )
        {
            return Integer.TYPE;
        }

        return null;
    }

    public Class resolveClass(String name) throws ClassNotFoundException
    {
        Class clazz = null;

        clazz = checkPrimitives( name );

        if ( clazz != null )
        {
            return clazz;
        }

        String  fullName = null;

        if ( name.indexOf(".") >= 0 )
        {
            fullName = name;
        }
        else
        {
            fullName = (String) this.classes.get( name );
        }

        if ( fullName != null )
        {
            clazz = Class.forName( fullName, true, Thread.currentThread().getContextClassLoader() );
        }
        else
        {
            Iterator pkgIter = getPackages().iterator();
            String   eachPkg = null;

            while ( pkgIter.hasNext() )
            {
                eachPkg = (String) pkgIter.next();

                fullName = eachPkg + name;

                try
                {
                    clazz = Class.forName( fullName, true, Thread.currentThread().getContextClassLoader() );
                    
                    if ( clazz != null )
                    {
                        break;
                    }
                }
                catch (ClassNotFoundException e)
                {
                    // nothing.
                }
            }
                
        }

        if ( clazz == null )
        {
            throw new ClassNotFoundException( name );
        }

        return clazz;
    }
}

