package org.drools.examples.primefactors;

/*
$Id: PrimeFactors.java,v 1.1 2004-07-22 20:45:19 dbarnett Exp $

Copyright 2004-2004 (C) The Werken Company. All Rights Reserved.

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

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.drools.RuleBase;
import org.drools.RuleBaseBuilder;
import org.drools.WorkingMemory;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.semantics.java.ClassObjectType;

public class PrimeFactors
{
    private static final ClassObjectType factorsType =
        new ClassObjectType(Number.class);

    static final Declaration factorsDecl = new Declaration(factorsType, "f");

    /** Default number of iterations. */
    private static long iterations = 1000;

    /** Set this to true to generate additional debugging output. */
    private static boolean debug = false;

    /** An array of the first 170 prime numbers. */
    private static final long[] primes = {
          2,     3,     5,     7,    11,    13,    17,    19,    23,    29,
         31,    37,    41,    43,    47,    53,    59,    61,    67,    71,
         73,    79,    83,    89,    97,   101,   103,   107,   109,   113,
        127,   131,   137,   139,   149,   151,   157,   163,   167,   173,
        179,   181,   191,   193,   197,   199,   211,   223,   227,   229,
        233,   239,   241,   251,   257,   263,   269,   271,   277,   281,
        283,   293,   307,   311,   313,   317,   331,   337,   347,   349,
        353,   359,   367,   373,   379,   383,   389,   397,   401,   409,
        419,   421,   431,   433,   439,   443,   449,   457,   461,   463,
        467,   479,   487,   491,   499,   503,   509,   521,   523,   541,
        547,   557,   563,   569,   571,   577,   587,   593,   599,   601,
        607,   613,   617,   619,   631,   641,   643,   647,   653,   659,
        661,   673,   677,   683,   691,   701,   709,   719,   727,   733,
        739,   743,   751,   757,   761,   769,   773,   787,   797,   809,
        811,   821,   823,   827,   829,   839,   853,   857,   859,   863,
        877,   881,   883,   887,   907,   911,   919,   929,   937,   941,
        947,   953,   967,   971,   977,   983,   991,   997,  1009,  1013
    };

    public static void main(String[] args) throws Exception
    {
        // Parse input arguments
        if (args.length > 0)
        {
            iterations = Long.parseLong(args[0]);
        }
        if (iterations > primes[primes.length - 1])
        {
            System.out.println(
                "This example only supports factoring numbers less than " +
                primes[primes.length - 1] + 1);
            return;
        }
        System.out.println("Iterations: " + iterations);

        if (args.length > 1)
        {
            debug = Boolean.valueOf(args[1]).booleanValue();
        }
        System.out.println("Debug: " + debug);

        // Dynamically construct rules based on Array of prime numbers
        RuleSet ruleSet = new RuleSet("Find Prime Number");

        if (debug) System.out.println("Creating rules...");
        for (int i = 0, max = primes.length; i < max; i++)
        {
            Rule rule = new Rule("Factor by " + primes[i]);
            rule.addParameterDeclaration(factorsDecl);
            rule.addCondition(new FactorCondition(primes[i]));
            rule.setConsequence(new FactorConsequence(primes[i]));
            ruleSet.addRule(rule);
        }
        if (debug) System.out.println("Creatd " + primes.length + "rules");

        // Build the RuleSet.
        RuleBaseBuilder builder = new RuleBaseBuilder();
        builder.addRuleSet(ruleSet);
        RuleBase ruleBase = builder.build();

        // Example 1
        System.out.println();
        System.out.println("== Example #1 ========================================");
        System.out.println("foreach Number {");
        System.out.println("    new WorkingMemory();");
        System.out.println("    assertObject();");
        System.out.println("    fireAllRules();");
        System.out.println("}");
        System.out.println("======================================================");
        List results = new LinkedList();
        long totalStart = System.currentTimeMillis();
        for (long i = 1, max = iterations; i < max; i++)
        {
            Number factors = new Number(i);

            WorkingMemory workingMemory = ruleBase.newWorkingMemory();

            long start = System.currentTimeMillis();

            workingMemory.assertObject(factors);
            workingMemory.fireAllRules();
            results.addAll(workingMemory.getObjects());

            if (debug) System.out.println(factors + ": " +
                (System.currentTimeMillis() - start) + "ms");
        }
        System.out.println(
            "Total time: " + (System.currentTimeMillis() - totalStart) + "ms");
        validate(results);

        // Example 2
        System.out.println();
        System.out.println("== Example #2 ========================================");
        System.out.println("new WorkingMemory();");
        System.out.println("foreach Number {");
        System.out.println("    assertObject();");
        System.out.println("    fireAllRules();");
        System.out.println("}");
        System.out.println("======================================================");
        WorkingMemory workingMemory = ruleBase.newWorkingMemory();

        totalStart = System.currentTimeMillis();
        for (long i = 1, max = iterations; i < max; i++)
        {
            Number factors = new Number(i);

            long start = System.currentTimeMillis();

            workingMemory.assertObject(factors);
            workingMemory.fireAllRules();

            if (debug) System.out.println(factors + ": " +
                (System.currentTimeMillis() - start) + "ms");
        }
        System.out.println(
            "Total time: " + (System.currentTimeMillis() - totalStart) + "ms");
        validate(workingMemory.getObjects());

        // Example 3
        System.out.println();
        System.out.println("== Example #3 ========================================");
        System.out.println("new WorkingMemory();");
        System.out.println("foreach Number {");
        System.out.println("    assertObject();");
        System.out.println("}");
        System.out.println("fireAllRules()");
        System.out.println("======================================================");
        workingMemory = ruleBase.newWorkingMemory();

        long start = System.currentTimeMillis();

        if (debug) System.out.println("Asserting " + iterations + " numbers...");
        for (long i = 1, max = iterations; i < max; i++)
        {
            workingMemory.assertObject(new Number(i));
        }
        if (debug) System.out.println("Firing all rules...");
        workingMemory.fireAllRules();

        if (debug)
        {
            for (Iterator i = workingMemory.getObjects().iterator(); i.hasNext(); )
            {
                System.out.println(i.next());
            }
        }

        System.out.println(
            "Total time: " + (System.currentTimeMillis() - start) + "ms" );
        validate(workingMemory.getObjects());
    }

    /**
     * Validates that the product of the factors of each Number in the list
     * equals the number itself.
     */
    private static void validate(List objects)
    {
        for (Iterator i = objects.iterator(); i.hasNext(); )
        {
            long product = 1;
            Number number = (Number) i.next();

            if (1 != number.getQuotient())
            {
                throw new RuntimeException("Error: Quotient != 1: " + number);
            }

            for (Iterator j = number.getFactors().iterator(); j.hasNext(); )
            {
                long factor = ((Long) j.next()).longValue();
                product *= factor;
            }

            if (number.getValue() != product)
            {
                throw new RuntimeException(
                    "Error: Product of factors doesn't equal number: " + number);
            }
        }
    }
}

