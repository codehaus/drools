package org.drools.semantics.groovy;

import org.apache.commons.jci.compilers.JavaCompiler;
import org.apache.commons.jci.compilers.JavaCompilerFactory;
import org.drools.semantics.java.JavaSemanticRuleCompiler;
import org.drools.smf.SemanticRuleCompiler;

public class GroovySemanticRuleCompiler extends JavaSemanticRuleCompiler
{
    private static SemanticRuleCompiler INSTANCE;

    public static SemanticRuleCompiler getInstance()
    {
        if ( GroovySemanticRuleCompiler.INSTANCE == null )
        {
            GroovySemanticRuleCompiler.INSTANCE = new GroovySemanticRuleCompiler( "groovy",
                                                                                  JavaCompilerFactory.getInstance().createCompiler( JavaCompilerFactory.GROOVY ) );
        }
        return GroovySemanticRuleCompiler.INSTANCE;
    }

    protected GroovySemanticRuleCompiler(String semanticType,
                                         JavaCompiler compiler)
    {
        super( semanticType,
               compiler );
    }
}
