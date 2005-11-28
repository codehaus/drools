package org.drools.semantics.groovy;

import org.apache.commons.jci.compilers.JavaCompiler;
import org.apache.commons.jci.compilers.JavaCompilerFactory;
import org.drools.semantics.java.JavaSemanticFunctionsCompiler;
import org.drools.smf.SemanticFunctionsCompiler;

public class GroovySemanticFunctionsCompiler extends JavaSemanticFunctionsCompiler
{
    private static SemanticFunctionsCompiler INSTANCE;
    
    public static SemanticFunctionsCompiler getInstance()
    {
        if ( GroovySemanticFunctionsCompiler.INSTANCE == null ) 
        {
            GroovySemanticFunctionsCompiler.INSTANCE = new GroovySemanticFunctionsCompiler( "groovy", 
                                                                                            JavaCompilerFactory.getInstance().createCompiler( JavaCompilerFactory.GROOVY ) );
        }
        return GroovySemanticFunctionsCompiler.INSTANCE;
    }
    
    protected GroovySemanticFunctionsCompiler(String semanticType, JavaCompiler compiler)
    {
        super( semanticType, 
               compiler );
    }    
  
}
