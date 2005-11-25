package org.drools.semantics.java;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.jci.CompilerFactory;
import org.apache.commons.jci.compilers.JavaCompiler;
import org.apache.commons.jci.readers.ResourceReader;
import org.apache.commons.jci.stores.ResourceStore;
import org.drools.rule.Declaration;
import org.drools.smf.SemanticCondition;
import org.drools.smf.SemanticConsequence;
import org.drools.smf.SemanticRule;
import org.drools.smf.SemanticRuleCompiler;
import org.drools.spi.Condition;
import org.drools.spi.Consequence;
import org.drools.spi.RuleComponent;

public class JavaSemanticRuleCompiler extends AbstractSemanticCompiler
    implements
    SemanticRuleCompiler
{
    private static SemanticRuleCompiler INSTANCE;

    private final String                semanticType;

    public static SemanticRuleCompiler getInstance()
    {
        if ( JavaSemanticRuleCompiler.INSTANCE == null )
        {
            JavaSemanticRuleCompiler.INSTANCE = new JavaSemanticRuleCompiler( "java",
                                                                              CompilerFactory.getInstance().newCompiler( CompilerFactory.ECLIPSE ) );
        }
        return JavaSemanticRuleCompiler.INSTANCE;
    }

    protected JavaSemanticRuleCompiler(String semanticType,
                                       JavaCompiler compiler)
    {
        this.semanticType = semanticType;
        this.compiler = compiler;
    }

    public String getSemanticType()
    {
        return this.semanticType;
    }

    public String getFileExtension()
    {
        return this.semanticType;
    }

    public void generate(RuleComponent[] components,
                         Declaration[] declarations,
                         Set imports,
                         Map applicationData,
                         String packageName,
                         String className,
                         String parentClass,
                         String knowledgeHelper,
                         ResourceReader src,
                         Map files) throws IOException
    {
        JavaMethodGenerator methodGenerator = JavaMethodGenerator.getInstance();
        JavaInvokerGenerator invokerGenerator = JavaInvokerGenerator.getInstance();

        SemanticRule component = null;
        SemanticCondition condition = null;
        SemanticConsequence consequence = null;

        /* needs to be LinkedHashSet() to ensure the order is the same */
        Set usedApplicationData = new LinkedHashSet();
        List methods = new ArrayList();
        List invokers = new ArrayList();
        for ( int i = 0; i < components.length; i++ )
        {
            usedApplicationData.clear();
            component = (SemanticRule) components[i];
            if ( component instanceof Condition )
            {
                condition = (SemanticCondition) component;
                StringBuffer conditionBuffer = methodGenerator.generateMethod( condition.getName(),
                                                                               "boolean",
                                                                               condition.getThrownException(),
                                                                               condition.getText(),
                                                                               imports,
                                                                               usedApplicationData,
                                                                               null,
                                                                               applicationData,
                                                                               ((Condition) condition).getRequiredTupleMembers(),
                                                                               4 );
                String name = condition.getName();
                StringBuffer invokerBuffer = invokerGenerator.generateInvoker( className,
                                                                               name.toUpperCase().charAt( 0 ) + name.substring( 1 ),
                                                                               name,
                                                                               "boolean",
                                                                               null,
                                                                               applicationData,
                                                                               usedApplicationData,
                                                                               ((Condition) condition).getRequiredTupleMembers(),
                                                                               4 );
                methods.add( conditionBuffer );
                invokers.add( invokerBuffer );

            }
            else if ( component instanceof Consequence )
            {
                consequence = (SemanticConsequence) component;
                String name = consequence.getName();
                StringBuffer consequenceBuffer = methodGenerator.generateMethod( consequence.getName(),
                                                                                 "void",
                                                                                 consequence.getThrownException(),
                                                                                 consequence.getText(),
                                                                                 imports,
                                                                                 usedApplicationData,
                                                                                 knowledgeHelper,
                                                                                 applicationData,
                                                                                 declarations,
                                                                                 4 );

                StringBuffer invokerBuffer = invokerGenerator.generateInvoker( className,
                                                                               name.toUpperCase().charAt( 0 ) + name.substring( 1 ),
                                                                               name,
                                                                               "void",
                                                                               knowledgeHelper,
                                                                               applicationData,
                                                                               usedApplicationData,
                                                                               declarations,
                                                                               4 );
                methods.add( consequenceBuffer );
                invokers.add( invokerBuffer );
            }
        }

        JavaClassGenerator classGenerator = JavaClassGenerator.getInstance();
        StringBuffer ruleClass = classGenerator.generateClass( packageName,
                                                               className,
                                                               parentClass,
                                                               methods,
                                                               imports );

        String fileName = packageName.replaceAll( "\\.",
                                                  "/" ) + "/" + className + "." + getSemanticType();
        write( ruleClass,
               packageName + "." + className,
               fileName,
               src );

        List list = (List) files.get( this );
        if ( list == null )
        {
            list = new ArrayList();
        }
        list.add( packageName + "." + className );
        files.put( this,
                   list );

        ruleClass = classGenerator.generateClass( packageName,
                                                  className + "Invoker",
                                                  null,
                                                  invokers,
                                                  imports );
        fileName = packageName.replaceAll( "\\.",
                                           "/" ) + "/" + className + "Invoker.java";
        write( ruleClass,
               packageName + "." + className + "Invoker",
               fileName,
               src );

        files = (Map) files.get( "invokers" );
        list = (List) files.get( JavaSemanticRuleCompiler.getInstance() );
        if ( list == null )
        {
            list = new ArrayList();
        }
        list.add( packageName + "." + className + "Invoker" );
        files.put( JavaSemanticRuleCompiler.getInstance(),
                   list );

    }

    public boolean equals(Object object)
    {
        if ( object == null )
        {
            return false;
        }

        if ( object instanceof JavaSemanticRuleCompiler )
        {
            JavaSemanticRuleCompiler other = (JavaSemanticRuleCompiler) object;
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
