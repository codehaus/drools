package org.drools.semantics.java;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.commons.jci.compilers.CompilationResult;
import org.apache.commons.jci.compilers.JavaCompiler;
import org.apache.commons.jci.readers.FileResourceReader;
import org.apache.commons.jci.readers.MemoryResourceReader;
import org.apache.commons.jci.readers.ResourceReader;
import org.apache.commons.jci.stores.FileResourceStore;
import org.apache.commons.jci.stores.ResourceStore;

public abstract class AbstractSemanticCompiler
{
    protected JavaCompiler compiler;

    protected void write(StringBuffer buffer,
                             String className,
                             String fileName,
                             ResourceReader src) throws IOException
    {

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
                             final char[] text) throws IOException
    {
        if (src instanceof MemoryResourceReader) {
            MemoryResourceReader memorySrc = (MemoryResourceReader) src;
            memorySrc.addFile(name, text);
        }
    }
    
   
    public void compile(String fileName,
                        ResourceReader src,
                        ResourceStore dst,
                        ClassLoader classLoader) 
    {
        compile( new String[] { fileName },
                 src,
                 dst,
                 classLoader);
    }
    
    public void compile(String[] fileNames,
                        ResourceReader src,
                        ResourceStore dst,
                        ClassLoader classLoader) 
    {     
        
        CompilationResult result = compiler.compile( fileNames ,
                                                     src,
                                                     dst,
                                                     classLoader );

        for ( int i = 0; i < result.getErrors().length; i++ )
        {
            System.out.println( result.getErrors()[i].getFileName() + "(" + result.getErrors()[i].getStartLine() + ")" + "\n" + result.getErrors()[i].getMessage() );
        }

    }
}
