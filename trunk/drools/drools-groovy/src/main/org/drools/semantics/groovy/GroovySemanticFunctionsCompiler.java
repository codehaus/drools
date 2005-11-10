package org.drools.semantics.groovy;

import org.apache.commons.jci.CompilerFactory;
import org.apache.commons.jci.compilers.JavaCompiler;
import org.drools.semantics.java.JavaSemanticFunctionsCompiler;
import org.drools.smf.SemanticFunctionsCompiler;

public class GroovySemanticFunctionsCompiler extends JavaSemanticFunctionsCompiler
    implements
    SemanticFunctionsCompiler
{
    private static SemanticFunctionsCompiler INSTANCE;
    
    public static SemanticFunctionsCompiler getInstance()
    {
        if ( GroovySemanticFunctionsCompiler.INSTANCE == null ) 
        {
            GroovySemanticFunctionsCompiler.INSTANCE = new GroovySemanticFunctionsCompiler( "groovy", 
                                                                                             CompilerFactory.getInstance().newCompiler( CompilerFactory.GROOVY ) );
        }
        return GroovySemanticFunctionsCompiler.INSTANCE;
    }
    
    protected GroovySemanticFunctionsCompiler(String semanticType, JavaCompiler compiler)
    {
        super( semanticType, 
               compiler );
    }    
  
}
