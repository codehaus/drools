package org.drools.semantics.java;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class JavaClassGenerator
{
    private static final JavaClassGenerator INSTANCE = new JavaClassGenerator();

    private static final String             newline  = System.getProperty( "line.separator" );

    public static JavaClassGenerator getInstance()
    {
        return INSTANCE;
    }

    private JavaClassGenerator()
    {

    }

    public StringBuffer generateClass(String packageName,
                                      String className,
                                      String parentClass,
                                      List methods,
                                      Set imports)
    {
        imports.add( "java.util.Map" );
        imports.add( "org.drools.rule.Declaration" );
        imports.add( "org.drools.spi.Tuple" );
        imports.add( "java.io.Serializable" );

        StringBuffer buffer = new StringBuffer();
        buffer.append( "package " );
        buffer.append( packageName );
        buffer.append( ";" );
        buffer.append( newline );
        buffer.append( newline );
        Iterator it = imports.iterator();
        while ( it.hasNext() )
        {
            buffer.append( "import " );
            buffer.append( it.next() );
            buffer.append( ";" );
            buffer.append( newline );

        }
        buffer.append( newline );
        buffer.append( "public class " );
        buffer.append( className );
        if ( (parentClass != null) && (!parentClass.equals( "" )) )
        {
            buffer.append( " extends " );
            buffer.append( parentClass );
        }
        buffer.append( " implements Serializable" );
        buffer.append( newline );
        buffer.append( "{" );
        buffer.append( newline );

        it = methods.iterator();
        while ( it.hasNext() )
        {
            buffer.append( it.next() );
        }

        buffer.append( "}" );
        buffer.append( newline );

        return buffer;
    }
}
