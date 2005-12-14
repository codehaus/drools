package org.drools.semantics.java;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.jci.compilers.JavaCompiler;
import org.apache.commons.jci.compilers.JavaCompilerFactory;
import org.apache.commons.jci.readers.ResourceReader;
import org.drools.smf.SemanticFunctionsCompiler;
import org.drools.spi.Functions;

public class JavaSemanticFunctionsCompiler extends AbstractSemanticCompiler
    implements
    SemanticFunctionsCompiler
{
    private static SemanticFunctionsCompiler INSTANCE;

    private final String                     semanticType;

    public static SemanticFunctionsCompiler getInstance()
    {
        if ( JavaSemanticFunctionsCompiler.INSTANCE == null )
        {
            JavaSemanticFunctionsCompiler.INSTANCE = new JavaSemanticFunctionsCompiler( "java",
                                                                                        JavaCompilerFactory.getInstance().createCompiler( JavaCompilerFactory.ECLIPSE ) );
        }
        return JavaSemanticFunctionsCompiler.INSTANCE;
    }

    protected JavaSemanticFunctionsCompiler(String semanticType,
                                            JavaCompiler compiler)
    {
        this.semanticType = semanticType;
        this.compiler = compiler;
    }

    public void generate(Functions functions,
                         Set imports,
                         String packageName,
                         String className,
                         String parentClass,
                         ResourceReader src,
                         Map files) throws IOException
    {
        List methods = new ArrayList();
        methods.add( functions.getText() );

        JavaClassGenerator classGenerator = JavaClassGenerator.getInstance();
        StringBuffer ruleClass = classGenerator.generateClass( packageName,
                                                               className,
                                                               parentClass,
                                                               methods,
                                                               imports );

        String fileName = packageName.replaceAll( "\\.",
                                                  "/" ) + "/" + className + "." + this.semanticType;
        write( ruleClass,
               packageName + "." + className,
               fileName,
               src);

        List list = (List) files.get( this.semanticType );
        if ( list == null )
        {
            list = new ArrayList();
        }
        list.add( packageName + "." + className );
        files.put( this,
                   list );
    }

    public String getSemanticType()
    {
        return this.semanticType;
    }

    public String getFileExtension()
    {
        return this.semanticType;
    }

    public boolean equals(Object object)
    {
        if ( object == null )
        {
            return false;
        }

        if ( object instanceof SemanticFunctionsCompiler )
        {
            SemanticFunctionsCompiler other = (SemanticFunctionsCompiler) object;
            return (this.getSemanticType().equals( other.getSemanticType() ));

        }
        else
        {
            return false;
        }
    }

    public int hashcode()
    {
        return this.semanticType.hashCode();
    }

}
