package org.drools.io;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.drools.FactException;
import org.drools.IntegrationException;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.rule.RuleSet;
import org.drools.smf.RuleSetCompiler;
import org.xml.sax.SAXException;

public class RuleBaseLoaderTest extends TestCase
{

    public void testAddFromRuleSetLoader() throws IntegrationException,
                                          SAXException,
                                          IOException,
                                          FactException
    {
        WorkingMemory workingMemory;
        List list;
        RuleBase ruleBase;
        RuleBaseLoader ruleBaseLoader;
        RuleSetLoader ruleSetLoader = new RuleSetLoader();

        // single URL parameter
        ruleSetLoader.addFromUrl( RuleBaseLoaderTest.class.getResource( "simple.java.drl" ) );

        //Try with addFromRuleSetLoader
        ruleBaseLoader = new RuleBaseLoader();
        ruleBaseLoader.addFromRuleSetLoader( ruleSetLoader );
        ruleBase = ruleBaseLoader.buildRuleBase();
        workingMemory = ruleBase.newWorkingMemory();
        list = new ArrayList();
        workingMemory.setApplicationData( "list",
                                          list );
        workingMemory.assertObject( "help" );
        workingMemory.fireAllRules();
        assertEquals( "help",
                      list.get( 0 ) );

        //Try with addFromRuleSetLoader []        
        ruleBaseLoader = new RuleBaseLoader();
        ruleBaseLoader.addFromRuleSetLoader( new RuleSetLoader[]{ruleSetLoader} );
        ruleBase = ruleBaseLoader.buildRuleBase();
        workingMemory = ruleBase.newWorkingMemory();
        list = new ArrayList();
        workingMemory.setApplicationData( "list",
                                          list );
        workingMemory.assertObject( "help" );
        workingMemory.fireAllRules();
        assertEquals( "help",
                      list.get( 0 ) );
    }

    public void testAddFromRuleSet() throws IntegrationException,
                                    SAXException,
                                    IOException,
                                    FactException
    {
        WorkingMemory workingMemory;
        List list;
        RuleBase ruleBase;
        RuleBaseLoader ruleBaseLoader;
        RuleSetLoader ruleSetLoader = new RuleSetLoader();

        // single URL parameter
        ruleSetLoader.addFromUrl( RuleBaseLoaderTest.class.getResource( "simple.java.drl" ) );

        //Get tehe ruleSet to test with
        RuleSetCompiler[] compilers = (RuleSetCompiler[]) ruleSetLoader.getRuleSets().values().toArray( new RuleSetCompiler[]{} );
        RuleSet ruleSet = compilers[0].getRuleSet();

        //Try with addFromRuleSet        
        ruleBaseLoader = new RuleBaseLoader();
        ruleBaseLoader.addFromRuleSet( ruleSet );
        ruleBase = ruleBaseLoader.buildRuleBase();
        workingMemory = ruleBase.newWorkingMemory();
        list = new ArrayList();
        workingMemory.setApplicationData( "list",
                                          list );
        workingMemory.assertObject( "help" );
        workingMemory.fireAllRules();
        assertEquals( "help",
                      list.get( 0 ) );

        //Try with addFromRuleSet        

        ruleBaseLoader = new RuleBaseLoader();
        ruleBaseLoader.addFromRuleSet( new RuleSet[]{ruleSet} );
        ruleBase = ruleBaseLoader.buildRuleBase();
        workingMemory = ruleBase.newWorkingMemory();
        list = new ArrayList();
        workingMemory.setApplicationData( "list",
                                          list );
        workingMemory.assertObject( "help" );
        workingMemory.fireAllRules();
        assertEquals( "help",
                      list.get( 0 ) );

    }

