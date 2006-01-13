package org.drools.examples.helloworld;

/*
 * $Id: HelloWorldExample.java,v 1.11 2006-01-13 07:26:55 michaelneale Exp $
 *
 * Copyright 2004 (C) The Werken Company. All Rights Reserved.
 *
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 *
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 *
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 *
 * 3. The name "drools" must not be used to endorse or promote products derived
 * from this Software without prior written permission of The Werken Company.
 * For written permission, please contact bob@werken.com.
 *
 * 4. Products derived from this Software may not be called "drools" nor may
 * "drools" appear in their names without prior written permission of The Werken
 * Company. "drools" is a trademark of The Werken Company.
 *
 * 5. Due credit should be given to The Werken Company. (http://werken.com/)
 *
 * THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE WERKEN COMPANY OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *
 */

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Map;

import org.drools.DroolsException;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.event.DebugWorkingMemoryEventListener;
import org.drools.io.ReteDumper;
import org.drools.io.RuleBaseLoader;
import org.drools.io.RuleSetLoader;
import org.drools.smf.RuleSetCompiler;
import org.drools.smf.RuleSetPackage;
import org.xml.sax.SAXException;

public class HelloWorldExample
{
    public static void main( String[] args ) throws DroolsException,
                                                    SAXException,
                                                    IOException
    {
        
        if ( args.length != 1 )
        {
            args = new String[] { "helloworld.java.drl" };
            System.out.println("You didn't tell me what DRL to run, so I am running " +
                    args[0] + 
                    " \nI can also run the groovy and python equivalents if you tell me to !");
        }
        System.out.println( "Using drl: " + args[0] );

        RuleSetLoader ruleSetLoader = new RuleSetLoader();           
        ruleSetLoader.addFromUrl( HelloWorldExample.class.getResource( args[ 0 ] ) );            
        
        //dumpGeneratedSourceToDisk( ruleSetLoader );
        
        RuleBaseLoader ruleBaseLoader = new RuleBaseLoader();
        ruleBaseLoader.addFromRuleSetLoader(ruleSetLoader);
        RuleBase ruleBase = ruleBaseLoader.buildRuleBase();        
        
        //dumpReteNetwork( ruleBase );
        
        System.out.println( "FIRE RULES(Hello)" );
        System.out.println( "----------" );
        WorkingMemory workingMemory = ruleBase.newWorkingMemory( );
        workingMemory.addEventListener(
            new DebugWorkingMemoryEventListener( ) );
        workingMemory.assertObject( "Hello" );
        workingMemory.fireAllRules( );

        System.out.println( "\n" );

        System.out.println( "FIRE RULES(GoodBye)" );
        System.out.println( "----------" );
        workingMemory = ruleBase.newWorkingMemory( );
        workingMemory.addEventListener(
            new DebugWorkingMemoryEventListener( ) );
        workingMemory.assertObject( "Goodbye" );
        workingMemory.fireAllRules( );
    }

    /** Just in case you want to dump out the contents to disk */
    private static void dumpGeneratedSourceToDisk(RuleSetLoader ruleSetLoader) throws IOException,
                                                                              FileNotFoundException {
        Map map = ruleSetLoader.getRuleSets();
        RuleSetCompiler compiler = (RuleSetCompiler) map.values().iterator().next();
        
        byte[] jar = compiler.getSourceDeploymentJar();
        //will put all the sources in the folling jar
        File file = new File("/helloworld.jar");
        if (file.exists()) file.delete();
        FileOutputStream out = new FileOutputStream(file);
        out.write(jar);
        out.flush();
        out.close();
    }

    /** Use ReteDumper to dump out the network if you need to */
    private static void dumpReteNetwork(RuleBase ruleBase) {
        System.out.println( "DUMP RETE" );
        System.out.println( "---------" );
        ReteDumper dumper = new ReteDumper( ruleBase );
        dumper.dumpRete( System.out );
    }
}
