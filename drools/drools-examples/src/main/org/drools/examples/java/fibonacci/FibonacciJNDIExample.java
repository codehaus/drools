package org.drools.examples.java.fibonacci;

/*
$Id: FibonacciJNDIExample.java,v 1.1 2004-07-04 16:48:42 mproctor Exp $

Copyright 2001-2003 (C) The Werken Company. All Rights Reserved.

Redistribution and use of this software and associated documentation
("Software"), with or without modification, are permitted provided
that the following conditions are met:

1. Redistributions of source code must retain copyright
statements and notices.  Redistributions must also contain a
copy of this document.

2. Redistributions in binary form must reproduce the
above copyright notice, this list of conditions and the
following disclaimer in the documentation and/or other
materials provided with the distribution.

3. The name "drools" must not be used to endorse or promote
products derived from this Software without prior written
permission of The Werken Company.  For written permission,
please contact bob@werken.com.

4. Products derived from this Software may not be called "drools"
nor may "drools" appear in their names without prior written
permission of The Werken Company. "drools" is a trademark of
The Werken Company.

5. Due credit should be given to The Werken Company.
(http://werken.com/)

THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS
``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
THE WERKEN COMPANY OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
(INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
OF THE POSSIBILITY OF SUCH DAMAGE.

*/

import org.drools.RuleBase;
import org.drools.reteoo.Dumper;
import org.drools.rule.RuleSet;
import org.drools.rule.Rule;
import org.drools.WorkingMemory;
import org.drools.io.RuleBaseBuilder;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

public class FibonacciJNDIExample
{
    private static void initJNDI(String[] args)
        throws Exception
    {
        System.setProperty("java.naming.factory.initial", args[0]);

        System.setProperty("org.osjava.jndi.shared", "true");

        RuleBase ruleBase = RuleBaseBuilder.buildFromUrl( FibonacciJNDIExample.class.getResource( "fibonacci.drl" ) );

        Context context =  new InitialContext();
        context.bind("fibonacci", ruleBase);
    }

    public static void main(String[] args)
        throws Exception
    {
        initJNDI(args);

        Context context =  new InitialContext();

        RuleBase ruleBase = (RuleBase) context.lookup("fibonacci");

        WorkingMemory workingMemory = ruleBase.newWorkingMemory();

        Fibonacci fibonacci = new Fibonacci( 50 );

        long start = System.currentTimeMillis();

        workingMemory.assertObject( fibonacci );

        workingMemory.fireAllRules();

        long stop = System.currentTimeMillis();

        System.out.println( "fibanacci(" + fibonacci.getSequence() + ") == " + fibonacci.getValue() + " took " + (stop-start) + "ms" );
    }


}