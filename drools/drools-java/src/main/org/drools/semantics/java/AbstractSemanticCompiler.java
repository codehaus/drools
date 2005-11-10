package org.drools.semantics.java;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

import org.apache.commons.jci.compilers.CompilationResult;
import org.apache.commons.jci.compilers.JavaCompiler;
import org.apache.commons.jci.readers.FileResourceReader;
import org.apache.commons.jci.stores.FileResourceStore;

public abstract class AbstractSemanticCompiler
{
    protected JavaCompiler compiler;

    protected void write(StringBuffer buffer,
                             String className,
                             String fileName,
                             File srcDir,
                             File dstDir) throws IOException
    {
        FileResourceReader src = new FileResourceReader( srcDir );
        FileResourceStore dst = new FileResourceStore( dstDir );

        char[] source = new char[buffer.length()];
        buffer.getChars( 0,
                         buffer.length(),
                         source,
                         0 );

        writeFile( srcDir,
                   fileName,
                   source );

    }

    protected File writeFile(File directory,
                             final String pName,
                             final char[] pText) throws IOException
    {
        final File file = new File( directory,
                                    pName );
        final File parent = file.getParentFile();
        
        // Make sure the parent folder exists
        if ( !parent.exists() && !parent.mkdirs() )
        {
            throw new IOException( "could not create directory '" + parent );
        }
        
        try
        {
            final FileWriter writer = new FileWriter( file );
            writer.write( pText );
            writer.close();
        }
        catch ( IOException e )
        {
            throw new IOException( "Unable to write code to file: " + e.getMessage() ); 
        }

        return file;
    }
    
   
    public void compile(String fileName,
                        File srcDir,
                        File dstDir) 
    {
        compile( new String[] { fileName },
                 srcDir,
                 dstDir );
    }
    
    public void compile(String[] filesNames,
                        File srcDir,
                        File dstDir) 
    {
        FileResourceReader src = new FileResourceReader( srcDir );
        FileResourceStore dst = new FileResourceStore( dstDir );      
        
        ClassLoader classLoader = null;
        
        try 
        {                    
            classLoader = new URLClassLoader( new URL[] { srcDir.toURL(), 
                                              dstDir.toURL() } );
        }
        catch (Exception e)
        {
            e.printStackTrace();
            //@todo make this add some intelligent error
            //swallow
        }
        
        CompilationResult result = compiler.compile( filesNames ,
                                                     src,
                                                     dst,
                                                     classLoader );

        for ( int i = 0; i < result.getErrors().length; i++ )
        {
            System.out.println( result.getErrors()[i].getFileName() + "(" + result.getErrors()[i].getStartLine() + ")" + "\n" + result.getErrors()[i].getMessage() );
        }

    }
}
