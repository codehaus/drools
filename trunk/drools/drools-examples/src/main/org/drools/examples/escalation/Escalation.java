package org.drools.examples.escalation;

/*
 * $Id: Escalation.java,v 1.6 2005-05-08 04:22:34 dbarnett Exp $
 *
 * Copyright 2004-2005 (C) The Werken Company. All Rights Reserved.
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

import java.io.IOException;
import java.net.URL;

import org.drools.AssertionException;
import org.drools.DroolsException;
import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.io.RuleBaseLoader;

public class Escalation
{
    public static void main(String[] args)
    {
        if ( args.length != 1 )
        {
            System.out.println( "Usage: " + Escalation.class.getName( )
                                + " [drl file]" );
            return;
        }
        System.out.println( "Using drl: " + args[0] );
        try
        {
            URL url = Escalation.class.getResource( args[0] );
            RuleBase ruleBase = RuleBaseLoader.loadFromUrl( url );

            WorkingMemory workingMemory = ruleBase.newWorkingMemory( );

            try
            {
                TroubleTicket bobTicket = new TroubleTicket( "bob" );
                TroubleTicket daveTicket = new TroubleTicket( "dave" );

                System.err.println( "----------------------------------------" );
                System.err.println( "    PRE" );
                System.err.println( "----------------------------------------" );

                System.err.println( bobTicket );
                System.err.println( daveTicket );

                System.err.println( "----------------------------------------" );

                // Now, simply assert them into the [org.drools.WorkingMemory]
                // and let the logic engine do the rest.

                workingMemory.assertObject( daveTicket );
                workingMemory.assertObject( bobTicket );

                System.err.println( "----------------------------------------" );
                System.err.println( "    POST ASSERT" );
                System.err.println( "----------------------------------------" );

                System.err.println( bobTicket );
                System.err.println( daveTicket );

                System.err.println( "----------------------------------------" );

                try
                {
                    System.err.println( "[[ Sleeping 10 seconds ]]" );
                    Thread.sleep( 10000 );
                    System.err.println( "[[ Done sleeping ]]" );
                }
                catch ( InterruptedException e )
                {
                    e.printStackTrace( );
                }

                System.err.println( "----------------------------------------" );
                System.err.println( "    POST SLEEP" );
                System.err.println( "----------------------------------------" );

                System.err.println( bobTicket );
                System.err.println( daveTicket );

                System.err.println( "----------------------------------------" );

            }
            catch ( AssertionException e )
            {
                e.printStackTrace( );
            }
        }
        catch ( DroolsException e )
        {
            e.printStackTrace( );
        }
        catch ( IOException e )
        {
            e.printStackTrace( );
        }
        catch ( Exception e )
        {
            e.printStackTrace( );
        }
    }
}
