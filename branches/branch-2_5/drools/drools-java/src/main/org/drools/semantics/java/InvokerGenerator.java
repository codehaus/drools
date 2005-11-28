package org.drools.semantics.java;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.drools.rule.Declaration;

public class InvokerGenerator
{

    public String generate(String methodName,
                           String returnType,
                           Map applicationData,
                           Set usedApplicationData,
                           Declaration[] declarations)
    {
        StringBuffer buffer = new StringBuffer();

        buffer.append( "package org.drools;\n" );
        buffer.append( "public " );
        buffer.append( returnType );
        buffer.append( " " );
        buffer.append( methodName );
        buffer.append( "(" );

        /*
         * values[i] = new DefaultKnowledgeHelper( rule, tuple ); i++;
         */

        String identifier = null;
        Class clazz = null;
        Iterator it = usedApplicationData.iterator();
        while ( it.hasNext() )
        {
            identifier = (String) it.next();
            clazz = (Class) applicationData.get( identifier );
            buffer.append( clazz.getName() );
            buffer.append( " " );
            buffer.append( identifier );
            buffer.append( " " );
            buffer.append( "applicationData.get(\"" );
            buffer.append( identifier );
            buffer.append( "\");\n" );
        }

        Declaration declaration = null;
        for ( int i = 0; i < declarations.length; i++ )
        {
            declaration = declarations[i];
            identifier = declaration.getIdentifier();
            clazz = declaration.getClass();
            buffer.append( clazz.getName() );
            buffer.append( " " );
            buffer.append( identifier );
            buffer.append( " " );
            buffer.append( "tuple.get(\"" );
            buffer.append( identifier );
            buffer.append( "\");\n" );
        }

        return buffer.toString();

    }
}