    public void testAddFromDdjUrl() throws IntegrationException,
                                   SAXException,
                                   IOException,
                                   FactException
    {
        WorkingMemory workingMemory;
        List list;
        RuleBase ruleBase;
        RuleBaseLoader ruleBaseLoader;
        RuleSetLoader ruleSetLoader = new RuleSetLoader();

        // single URL parameter
        ruleSetLoader.addFromUrl( RuleBaseLoaderTest.class.getResource( "simple.java.drl" ) );

        //Try with addFromRuleSetLoader
        ruleBaseLoader = new RuleBaseLoader();
        ruleBaseLoader.addFromRuleSetLoader( ruleSetLoader );
        RuleSetCompiler[] compilers = (RuleSetCompiler[]) ruleSetLoader.getRuleSets().values().toArray( new RuleSetCompiler[]{} );

        File root = null;
        try
        {
            root = createTempDirectory();
            File file = new File( root,
                                  "drools.ddj" );
            FileOutputStream fos = new FileOutputStream( file );
            fos.write( compilers[0].getBinaryDeploymentJar() );
            fos.close();

            // Try with URL to Drools Distribution Jar, ddj
            ruleBaseLoader = new RuleBaseLoader();
            ruleBaseLoader.addFromUrl( file.toURL() );
            ruleBase = ruleBaseLoader.buildRuleBase();
            workingMemory = ruleBase.newWorkingMemory();
            list = new ArrayList();
            workingMemory.setApplicationData( "list",
                                              list );
            workingMemory.assertObject( "help" );
            workingMemory.fireAllRules();
            assertEquals( "help",
                          list.get( 0 ) );

            // Try with URL to Drools Distribution Jar, ddj []            
            ruleBaseLoader = new RuleBaseLoader();
            ruleBaseLoader.addFromUrl( new URL[]{file.toURL()} );
            ruleBase = ruleBaseLoader.buildRuleBase();
            workingMemory = ruleBase.newWorkingMemory();
            list = new ArrayList();
            workingMemory.setApplicationData( "list",
                                              list );
            workingMemory.assertObject( "help" );
            workingMemory.fireAllRules();
            assertEquals( "help",
                          list.get( 0 ) );

            // make a conf file to the URL
            File conf = new File( root,
                                  "rule-base.conf" );
            fos = new FileOutputStream( conf );
            fos.write( file.toURL().toExternalForm().getBytes() );
            fos.close();
        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail( "should not throw exception: " + e.getMessage() );
        }
        finally
        {
            if ( root != null )
            {
                deleteDir( root );
            }
        }
    }

    public void testAddFromConfUrl() throws IntegrationException,
                                    SAXException,
                                    IOException,
                                    FactException
    {
        WorkingMemory workingMemory;
        List list;
        RuleBase ruleBase;
        RuleBaseLoader ruleBaseLoader;
        RuleSetLoader ruleSetLoader = new RuleSetLoader();

        // single URL parameter
        ruleSetLoader.addFromUrl( RuleBaseLoaderTest.class.getResource( "simple.java.drl" ) );

        //Try with addFromRuleSetLoader
        ruleBaseLoader = new RuleBaseLoader();
        ruleBaseLoader.addFromRuleSetLoader( ruleSetLoader );

        RuleSetCompiler[] compilers = (RuleSetCompiler[]) ruleSetLoader.getRuleSets().values().toArray( new RuleSetCompiler[]{} );

        File root = null;
        try
        {
            root = createTempDirectory();
            File file = new File( root,
                                  "drools.ddj" );
            FileOutputStream fos = new FileOutputStream( file );
            fos.write( compilers[0].getBinaryDeploymentJar() );
            fos.close();

            // make a conf file to the URL
            File conf = new File( root,
                                  "rule-base.conf" );
            fos = new FileOutputStream( conf );
            fos.write( file.toURL().toExternalForm().getBytes() );
            fos.close();

            // Try with URL to Distribution conf file            
            ruleBaseLoader = new RuleBaseLoader();
            ruleBaseLoader.addFromUrl( conf.toURL() );
            ruleBase = ruleBaseLoader.buildRuleBase();
            workingMemory = ruleBase.newWorkingMemory();
            list = new ArrayList();
            workingMemory.setApplicationData( "list",
                                              list );
            workingMemory.assertObject( "help" );
            workingMemory.fireAllRules();
            assertEquals( "help",
                          list.get( 0 ) );

            // Try with URL to Distribution conf file            
            ruleBaseLoader = new RuleBaseLoader();
            ruleBaseLoader.addFromUrl( new URL[]{conf.toURL()} );
            ruleBase = ruleBaseLoader.buildRuleBase();
            workingMemory = ruleBase.newWorkingMemory();
            list = new ArrayList();
            workingMemory.setApplicationData( "list",
                                              list );
            workingMemory.assertObject( "help" );
            workingMemory.fireAllRules();
            assertEquals( "help",
                          list.get( 0 ) );

        }
        catch ( Exception e )
        {
            e.printStackTrace();
            fail( "should not throw exception: " + e.getMessage() );
        }
        finally
        {
            if ( root != null )
            {
                deleteDir( root );
            }
        }
    }

    /**
     * Creates and returns a temp directory
     * 
     * @return
     * @throws IOException
     */
    private File createTempDirectory() throws IOException
    {
        final File tempFile = File.createTempFile( "drools",
                                                   null );

        if ( !tempFile.delete() )
        {
            throw new IOException();
        }

        if ( !tempFile.mkdir() )
        {
            throw new IOException();
        }

        return tempFile;
    }

    /**
     * Deletes the current directory and all sub directories, including their contents
     * 
     * @param dir
     * @return
     */
    private static boolean deleteDir(File dir)
    {
        if ( dir == null )
        {
            return false;
        }

        if ( dir.isDirectory() )
        {
            String[] children = dir.list();
            for ( int i = 0; i < children.length; i++ )
            {
                boolean success = deleteDir( new File( dir,
                                                       children[i] ) );
                if ( !success )
                {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }
}
