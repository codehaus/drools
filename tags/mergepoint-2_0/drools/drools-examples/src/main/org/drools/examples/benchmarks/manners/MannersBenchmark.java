package org.drools.examples.benchmarks.manners;

/*
 * $Id: MannersBenchmark.java,v 1.2 2004-12-16 18:48:14 dbarnett Exp $
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
 * Company. "drools" is a registered trademark of The Werken Company.
 *
 * 5. Due credit should be given to The Werken Company.
 * (http://drools.werken.com/).
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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import org.drools.DroolsException;
import org.drools.FactException;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.conflict.ComplexityConflictResolver;
import org.drools.examples.benchmarks.manners.model.Context;
import org.drools.examples.benchmarks.manners.model.Count;
import org.drools.examples.benchmarks.manners.model.Guest;
import org.drools.examples.benchmarks.manners.model.LastSeat;
import org.drools.io.RuleBaseLoader;
import org.drools.reteoo.Dumper;
import org.xml.sax.SAXException;

/**
 * A Drools implementation of the Miss Manners benchmark,
 * a part of the OPS5 Benchmark Suite available at:<br/>
 * http://www.cs.utexas.edu/ftp/pub/ops5-benchmark-suite/<br/>
 * ftp://ftp.cs.utexas.edu/pub/ops5-benchmark-suite/ 
 */
public class MannersBenchmark
{
    /**
     * The DRL file contains the Drools implementation
     * of the Miss Manners rules.
     */
    private static final String DRL_FILE = "manners.drl";
    
    /**
     * A GraphViz DOT file created to show the resulting Rete Graph
     * generated by the DRL rules.
     */
    private static final String DOT_FILE = "manners.dot";
    
    /**
     * List of org.drools.examples.benchmarks.manners.model.* objects
     * read from the provided *.dat data file and to be asserted into the
     * WorkingMemory.
     */
    private List inputObjects;
    
    /**
     * The <code>WorkingMemory</code> generated by the rules in the DRL file.
     */
    private WorkingMemory workingMemory;
    
    /**
     * Creates a new <code>MannersBenchmark</code> object and executes its
     * <code>run</code> method.
     * 
     * @param args if an argument is provided,
     *             it is used as the name of the *.dat data file to use.
     */
    public static void main( String[] args )
        throws DroolsException,
               IOException,
               SAXException
    {
        String datFile = "manners_16.dat";
        if ( args.length > 0 )
        {
            datFile = args[0];
        }
        
        MannersBenchmark benchmark = new MannersBenchmark( datFile );

        long start = System.currentTimeMillis( );
        benchmark.run( );
        System.out.println( "Elapsed time: "
            + ( System.currentTimeMillis( ) - start ) + " ms" );
    }

    /**
     * Read the DRL and DAT files, output the Rete to a DOT file,
     * and initialize a <code>WorkingMemory</code>.
     * 
     * @param datFile the input data file
     */
    public MannersBenchmark( String datFile )
        throws DroolsException,
               IOException,
               SAXException
    {
        System.out.println( "Loading DRL: " + DRL_FILE + "..." );
        RuleBase ruleBase = RuleBaseLoader.loadFromUrl(
            MannersBenchmark.class.getResource( DRL_FILE ),
            ComplexityConflictResolver.getInstance( ) );
        
        File dotFile = new File( DOT_FILE );
        System.out.println( "Creating DOT: " + dotFile.getCanonicalPath() + "..." );
        new Dumper( ruleBase ).dumpReteToDot( new PrintStream(
            new FileOutputStream( dotFile ) ) );

        this.workingMemory = ruleBase.newWorkingMemory( );
        
        this.workingMemory.addEventListener( new MannersWMEL( ) );
        
        System.out.println( "Reading DAT: " + datFile + "..." );
        this.inputObjects = getInputObjects(
            this.getClass().getResourceAsStream( datFile ) );
    }

    /**
     * Assert the facts and fire the rules.
     */
    public void run( ) throws FactException
    {
        System.out.println( "Asserting initial objects..." );
        for ( Iterator i = this.inputObjects.iterator( ); i.hasNext( ); )
        {
            this.workingMemory.assertObject( i.next( ) );
        }
        
        System.out.println( "Firing all rules..." );
        this.workingMemory.fireAllRules( );
   }

    /**
     * Convert the facts from the <code>InputStream</code> to a list of
     * objects.
     */
    private static List getInputObjects( InputStream inputStream )
        throws IOException
    {
        List list = new ArrayList( );

        BufferedReader br = new BufferedReader(
            new InputStreamReader( inputStream ) );

        String line;
        while ( ( line = br.readLine( ) ) != null )
        {
            if ( line.trim( ).length( ) == 0 || line.trim( ).startsWith( ";" ) )
            {
                continue;
            }
            StringTokenizer st = new StringTokenizer( line, "() " );
            String make = st.nextToken( );

            if ( !"make".equals( make ) )
            {
                throw new IOException( "expected 'make' in: " + line );
            }
            
            String type = st.nextToken( );
            if ( "guest".equals( type ) )
            {
                if ( !"^name".equals( st.nextToken( ) ) )
                {
                    throw new IOException( "expected '^name' in: " + line );
                }
                String name = st.nextToken( );
                
                if ( !"^sex".equals( st.nextToken( ) ) )
                {
                    throw new IOException( "expected '^sex' in: " + line );
                }
                char sex = st.nextToken( ).charAt( 0 );
                
                if ( !"^hobby".equals( st.nextToken( ) ) )
                {
                    throw new IOException( "expected '^hobby' in: " + line );
                }
                String hobby = st.nextToken( );

                list.add( new Guest( name, sex, hobby ) );
            }

            if ( "last_seat".equals( type ) )
            {
                list.add( new LastSeat( Integer.parseInt( st.nextToken( ) ) ) );
            }

            if ( "count".equals( type ) )
            {
                list.add( new Count( Integer.parseInt( st.nextToken( ) ) ) );
            }

            if ( "context".equals( type ) )
            {
                list.add( new Context( st.nextToken( ) ) );
            }
        }
        inputStream.close( );

        return list;
    }
}
