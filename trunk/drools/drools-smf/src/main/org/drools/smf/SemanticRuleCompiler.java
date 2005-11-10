package org.drools.smf;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.drools.rule.Declaration;
import org.drools.spi.RuleComponent;

public interface SemanticRuleCompiler
    extends
    SemanticCompiler
{
    public void generate(RuleComponent[] components,
                         Declaration[] declarations,
                         Set imports,
                         Map applicationData,
                         String packageName,
                         String className,
                         String lastClassName,
                         String knowledgeHelper,
                         File src,
                         File dst,
                         Map files)  throws IOException;

    public void compile(String fileName,
                        File srcDir,
                        File dstDir);
    
    public void compile(String[] filesNames,
                        File srcDir,
                        File dstDir);
}
