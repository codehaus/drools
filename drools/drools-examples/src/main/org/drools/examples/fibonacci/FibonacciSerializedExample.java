package org.drools.examples.fibonacci;

/*
 * $Id: FibonacciSerializedExample.java,v 1.5 2004-12-04 14:08:54 simon Exp $
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

import org.drools.FactHandle;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.io.RuleBaseFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;

public class FibonacciSerializedExample
{
    private static WorkingMemory getWorkingMemory(RuleBase ruleBase) throws Exception
    {
        // Serialize to a byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream( );
        ObjectOutput out = new ObjectOutputStream( bos );
        out.writeObject( ruleBase.newWorkingMemory( ) );
        out.close( );

        // Get the bytes of the serialized object
        byte[] bytes = bos.toByteArray( );

        // Deserialize from a byte array
        ObjectInput in = new ObjectInputStream(
                                                new ByteArrayInputStream( bytes ) );
        WorkingMemory workingMemoryOut = ( WorkingMemory ) in.readObject( );
        in.close( );
        return workingMemoryOut;
    }

    private static WorkingMemory serializeWorkingMemory(WorkingMemory workingMemoryIn) throws Exception
    {
        // Serialize to a byte array
        ByteArrayOutputStream bos = new ByteArrayOutputStream( );
        ObjectOutput out = new ObjectOutputStream( bos );
        out.writeObject( workingMemoryIn );
        out.close( );

        // Get the bytes of the serialized object
        byte[] bytes = bos.toByteArray( );

        // Deserialize from a byte array
        ObjectInput in = new ObjectInputStream(
                                                new ByteArrayInputStream( bytes ) );
        WorkingMemory workingMemoryOut = ( WorkingMemory ) in.readObject( );
        in.close( );
        return workingMemoryOut;
    }

    public static void main(String[] args) throws Exception
    {
        if ( args.length != 1 )
        {
            System.out.println( "Usage: "
                                + FibonacciSerializedExample.class.getName( )
                                + " [drl file]" );
            return;
        }
        System.err.println( "Using drl: " + args[0] );

        RuleBase ruleBase = RuleBaseFactory
                                           .createFromUrl( FibonacciSerializedExample.class
                                                                                          .getResource( args[0] ) );

        WorkingMemory workingMemory = getWorkingMemory( ruleBase );

        Fibonacci fibonacci = new Fibonacci( 50 );

        long start = System.currentTimeMillis( );

        FactHandle fibFact = workingMemory.assertObject( fibonacci );

        workingMemory = serializeWorkingMemory( workingMemory );

        workingMemory.fireAllRules( );

        long stop = System.currentTimeMillis( );

        fibonacci = ( Fibonacci ) workingMemory.getObject( fibFact );

        System.err.println( "fibanacci(" + fibonacci.getSequence( ) + ") == "
                            + fibonacci.getValue( ) + " took "
                            + ( stop - start ) + "ms" );
    }
}