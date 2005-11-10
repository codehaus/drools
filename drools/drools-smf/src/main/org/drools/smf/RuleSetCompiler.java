package org.drools.smf;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.drools.IntegrationException;
import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.spi.Condition;
import org.drools.spi.Functions;

public class RuleSetCompiler
{
    private static final RuleSetCompiler INSTANCE = new RuleSetCompiler();
    
    private RuleSet ruleSet;
    
    private String  ruleSetName;    

    private String  packageName;

    private String  knowledgeHelper;

    private File          src;
    private File          dst;
    private File          temp;

    private File          srcJar;
    private File          binJar;
    
    public static RuleSetCompiler getInstance()
    {
        return RuleSetCompiler.INSTANCE;
    }
    
    private RuleSetCompiler()
    {
    }
    
    /**
     * Compiles a RuleSet using the give packageName for the generated classes.
     * This method uses the OS specific temp directory and creates two sub directories
     * "src" and "dst".
     * 
     * @param ruleSet
     *            The RuleSet to compile
     * @param packageName
     *            The package to use for the generated classes
     * @param knowledgeHelper
     *            This String allows users to choose the variable name for the KnowledgeHelper
     * @return
     * @throws IntegrationException
     * @throws IOException
     */
    public synchronized RuleSetPackage compile(RuleSet ruleSet,
                                               String packageName,
                                               String knowledgeHelper)  throws IntegrationException, IOException
    {
        File temp = createTempDirectory();
        return compile( ruleSet,
                        packageName,
                        knowledgeHelper,
                        temp,
                        new File( temp,
                                  "src" ),
                        new File( temp,
                                  "dst" ) );     
        
    }
    /**
     * Compiles a RuleSet using the give packageName for the generated classes.
     * This method uses the specific temp directory and creates two sub directories
     * "src" and "dst".
     * 
     * @param ruleSet
     *            The RuleSet to compile
     * @param packageName
     *            The package to use for the generated classes
     * @param knowledgeHelper
     *            This String allows users to choose the variable name for the KnowledgeHelper
     * @param temp
     * @return
     * @throws IntegrationException
     * @throws IOException
     */
    public synchronized RuleSetPackage compile(RuleSet ruleSet,
                                               String packageName,
                                               String knowledgeHelper,
                                               File temp)  throws IntegrationException, IOException
    {

        return compile( ruleSet,
                        packageName,
                        knowledgeHelper,
                        temp,
                        new File( temp,
                                  "src" ),
                        new File( temp,
                                  "dst" ) );        
    }
    
    /**
     * Compiles a RuleSet using the give packageName for the generated classes.
     * If temp is different to src and dst teh resulting jars and serialised objects will be there,
     * While source code and binaries will still go to src and st.
     * 
     * @param ruleSet
     *            The RuleSet to compile
     * @param packageName
     *            The package to use for the generated classes
     * @param knowledgeHelper
     *            This String allows users to choose the variable name for the KnowledgeHelper
     * @param temp
     *            The temp directory to generate classes into
     * @param src
     *            The src directory
     * @param dst
     *            The dst directory
     * @return
     * @throws IntegrationException
     * @throws IOException
     */
    public synchronized RuleSetPackage compile(RuleSet ruleSet,
                                               String packageName,
                                               String knowledgeHelper,
                                               File temp,
                                               File src,
                                               File dst)  throws IntegrationException, IOException
    {
        this.temp = temp;
        this.src = src;
        this.dst = dst;

        srcJar = null;
        binJar = null;        

        // make sure all directories are empty before we generate source and compile
        try
        {
            initDirectories( new File[] { this.temp, this.src, this.dst } );
        }
        catch ( IOException e )
        {
            throw new IOException("Unable to initialise temp, src and dst compilation directories: " + e.getMessage() );
        }
               
        
        this.ruleSet = ruleSet;
        this.ruleSetName = this.ruleSet.getName().replaceAll( "(^[0-9]|[^\\w$])",
                                                              "_" );
        this.packageName = packageName;
        this.knowledgeHelper = knowledgeHelper;
                      
        compile();
        
        //srcJar may be null so check before we do toURL
        URL srcJarUrl = ( this.srcJar != null ) ? this.srcJar.toURL() : null ;
        
        return new RuleSetPackage( ruleSet, this.binJar.toURL(), srcJarUrl  );
    }
    

