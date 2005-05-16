package org.drools.examples.fibonacci;

/*
 * $Id: FibonacciExample.java,v 1.16 2004-12-04 14:59:45 simon Exp $
 *
 * Copyright 2001-2003 (C) The Werken Company. All Rights Reserved.
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

import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.io.RuleBaseLoader;

public class FibonacciExample
{
    public static void main(String[] args)
    {
        if ( args.length != 1 )
        {
            System.out.println( "Usage: " + FibonacciExample.class.getName( )
                                + " [drl file]" );
            return;
        }
        System.out.println( "Using drl: " + args[0] );

        try
        {
            RuleBase ruleBase = RuleBaseLoader.loadFromUrl( FibonacciExample.class.getResource( args[ 0 ] ) );

            WorkingMemory workingMemory;
            Fibonacci fibonacci;
            long start;
            long stop;

            System.err.println( "\nFirst run - code compiled on the fly" );
            workingMemory = ruleBase.newWorkingMemory( );

            fibonacci = new Fibonacci( 50 );
            start = System.currentTimeMillis( );
            workingMemory.assertObject( fibonacci );

            workingMemory.fireAllRules( );
            stop = System.currentTimeMillis( );

            System.err.println( "fibonacci(" + fibonacci.getSequence( )
                                + ") == " + fibonacci.getValue( ) + " took "
                                + ( stop - start ) + "ms" );
            System.err.println( "\nSecond run - code already compiled" );

            workingMemory = ruleBase.newWorkingMemory( );

            fibonacci = new Fibonacci( 50 );
            start = System.currentTimeMillis( );
            workingMemory.assertObject( fibonacci );

            workingMemory.fireAllRules( );
            stop = System.currentTimeMillis( );
            System.err.println( "fibonacci(" + fibonacci.getSequence( )
                                + ") == " + fibonacci.getValue( ) + " took "
                                + ( stop - start ) + "ms" );

        }
        catch ( Exception e )
        {
            e.printStackTrace( );
        }
    }
}