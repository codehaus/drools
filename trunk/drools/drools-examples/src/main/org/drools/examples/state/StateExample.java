package org.drools.examples.state;

/*
 * $Id: StateExample.java,v 1.5 2004-12-04 14:59:45 simon Exp $
 *
 * Copyright 2002 (C) The Werken Company. All Rights Reserved.
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
 */

import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.io.RuleBaseLoader;

import java.net.URL;

public class StateExample
{
    public static void main( String[] args ) throws Exception
    {
        if ( args.length != 1 )
        {
            System.out.println(
                "Usage: " + StateExample.class.getName( ) + " [drl file]" );
            return;
        }
        System.out.println( "Using drl: " + args[0] );

        URL url = StateExample.class.getResource( args[0] );
        RuleBase ruleBase = RuleBaseLoader.loadFromUrl( url );

        WorkingMemory workingMemory = ruleBase.newWorkingMemory( );

        State a = new State( "A" );
        State b = new State( "B" );
        State c = new State( "C" );
        State d = new State( "D" );

        // By setting dynamic to TRUE, Drools will use JavaBean
        // PropertyChangeListeners so you don't have to call modifyObject().
        boolean dynamic = true;

        workingMemory.assertObject( a, dynamic );
        workingMemory.assertObject( b, dynamic );
        workingMemory.assertObject( c, dynamic );
        workingMemory.assertObject( d, dynamic );

        workingMemory.fireAllRules( );
    }
}