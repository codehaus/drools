package org.drools.semantics.java;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.drools.rule.Declaration;
import org.drools.semantics.base.ClassObjectType;
import org.drools.spi.KnowledgeHelper;
import org.drools.spi.ObjectType;

/**
 * 
 * @todo make sure its solid for nested/inner class definitions
 * @todo still uding == '\n' should work with newline
 * 
 * @author mproctor
 * 
 */
public class JavaMethodGenerator
{
    private static final JavaMethodGenerator INSTANCE = new JavaMethodGenerator();

    private static final String              newline  = System.getProperty( "line.separator" );

    public static JavaMethodGenerator getInstance()
    {
        return INSTANCE;
    }

    private JavaMethodGenerator()
    {

    }

    public StringBuffer generateMethod(String methodName,
                                       String returnType,
                                       String exception,
                                       String script,
                                       Set imports,
                                       Set usedApplicationData,
                                       String knowledgeHelper,
                                       Map applicationData,
                                       Declaration[] declarations,
                                       int indentSize)
    {
        Parameter[] source = getParameters( applicationData,
                                            usedApplicationData,
                                            imports,
                                            script );

        int size = usedApplicationData.size() + declarations.length;
        int offset = 0;
        if ( knowledgeHelper != null )
        {
            size++;
        }

        Parameter[] parameters = new Parameter[size];
        if ( knowledgeHelper != null )
        {
            imports.add( KnowledgeHelper.class.getName() );
            parameters[0] = new Parameter( KnowledgeHelper.class.getName(),
                                           knowledgeHelper );
            offset = 1;
        }

        System.arraycopy( source,
                          0,
                          parameters,
                          offset,
                          source.length );

        offset += source.length;
        source = getParameters( declarations,
                                imports );

        System.arraycopy( source,
                          0,
                          parameters,
                          offset,
                          source.length );

        StringBuffer buffer = generateMethod( methodName,
                                              returnType,
                                              exception,
                                              script,
                                              imports,
                                              parameters,
                                              indentSize );

        return buffer;
    }

    private Parameter[] getParameters(Map applicationData,
                                      Set usedApplicationData,
                                      Set imports,
                                      String script)
    {
        List list = new ArrayList();

        Set keys = applicationData.keySet();
        Iterator it = keys.iterator();
        String key;
        Class clazz;
        String type;
        int nestedClassPosition;

        while ( it.hasNext() )
        {
            key = (String) it.next();

            /*
             * poor mans check. Only add the application variable if it appears as text in the script
             */
            if ( script.indexOf( key ) == -1 )
            {
                continue;
            }
            clazz = (Class) applicationData.get( key );

            type = clazz.getName();
            nestedClassPosition = type.indexOf( '$' );

            if ( nestedClassPosition != -1 )
            {
                type = type.substring( 0,
                                       nestedClassPosition );
            }

            usedApplicationData.add( key );

            imports.add( type );

            list.add( new Parameter( type,
                                     key ) );
        }

        return (Parameter[]) list.toArray( new Parameter[list.size()] );
    }

    private Parameter[] getParameters(Declaration[] declarations,
                                      Set imports)
    {
        List list = new ArrayList();

        ObjectType objectType;
        Class clazz;
        String identifier;
        Declaration declaration;

        String type;
        int nestedClassPosition;

        for ( int i = 0; i < declarations.length; i++ )
        {
            declaration = declarations[i];
            identifier = declaration.getIdentifier();
            objectType = declaration.getObjectType();

            clazz = ((ClassObjectType) objectType).getType();

            type = clazz.getName();
            nestedClassPosition = type.indexOf( '$' );

            if ( nestedClassPosition != -1 )
            {
                type = type.substring( 0,
                                       nestedClassPosition );
            }

            imports.add( type );

            list.add( new Parameter( type,
                                     identifier ) );
            ;
        }

        return (Parameter[]) list.toArray( new Parameter[list.size()] );
    }

