package org.drools.io;

import org.drools.smf.SemanticModule;
import org.drools.smf.SimpleSemanticModule;
import org.drools.spi.ObjectType;
import org.drools.spi.Condition;
import org.drools.spi.Extractor;
import org.drools.spi.Consequence;

import java.io.InputStream;
import java.net.URL;
import java.util.Properties;
import java.util.Enumeration;

public class SemanticsReader
{
    public SemanticsReader()
    {

    }

    public SemanticModule read(URL url)
        throws Exception
    {
        InputStream in = url.openStream();

        try
        {
            return read( in );
        }
        finally
        {
            in.close();
        }
    }

    public SemanticModule read(InputStream in)
        throws Exception
    {
        ClassLoader cl = Thread.currentThread().getContextClassLoader();

        if ( cl == null )
        {
            cl = SemanticsReader.class.getClassLoader();
        }

        Properties props = new Properties();

        props.load( in );

        String uri = props.getProperty( "module.uri" );

        if ( uri == null
             ||
             uri.trim().equals( "" ) )
        {
            throw new Exception( "module.uri must be specified" );
        }

        SimpleSemanticModule module = new SimpleSemanticModule( uri.trim() );

        for ( Enumeration propNames = props.propertyNames();
              propNames.hasMoreElements(); )
        {
            String componentName = (String) propNames.nextElement();

            if ( componentName.equals( "module.uri" ) )
            {
                continue;
            }

            String className     = props.getProperty( componentName );

            Class componentClass = cl.loadClass( className );

            if ( ObjectType.class.isAssignableFrom( componentClass ) )
            {
                module.addObjectType( componentName,
                                      componentClass );
            }
            else if ( Condition.class.isAssignableFrom( componentClass ) )
            {
                module.addCondition( componentName,
                                     componentClass );
            }
            else if ( Extractor.class.isAssignableFrom( componentClass ) )
            {
                module.addExtractor( componentName,
                                     componentClass );
            }
            else if ( Consequence.class.isAssignableFrom( componentClass ) )
            {
                module.addConsequence( componentName,
                                       componentClass );
            }
        }

        return module;
    }
}
