package org.drools.smf;

import java.io.File;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import org.drools.spi.Functions;

public interface SemanticFunctionsCompiler
    extends
    SemanticCompiler
{

    public void generate(Functions functions,
                         Set imports,
                         String packageName,
                         String className,
                         String parentClass,
                         File src,
                         File dst,
                         Map files) throws IOException;

    public void compile(String fileName,
                        File srcDir,
                        File dstDir);

    public void compile(String[] filesNames,
                        File srcDir,
                        File dstDir);

}
