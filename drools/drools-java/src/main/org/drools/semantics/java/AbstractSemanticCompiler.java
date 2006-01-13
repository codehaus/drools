package org.drools.semantics.java;

import java.io.IOException;

import org.apache.commons.jci.compilers.CompilationResult;
import org.apache.commons.jci.compilers.JavaCompiler;
import org.apache.commons.jci.readers.MemoryResourceReader;
import org.apache.commons.jci.readers.ResourceReader;
import org.apache.commons.jci.stores.ResourceStore;
import org.apache.commons.jci.stores.ResourceStoreClassLoader;

/**
 * This is the real Java semantic compiler engine. It uses JCI to compile the
 * generated source.
 */
public abstract class AbstractSemanticCompiler {
    protected JavaCompiler compiler;

    protected void write(StringBuffer buffer,
                         String className,
                         String fileName,
                         ResourceReader src) throws IOException {

        char[] source = new char[buffer.length()];
        buffer.getChars( 0,
                         buffer.length(),
                         source,
                         0 );

        writeFile( src,
                   fileName,
                   source );

    }

    protected void writeFile(ResourceReader src,
                             final String name,
                             final char[] text) throws IOException {
        if ( src instanceof MemoryResourceReader ) {
            MemoryResourceReader memorySrc = (MemoryResourceReader) src;
            memorySrc.addFile( name,
                               text );
        }
    }

    public void compile(String fileName,
                        ResourceReader src,
                        ResourceStore dst,
                        ClassLoader classLoader) {
        compile( new String[]{fileName},
                 src,
                 dst,
                 classLoader );
    }

    public void compile(String[] fileNames,
                        ResourceReader src,
                        ResourceStore dst,
                        ClassLoader classLoader) {

        CompilationResult result = compiler.compile( fileNames,
                                                     src,
                                                     dst,
                                                     classLoader );

        handleAnyErrors( result );

    }

    private void handleAnyErrors(CompilationResult result) {
        if ( result.getErrors().length > 0 ) {
            throw new JavaSemanticCompileError( result.getErrors() );
        }
    }
}
