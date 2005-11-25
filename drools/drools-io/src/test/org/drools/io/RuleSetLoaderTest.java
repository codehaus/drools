package org.drools.io;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectStreamClass;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarInputStream;

import junit.framework.TestCase;

import org.drools.IntegrationException;
import org.drools.rule.RuleSet;
import org.drools.smf.RuleSetCompiler;
import org.xml.sax.SAXException;

public class RuleSetLoaderTest extends TestCase
{

    public void testAddFromUrl() throws IntegrationException,
                                SAXException,
                                IOException
    {
        RuleSetLoader ruleSetLoader = new RuleSetLoader();
        RuleSetCompiler[] compilers;

        // single URL parameter
        ruleSetLoader.addFromUrl( RuleSetLoaderTest.class.getResource( "simple.java.drl" ) );
        compilers = (RuleSetCompiler[]) ruleSetLoader.getRuleSets().values().toArray( new RuleSetCompiler[]{} );
        assertEquals( "simple",
                      compilers[0].getRuleSet().getName() );

        // single URL[] parameter        
        ruleSetLoader = new RuleSetLoader();
        ruleSetLoader.addFromUrl( new URL[]{RuleSetLoaderTest.class.getResource( "simple.java.drl" )} );
        compilers = (RuleSetCompiler[]) ruleSetLoader.getRuleSets().values().toArray( new RuleSetCompiler[]{} );
        assertEquals( "simple",
                      compilers[0].getRuleSet().getName() );
    }

    public void testLoadFromInputStream() throws IntegrationException,
                                         SAXException,
                                         IOException
    {
        RuleSetLoader ruleSetLoader = new RuleSetLoader();
        RuleSetCompiler[] compilers;

        // single InputStream parameter        
        ruleSetLoader.addFromInputStream( RuleSetLoaderTest.class.getResource( "simple.java.drl" ).openStream() );
        compilers = (RuleSetCompiler[]) ruleSetLoader.getRuleSets().values().toArray( new RuleSetCompiler[]{} );
        assertEquals( "simple",
                      compilers[0].getRuleSet().getName() );

        // single InputStream[] parameter 
        ruleSetLoader = new RuleSetLoader();
        ruleSetLoader.addFromInputStream( new InputStream[]{RuleSetLoaderTest.class.getResource( "simple.java.drl" ).openStream()} );
        compilers = (RuleSetCompiler[]) ruleSetLoader.getRuleSets().values().toArray( new RuleSetCompiler[]{} );
        assertEquals( "simple",
                      compilers[0].getRuleSet().getName() );
    }

    public void testLoadFromReader() throws IntegrationException,
                                    SAXException,
                                    IOException
    {
        RuleSetLoader ruleSetLoader = new RuleSetLoader();
        RuleSetCompiler[] compilers;

        // single InputStream parameter        
        ruleSetLoader.addFromReader( new InputStreamReader( RuleSetLoaderTest.class.getResource( "simple.java.drl" ).openStream() ) );
        compilers = (RuleSetCompiler[]) ruleSetLoader.getRuleSets().values().toArray( new RuleSetCompiler[]{} );
        assertEquals( "simple",
                      compilers[0].getRuleSet().getName() );

        // single InputStream[] parameter 
        ruleSetLoader = new RuleSetLoader();
        ruleSetLoader.addFromReader( new InputStreamReader[]{new InputStreamReader( RuleSetLoaderTest.class.getResource( "simple.java.drl" ).openStream() )} );
        compilers = (RuleSetCompiler[]) ruleSetLoader.getRuleSets().values().toArray( new RuleSetCompiler[]{} );
        assertEquals( "simple",
                      compilers[0].getRuleSet().getName() );
    }

