package org.drools.io;

import org.drools.smf.Configuration;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.Iterator;

class DefaultConfiguration
    implements Configuration
{
    private static final String[] EMPTY_STRING_ARRAY = new String[0];

    private String name;
    private String text;
    private Map attrs;
    private List children;

    DefaultConfiguration(String name)
    {
        this.name     = name;
        this.attrs    = new HashMap();
        this.children = new ArrayList();
    }

    public String getName()
    {
        return this.name;
    }

    void setText(String text)
    {
        this.text = text;
    }

    public String getText()
    {
        return this.text;
    }

    void setAttribute(String name,
                      String value)
    {
        this.attrs.put( name,
                        value );
    }

    public String getAttribute(String name)
    {
        return (String) this.attrs.get( name );
    }

    public String[] getAttributeNames()
    {
        return (String[]) this.attrs.keySet().toArray( EMPTY_STRING_ARRAY );
    }

    void addChild(Configuration config)
    {
        this.children.add( config );
    }

    public Configuration getChild(String name)
    {
        for ( Iterator childIter = this.children.iterator();
              childIter.hasNext(); )
        {
            Configuration eachConfig = (Configuration) childIter.next();

            if ( eachConfig.getName().equals( name ) )
            {
                return eachConfig;
            }
        }

        return null;
    }

    public Configuration[] getChildren(String name)
    {
        List result = new ArrayList();

        for ( Iterator childIter = this.children.iterator();
              childIter.hasNext(); )
        {
            Configuration eachConfig = (Configuration) childIter.next();

            if ( eachConfig.getName().equals( name ) )
            {
                result.add( eachConfig );
            }
        }

        return (Configuration[]) result.toArray( Configuration.EMPTY_ARRAY );
    }

    public Configuration[] getChildren()
    {
        return (Configuration[]) this.children.toArray( Configuration.EMPTY_ARRAY );
    }
}