    private void compile() throws IOException, IntegrationException
    {
 
        Map functionMap = this.ruleSet.getFunctions();
        Iterator it = functionMap.values().iterator();
        SemanticFunctions functions = null;
        SemanticFunctionsCompiler compiler = null;
        String functionClassName = null;
        String name = null;
        String semanticPackageName = null;

        Map parents = new HashMap();
        Map files = new HashMap();
        Object object = null;
        while ( it.hasNext() )
        {
            object = it.next();
            if ( ! (object instanceof SemanticFunctions) )
            {
                continue;
            }
            
            functions = (SemanticFunctions) object;
            name = functions.getName();

            // Make a copy of the imports
            // Cannot use the original as it will be updated by the compiler
            Set imports = new HashSet();
            imports.addAll( this.ruleSet.getImporter().getImports() );

            compiler = functions.getSemanticFunctionsCompiler();

            semanticPackageName = packageName + "." + compiler.getSemanticType();
            functionClassName = generateUniqueLegalName( semanticPackageName,
                                                         src,
                                                         name.toUpperCase().charAt( 0 ) + name.substring( 1 ),
                                                         "." + compiler.getSemanticType() );

            compiler.generate( (Functions) functions,
                               imports,
                               semanticPackageName,
                               functionClassName,
                               null,
                               this.src,
                               this.dst,
                               files );
            
            parents.put( compiler.getSemanticType(), semanticPackageName + "." + functionClassName );

        }
        
        it = files.keySet().iterator();
        object = null;
        List list = null;
        while ( it.hasNext() )
        {
            object = it.next();
            if ( object instanceof SemanticFunctionsCompiler ) {
                compiler = (SemanticFunctionsCompiler) object;
                list = ( List ) files.get( compiler ); 
                compiler.compile( ( String[] ) list.toArray( new String[ list.size() ] ),
                                  this.src,
                                  this.dst );
            }
        }        

        Rule[] rules = this.ruleSet.getRules();
        String className = null;

        // use a HashMap to map the rules to their new class names, used for wiring
        Map ruleNameMap = new HashMap();
        for ( int i = 0; i < rules.length; i++ )
        {
            compileRule( rules[i],
                         this.packageName,
                         parents,
                         ruleNameMap,
                         this.knowledgeHelper,
                         src,
                         dst );

        }

        File conf = new File( this.temp,
                              "rule-set.conf" );

        Properties prop = new Properties();
        prop.setProperty( "name",
                          this.ruleSetName );
        prop.store( new FileOutputStream( conf ),
                    null );

        URLClassLoader classLoader = new URLClassLoader( new URL[]{this.dst.toURL()},
                                                         getClass().getClassLoader() );

        setInvokers( this.ruleSet,
                     this.packageName,
                     ruleNameMap,
                     classLoader );

        createBinJar();
        createSrcJar();
    }

    private static void compileRule(Rule rule,
                                    String packageName,
                                    Map parents,
                                    Map ruleMap,
                                    String knowledgeHelper,
                                    File src,
                                    File dst) throws IOException
    {
        RuleCompiler compiler = RuleCompiler.getInstance();
        compiler.compile( rule,
                          packageName,
                          parents,
                          ruleMap,
                          knowledgeHelper,
                          src,
                          dst );
    }