    public StringBuffer generateMethod(String methodName,
                                       String returnType,
                                       String exception,
                                       String script,
                                       Set imports,
                                       Parameter[] parameters,
                                       int indentSize)
    {
        StringBuffer buffer = new StringBuffer();

        int lastImportIndex = getLastImportIndex( script,
                                                  imports );
        String indent = indent( indentSize );

        StringBuffer buffer2 = new StringBuffer();
        buffer2.append( indent );
        buffer2.append( "public static " );
        buffer2.append( returnType );
        buffer2.append( " " );
        buffer2.append( methodName );
        buffer2.append( "(" );
        indentSize = buffer2.length();
        indent = indent( indentSize );

        buffer.append( buffer2 );

        if ( parameters.length != 0 )
        {
            buffer.append( createParameterStringBuffer( parameters,
                                                        indent( indentSize ),
                                                        false ) );
        }

        indentSize = 4;
        indent = indent( indentSize );
        buffer.append( ") " );
        if ( exception != null )
        {
            buffer.append( "throws " );
            buffer.append( exception );
        }
        buffer.append( newline );
        buffer.append( indent );
        buffer.append( "{" );
        buffer.append( newline );
        buffer.append( indent );
        buffer.append( indent );

        if ( !returnType.equals( "void" ) )
        {
            imports.add( "org.drools.smf.ConditionInvoker" );
            buffer.append( "return ( " );
            buffer.append( script.substring( lastImportIndex ) );
            while ( buffer.charAt( buffer.length() - 1 ) == '\n' )
            {
                buffer.deleteCharAt( buffer.length() - 1 );
            }
            buffer.append( " );" );
            buffer.append( newline );

        }
        else
        {
            imports.add( "org.drools.smf.ConsequenceInvoker" );
            buffer.append( script.substring( lastImportIndex ) );
            if ( script.charAt( script.length() - 1 ) != '\n' )
            {
                buffer.append( newline );
            }
        }

        buffer.append( indent );
        buffer.append( "}" );
        buffer.append( newline );
        buffer.append( newline );

        return buffer;
    }

    private StringBuffer createParameterStringBuffer(Parameter[] parameters,
                                                     String indent,
                                                     boolean indentFirst)
    {
        StringBuffer buffer = new StringBuffer();
        String type = null;
        for ( int i = 0; i < parameters.length; i++ )
        {
            if ( i > 0 )
            {
                buffer.append( "," );
                buffer.append( newline );
            }

            if ( indentFirst || i > 0 )
            {
                buffer.append( indent );
            }
            // its already imported so dont use fully qualified
            type = parameters[i].getType();
            buffer.append( " " );
            /* @todo: allow this to handle nested types and Classes with no packages */
            buffer.append( type.substring( type.lastIndexOf( '.' ) + 1,
                                           type.length() ) );
            buffer.append( " " );
            buffer.append( parameters[i].getIdentifier() );
        }

        return buffer;
    }

    /**
     * Return the index of the ; for the last import. Also removes imports from the Set that are already part of the script.
     * 
     * @param script
     * @param imports
     * @return
     * 
     * @todo should try and include relevant imports
     */
    private int getLastImportIndex(String script,
                                   Set imports)
    {
        char[] chars = script.toCharArray();

        boolean foundImport = false;
        for ( int i = 0; i < chars.length; i++ )
        {

            i = skipChars( chars,
                           i );

            if ( chars[i] == 'i' && chars[i + 1] == 'm' && chars[i + 2] == 'p' && chars[i + 3] == 'o' && chars[i + 4] == 'r' && chars[i + 5] == 't' )
            {
                i += 6;
                i = skipChars( chars,
                               i );
                foundImport = true;
            }
            else
            {
                return i;
            }

            // We use this to extract imports
            if ( foundImport )
            {
                StringBuffer buffer = new StringBuffer();
                while ( chars[i] != ';' && chars[i] != '/' && chars[i] != ' ' )
                {
                    buffer.append( chars[i] );
                    i++;
                }
                // we go back one as it will be incremented by the loop
                i--;

                imports.add( buffer.toString() );

                foundImport = false;
            }
        }

        return -1;
    }

    private int skipChars(char[] chars,
                          int i)
    {
        boolean loop = true;

        while ( loop )
        {
            switch ( chars[i] )
            {
                case '/' :
                    if ( chars[i + 1] == '*' )
                    {
                        // found '/*' skip till '*/'
                        i = skipSlashStarComments( chars,
                                                   i );
                    }
                    else
                    {
                        // found '//' spip till '\n'
                        while ( !(chars[i] == '\n') )
                        {
                            i++;
                        }
                    }
                    break;
                case ' ' :
                case '\n' :
                case ';' :
                    i++;
                    break;
                default :
                    loop = false;
            }
        }
        return i;
    }

    private int skipSlashStarComments(char[] chars,
                                      int i)
    {
        while ( !(chars[i] == '*' && chars[i + 1] == '/') )
        {
            i++;
        }
        i += 2;

        while ( chars[i] == ' ' )
        {
            i++;
        }

        // skip /* ... */
        if ( chars[i] == '/' && chars[i + 1] == '*' )
        {
            i = skipSlashStarComments( chars,
                                       i );
        }

        return i;
    }

    static class Parameter
    {
        private String type;
        private String identifier;

        public Parameter(String type,
                         String identifier)
        {
            this.type = type;
            this.identifier = identifier;
        }

        public String getType()
        {
            return this.type;
        }

        public String getIdentifier()
        {
            return this.identifier;
        }
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
