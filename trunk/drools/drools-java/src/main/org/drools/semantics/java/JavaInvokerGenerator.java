package org.drools.semantics.java;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.drools.rule.Declaration;
import org.drools.semantics.base.ClassObjectType;

public class JavaInvokerGenerator
{
    private static final JavaInvokerGenerator INSTANCE = new JavaInvokerGenerator();

    private static final String               newline  = System.getProperty( "line.separator" );

    public static JavaInvokerGenerator getInstance()
    {
        return INSTANCE;
    }

    private JavaInvokerGenerator()
    {

    }

    public StringBuffer generateInvoker(String ruleName,
                                        String className,
                                        String methodName,
                                        String returnType,
                                        String knowledgeHelper,
                                        Map applicationData,
                                        Set usedApplicationData,
                                        Declaration[] declarations,
                                        int indentSize)
    {

        StringBuffer buffer = new StringBuffer();

        StringBuffer call = new StringBuffer();

        String indent = indent( indentSize );

        call.append( indent );
        if ( !returnType.equals( "void" ) )
        {
            call.append( "        return " );
        }
        else
        {
            call.append( "        " );
        }
        call.append( ruleName );
        call.append( "." );
        call.append( methodName );
        call.append( "( " );
        String callIndent = indent( call.length() );

        int i = 0;
        if ( knowledgeHelper != null )
        {
            call.append( knowledgeHelper );
            i++;
        }

        String classIndent = indent( indentSize );
        buffer.append( classIndent );
        buffer.append( "public static class " );
        buffer.append( className );
        buffer.append( "Invoker" );
        if ( !returnType.equals( "void" ) )
        {
            buffer.append( " implements ConditionInvoker" );
            buffer.append( newline );
        }
        else
        {
            buffer.append( " implements ConsequenceInvoker" );
            buffer.append( newline );
        }
        buffer.append( classIndent );
        buffer.append( "{" );
        buffer.append( newline );

        String methodIndent = indent( indentSize + 4 );
        buffer.append( methodIndent );
        buffer.append( "public " );
        buffer.append( returnType );
        buffer.append( " invoke(final Tuple tuple," );
        buffer.append( newline );
        buffer.append( methodIndent );
        buffer.append( "                      final Declaration[] declarations," );
        buffer.append( newline );
        buffer.append( methodIndent );
        if ( returnType.equals( "void" ) )
        {
            buffer.append( "                      final KnowledgeHelper " );
            buffer.append( knowledgeHelper );
            buffer.append( "," );
            buffer.append( newline );
        }
        buffer.append( methodIndent );
        buffer.append( "                      final Map applicationData) throws Exception" );
        buffer.append( newline );
        buffer.append( methodIndent );
        buffer.append( "{" );
        buffer.append( newline );
        String identifier = null;
        Class clazz = null;
        Iterator it = usedApplicationData.iterator();
        while ( it.hasNext() )
        {
            identifier = (String) it.next();
            if ( i > 0 )
            {
                call.append( "," );
                buffer.append( newline );
                call.append( callIndent );
            }
            i++;
            call.append( identifier );
            clazz = (Class) applicationData.get( identifier );
            buffer.append( methodIndent );
            buffer.append( "    final " );
            buffer.append( clazz.getName() );
            buffer.append( " " );
            buffer.append( identifier );
            buffer.append( " = " );
            buffer.append( "( " );
            buffer.append( clazz.getName() );
            buffer.append( " ) applicationData.get( \"" );
            buffer.append( identifier );
            buffer.append( "\" );" );
            buffer.append( newline );
        }

        Declaration declaration = null;
        for ( int j = 0; j < declarations.length; j++ )
        {
            declaration = declarations[j];
            identifier = declaration.getIdentifier();
            if ( i > 0 )
            {
                call.append( "," );
                call.append( newline );
                call.append( callIndent );
            }
            i++;
            call.append( identifier );
            clazz = ((ClassObjectType) declaration.getObjectType()).getType();
            buffer.append( methodIndent );
            buffer.append( "    final " );
            buffer.append( clazz.getName() );
            buffer.append( " " );
            buffer.append( identifier );
            buffer.append( " = " );
            buffer.append( "( " );
            buffer.append( clazz.getName() );
            buffer.append( " )" );
            buffer.append( "tuple.get( " );
            buffer.append( "declarations[" );
            buffer.append( j );
            buffer.append( "]" );
            buffer.append( " );" );
            buffer.append( newline );
        }

        call.append( " );" );
        call.append( newline );
        buffer.append( call );
        buffer.append( methodIndent );
        buffer.append( "}" );
        buffer.append( newline );
        buffer.append( classIndent );
        buffer.append( "}" );
        buffer.append( newline );
        buffer.append( newline );
        return buffer;

    }

    private String indent(int i)
    {
        StringBuffer buffer = new StringBuffer();
        for ( ; i > 0; i-- )
        {
            buffer.append( " " );
        }
        return buffer.toString();
    }

}