    public void testSourceJar() throws IntegrationException,
                               SAXException,
                               IOException
    {
        RuleSetLoader ruleSetLoader = new RuleSetLoader();
        RuleSetCompiler[] compilers;

        // single URL parameter
        ruleSetLoader.addFromUrl( RuleSetLoaderTest.class.getResource( "simple.java.drl" ) );
        compilers = (RuleSetCompiler[]) ruleSetLoader.getRuleSets().values().toArray( new RuleSetCompiler[]{} );
        JarInputStream jis = new JarInputStream( new ByteArrayInputStream( compilers[0].getSourceDeploymentJar() ) );
        JarEntry entry = null;
        Map entries = new HashMap();
        Map classNames = new HashMap();

        ByteArrayOutputStream bos = null;

        // get all the entries from the jar
        while ( (entry = jis.getNextJarEntry()) != null )
        {
            bos = new ByteArrayOutputStream();
            copy( jis,
                  bos );
            //we have to remove the namespace datestamp, as its random
            String name = entry.getName();
            classNames.put( name.replaceAll( "_[\\d]+/",
                                             "_/" ),
                            name.replace( '/',
                                          '.' ).substring( 0,
                                                           name.length() - 5 ) );
            entries.put( name.replaceAll( "_[\\d]+/",
                                          "_/" ),
                         bos.toByteArray() );
        }
        assertEquals( 2,
                      entries.size() );

        assertTrue( entries.containsKey( "drools/org/simple_/java/Rule1_0.java" ) );
        assertTrue( entries.containsKey( "drools/org/simple_/java/Rule1_0Invoker.java" ) );

    }

    public void testBinaryJar() throws IntegrationException,
                               SAXException,
                               IOException
    {
        RuleSetLoader ruleSetLoader = new RuleSetLoader();
        RuleSetCompiler[] compilers;

        // single URL parameter
        ruleSetLoader.addFromUrl( RuleSetLoaderTest.class.getResource( "simple.java.drl" ) );
        compilers = (RuleSetCompiler[]) ruleSetLoader.getRuleSets().values().toArray( new RuleSetCompiler[]{} );
        byte[] jarBytes = compilers[0].getBinaryDeploymentJar();
        JarInputStream jis = new JarInputStream( new ByteArrayInputStream( jarBytes ) );
        JarEntry entry = null;
        Map entries = new HashMap();
        Map classNames = new HashMap();

        ByteArrayOutputStream bos = null;

        //get all the entries from the jar
        while ( (entry = jis.getNextJarEntry()) != null )
        {
            bos = new ByteArrayOutputStream();
            copy( jis,
                  bos );
            //we have to remove the namespace datestamp, as its random
            String name = entry.getName();
            classNames.put( name.replaceAll( "_[\\d]+/",
                                             "_/" ),
                            name.replace( '/',
                                          '.' ).substring( 0,
                                                           name.length() - 6 ) );
            entries.put( name.replaceAll( "_[\\d]+/",
                                          "_/" ),
                         bos.toByteArray() );
        }
        assertEquals( 5,
                      entries.size() );

        assertTrue( entries.containsKey( "drools/org/simple_/java/Rule1_0.class" ) );
        assertTrue( entries.containsKey( "drools/org/simple_/java/Rule1_0Invoker.class" ) );
        assertTrue( entries.containsKey( "drools/org/simple_/java/Rule1_0Invoker$Consequence_0Invoker.class" ) );
        assertTrue( entries.containsKey( "simple" ) );
        assertTrue( entries.containsKey( "rule-set.conf" ) );
        
        //this makes sure the classes have correct byte[] and can be added to a classLoader
        ClassLoader parentClassLoader = Thread.currentThread().getContextClassLoader();
        if ( parentClassLoader == null ) {
            parentClassLoader = this.getClass().getClassLoader();
        }
        
        TestingClassLoader classLoader = new TestingClassLoader(parentClassLoader);
        assertTrue( classLoader.testByteArray( (String) classNames.get( "drools/org/simple_/java/Rule1_0.class" ),
                            (byte[]) entries.get( "drools/org/simple_/java/Rule1_0.class" ) ) );
        assertTrue( classLoader.testByteArray( (String) classNames.get( "drools/org/simple_/java/Rule1_0Invoker.class" ),
                            (byte[]) entries.get( "drools/org/simple_/java/Rule1_0Invoker.class" ) ) );
        assertTrue( classLoader.testByteArray( (String) classNames.get( "drools/org/simple_/java/Rule1_0Invoker$Consequence_0Invoker.class" ),
                                               (byte[]) entries.get( "drools/org/simple_/java/Rule1_0Invoker$Consequence_0Invoker.class" ) ) );
        
        File file = File.createTempFile( "drools.ddj", null );
        file.deleteOnExit();
        try {
            FileOutputStream fos = new FileOutputStream( file );
            
            fos.write( compilers[0].getBinaryDeploymentJar() );
            
            RuleSet ruleSet = getRuleSet( file.toURL() );
            assertEquals("simple", ruleSet.getName() );
        } 
        catch (Exception e) 
        {
            fail("should not throw exception: " + e.getMessage() );
        }
        finally
        {
            //just incase deleteOnExit doens't always work
            file.delete();
        }
        
        
    }