    private void setInvokers(RuleSet ruleSet,
                             String packageName,
                             Map ruleMap,
                             ClassLoader classLoader) throws IntegrationException
    {
        Rule[] rules = ruleSet.getRules();
        Rule rule = null;
        SemanticInvokeable component = null;
        String name = null;
        String semanticPackageName = null;
        try
        {
            for ( int i = 0; i < rules.length; i++ )
            {
                rule = rules[i];
                Condition[] conditions = (Condition[]) rule.getConditions().toArray( new Condition[rule.getConditions().size()] );
                for ( int j = 0; j < conditions.length; j++ )
                {
                    //only wire up this condition if it implements SemanticInvokeble
                    if ( ! ( conditions[j] instanceof SemanticInvokeable ) )
                    {
                        continue;
                    }
                    component = (SemanticInvokeable) conditions[j];
                    name = component.getName();
                    semanticPackageName = packageName + "." + component.getSemanticType();
                    component.setInvoker( (Invoker) classLoader.loadClass( semanticPackageName + "." + ruleMap.get( rule ) + "Invoker$" + name.toUpperCase().charAt( 0 ) + name.substring( 1 ) + "Invoker" ).newInstance() );
    
                }
    
                //only wire up this consequenceif it implements SemanticInvokeble
                if ( rule.getConsequence() instanceof SemanticInvokeable  )
                {
                    component = (SemanticInvokeable) rule.getConsequence();
                    name = component.getName();
                    semanticPackageName = packageName + "." + component.getSemanticType();
                    component.setInvoker( (Invoker) classLoader.loadClass( semanticPackageName + "." + ruleMap.get( rule ) + "Invoker$" + name.toUpperCase().charAt( 0 ) + name.substring( 1 ) + "Invoker" ).newInstance() );
                }
            }
        }
        catch ( InstantiationException e )
        {
            throw new IntegrationException( "Unable to bind RuleSet '" + ruleSet.getName() + "' component to Class Method: " + e.getMessage(), e );
        }
        catch ( IllegalAccessException e )
        {
            throw new IntegrationException( "Unable to bind RuleSet '" + ruleSet.getName() + "'  component to Class Method: " + e.getMessage(), e );
        }
        catch ( ClassNotFoundException e )
        {
            throw new IntegrationException( "Unable to bind RuleSet '" + ruleSet.getName() + "'  component to Class Method: " + e.getMessage(), e );
        }            
    }

    private void createBinJar() throws FileNotFoundException,
                               IOException
    {
        String jarName = this.ruleSetName + ".jar";
        this.binJar = new File( this.temp,
                                jarName );
        Jarer jarer = new Jarer( binJar );

        jarer.addDirectory( this.dst );
        jarer.addObject( this.ruleSetName,
                         this.ruleSet );

        File conf = new File( this.temp,
                              "rule-set.conf" );

        jarer.addFile( conf,
                       "rule-set.conf" );

        jarer.close();

    }

    /**
     * Only create a src jar is the src directory has entries
     * 
     * @throws FileNotFoundException
     * @throws IOException
     */
    private void createSrcJar() throws FileNotFoundException,
                               IOException
    {
        // Only create a src jar is the src directory has entries 
        if ( this.src.list().length != 0 )
        {
            String jarName = this.ruleSet.getName().replaceAll( "(^[0-9]|[^\\w$])",
                                                                "_" ) + "-src.jar";
            File jar = new File( this.temp,
                                 jarName );
            Jarer jarer = new Jarer( jar );
            jarer.addDirectory( this.src );
            jarer.close();
        }
    }


    /**
     * Takes a given name and makes sure that its legal and doesn't already exist. If the file exists it increases counter appender untill it is unique.
     * 
     * @param packageName
     * @param name
     * @param ext
     * @return
     */
    private String generateUniqueLegalName(String packageName,
                                           File directory,
                                           String name,
                                           String ext)
    {
        // replaces the first char if its a number and after that all non
        // alphanumeric or $ chars with _
        String newName = name.replaceAll( "(^[0-9]|[^\\w$])",
                                          "_" );

        // make sure the class name does not exist, if it does increase the counter
        int counter = -1;
        boolean exists = true;
        while ( exists )
        {
            counter++;
            String fileName = packageName.replaceAll( "\\.",
                                                      "/" ) + newName + "_" + counter + ext;
            File file = new File( directory,
                                  fileName );
            exists = file.exists();
        }
        // we have duplicate file names so append counter
        if ( counter >= 0 )
        {
            newName = newName + "_" + counter;
        }

        return newName;
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
    
    private void initDirectories( File[] files ) throws IOException
    {
        File file = null;
        for( int i = 1; i < files.length; i++ )
        {
            file = files[i];
            deleteDir( file );
            if ( !file.exists() && !file.mkdirs() )
            {
                throw new IOException( "could not create directory : " + file );
            }
        }
    }

    /**
     * Deletes the current directory and all sub directories, including their contents
     * 
     * @param dir
     * @return
     */
    private static boolean deleteDir(File dir)
    {
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