    public static int copy(InputStream input,
                           OutputStream output) throws IOException
    {
        byte[] buffer = new byte[1024 * 4];
        int count = 0;
        int n = 0;
        while ( -1 != (n = input.read( buffer )) )
        {
            output.write( buffer,
                          0,
                          n );
            count += n;
        }
        return count;
    }

    public static boolean assertNotEquals(byte[] b1,
                                          byte[] b2)
    {
        return !assertEquals( b1,
                              b2 );
    }

    public static boolean assertEquals(byte[] b1,
                                       byte[] b2)
    {
        if ( b1.length != b2.length )
        {
            return false;
        }
        for ( int i = 0; i < b1.length; i++ )
        {
            if ( b1[i] != b2[i] )
            {
                return false;
            }
        }
        return true;
    }

    class TestingClassLoader extends ClassLoader
    {
        public TestingClassLoader(ClassLoader parent) 
        {
            super(parent);
        }
        public boolean testByteArray(String name,
                                     byte[] bytes)
        {
            defineClass( name,
                         bytes,
                         0,
                         bytes.length );
            Class clazz = null;
            try
            {
                clazz = loadClass( name );
            }
            catch ( Exception e )
            {
            }

            return (clazz == null) ? false : true;
        }
    }
    
    private static class ObjectInputStreamWithLoader extends ObjectInputStream
    {
        private final ClassLoader classLoader;

        public ObjectInputStreamWithLoader(InputStream in,
                                           ClassLoader classLoader) throws IOException
        {
            super( in );
            this.classLoader = classLoader;
            enableResolveObject( true );
        }

        protected Class resolveClass(ObjectStreamClass desc) throws IOException,
                                                            ClassNotFoundException
        {
            if ( this.classLoader == null )
            {
                return super.resolveClass( desc );
            }
            else
            {
                String name = desc.getName();
                return this.classLoader.loadClass( name );
            }
        }
    }    
    
    private static RuleSet getRuleSet( URL url ) throws IntegrationException, IOException
    {
        URLClassLoader classLoader = new URLClassLoader( new URL[]{ url },
                                                         RuleBaseLoader.class.getClassLoader() );

        Properties props = new Properties();
        props.load( classLoader.getResourceAsStream( "rule-set.conf" ) );
        
        if (props.get( "name" ) == null)
        {
            throw new IntegrationException( "Rule Set jar does not contain a rule-set.conf file." );
        }
        
        InputStream is = null; 
        ObjectInput in = null;
        RuleSet ruleSet = null;
        try
        {
            is = classLoader.getResourceAsStream( (String) props.get( "name" ) );
        
            in = new ObjectInputStreamWithLoader( is,
                                                  classLoader );
            ruleSet = (RuleSet) in.readObject();
        }
        catch (ClassNotFoundException e)
        {
            throw new IntegrationException( "Rule Set jar is not correctly formed." );
        }
        finally
        {
            if ( is != null )
            {
                is.close();
            }
            if ( in != null )
            {
                in.close();
            }
        }
        return ruleSet;
           
    }     

}
