package org.drools.examples.primefactors;

/*
$Id: PrimeFactors.java,v 1.3 2004-09-01 20:23:34 dbarnett Exp $

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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Random;
import java.util.zip.GZIPInputStream;

import org.drools.RuleBase;
import org.drools.RuleBaseBuilder;
import org.drools.WorkingMemory;
import org.drools.io.RuleSetReader;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.semantics.java.ClassObjectType;

public class PrimeFactors
{
    /** A list of the first X primes. */
    private static final String PRIMES_FILE = "primes.txt.gz";

    /** . */
    private static final String DRL_FILE = "primes.java.drl";

    /** . */
    private static final ClassObjectType numberType =
        new ClassObjectType(Number.class);

    /** . */
    static final Declaration numberDecl = new Declaration(numberType, "Number");

    /** Default number of numberOfRules. */
    private static int numberOfRules = 100;

    /** Default number of numberOfFacts. */
    private static int numberOfFacts = 1000;

    /** Default random seed. */
    private static long randomSeed = 0;

    /** Set this to true to generate additional output. */
    private static boolean verbose = false;

    /** An array of the first X prime numbers. */
    private static int[] primes;

    /** Used for recording elapsed time measurements. */
    private static long[] timepoint = new long[2];

    /**
     * The main method where all the magic happens.
     */
    public static void main(String[] args) throws Exception
    {
        // Parse input arguments:
        // - Number of Rules (args[0])
        if (args.length > 0)
        {
            numberOfRules = Integer.parseInt(args[0]);
        }
        if (numberOfRules < 1)
        {
            System.out.println(
                "Please enter a number creater than 0 for the number of rules");
            return;
        }
        System.out.println("Number of Rules: " + numberOfRules);

        // - Number of Facts (args[1])
        if (args.length > 1)
        {
            numberOfFacts = Integer.parseInt(args[1]);
        }
        if (numberOfFacts < 0)
        {
            System.out.println(
                "Please enter a positive value for the number of facts.");
            return;
        }
        System.out.println("Number of Facts: " + numberOfFacts);

        // - Random Seed (args[2])
        if (args.length > 2)
        {
            randomSeed = Long.parseLong(args[2]);
        }
        if (-1 == randomSeed)
        {
            randomSeed = System.currentTimeMillis();
        }
        System.out.println("Random Seed: " + randomSeed);

        // - Verbose Output (args[3])
        if (args.length > 3)
        {
            verbose = Boolean.valueOf(args[3]).booleanValue();
        }
        System.out.println("Verbose Output: " + verbose);

        // Dynamically construct rules based on Array of prime numbers
        RuleSet ruleSet = new RuleSet("Find Prime Number");

        verbose("");
        verbose("Reading " + numberOfRules + " primes...");
        stopwatch(0);
        readPrimes();
        verbose("Read " + numberOfRules + " primes" + stopwatch(0));

        verbose("Creating " + numberOfRules + " rules...");
        for (int i = 0; i < numberOfRules; i++)
        {
            Rule rule = new Rule("Factor by " + primes[i]);
            rule.addParameterDeclaration(numberDecl);
            rule.addCondition(new FactorCondition(primes[i]));
            rule.setConsequence(new FactorConsequence(primes[i]));
            ruleSet.addRule(rule);
        }
        verbose("Created " + numberOfRules + " rules" + stopwatch(0));

        // Build the RuleSets.
        verbose("Building RuleBase with " + numberOfRules + " rules...");
        RuleBaseBuilder builder = new RuleBaseBuilder();
        builder.addRuleSet(ruleSet);
        builder.addRuleSet(
            new RuleSetReader().read(PrimeFactors.class.getResource(DRL_FILE)));
        RuleBase ruleBase = builder.build();
        verbose("Built RuleBase with " + numberOfRules + " rules" + stopwatch(0));

        // Determine random set of Facts to assert
        verbose("Generating " + numberOfFacts + " random numbers to assert...");
        Random random = new Random(randomSeed);
        int[] factValues = new int[numberOfFacts];
        for (int i = 0; i < numberOfFacts; i++)
        {
            factValues[i] = random.nextInt(primes[numberOfRules - 1]) + 1;
            if (factValues[i] < 1)
            {
                // Random() should only return numbers greater than or equal to 1
                System.out.println(
                    "Programmer Error: factValues[" + i + "]=" + factValues[i]);
                return;
            }
        }
        primes = null; // Free up unused memory
        verbose("Generated " + numberOfFacts + " random numbers to assert" +
            stopwatch(0));

        // Example 1
        System.out.println();
        System.out.println(
            "== Example #1 ========================================");
        System.out.println("foreach Number {");
        System.out.println("    new WorkingMemory();");
        System.out.println("    assertObject();");
        System.out.println("    fireAllRules();");
        System.out.println("}");
        System.out.println(
            "======================================================");
        List results = new LinkedList();
        stopwatch(0);
        for (int i = 0; i < numberOfFacts; i++)
        {
            stopwatch(1);
            WorkingMemory workingMemory = ruleBase.newWorkingMemory();

            Number fact = new Number(factValues[i]);
            workingMemory.assertObject(fact);
            workingMemory.fireAllRules();
            results.addAll(workingMemory.getObjects());

            verbose(fact + ":" + stopwatch(1));
        }

        System.out.println("Total time:" + stopwatch(0));
        validate(results);

        // Example 2
        System.out.println();
        System.out.println(
            "== Example #2 ========================================");
        System.out.println("new WorkingMemory();");
        System.out.println("foreach Number {");
        System.out.println("    assertObject();");
        System.out.println("    fireAllRules();");
        System.out.println("}");
        System.out.println(
            "======================================================");
        WorkingMemory workingMemory = ruleBase.newWorkingMemory();

        stopwatch(0);
        for (int i = 0; i < numberOfFacts; i++)
        {
            stopwatch(1);

            Number fact = new Number(factValues[i]);
            workingMemory.assertObject(fact);
            workingMemory.fireAllRules();

            verbose(fact + ":" + stopwatch(1));
        }

        System.out.println("Total time:" + stopwatch(0));
        validate(workingMemory.getObjects());

        // Example 3
        System.out.println();
        System.out.println(
            "== Example #3 ========================================");
        System.out.println("new WorkingMemory();");
        System.out.println("foreach Number {");
        System.out.println("    assertObject();");
        System.out.println("}");
        System.out.println("fireAllRules()");
        System.out.println(
            "======================================================");
        workingMemory = ruleBase.newWorkingMemory();

        verbose("Asserting " + numberOfFacts + " facts...");
        stopwatch(0);

        for (int i = 0; i < numberOfFacts; i++)
        {
            workingMemory.assertObject(new Number(factValues[i]));
        }
        verbose("Firing all rules...");
        workingMemory.fireAllRules();

        if (verbose)
        {
            for (Iterator i = workingMemory.getObjects().iterator();
                 i.hasNext();)
            {
                System.out.println(i.next());
            }
        }

        System.out.println("Total time:" + stopwatch(0));
        validate(workingMemory.getObjects());
    }

    /**
     * Reads (optionally GZipped) file of prime numbers, one prime per line.
     */
    private static void readPrimes()
    {
        primes = new int[numberOfRules];
        try {
            BufferedReader br =
                new BufferedReader(new InputStreamReader(new GZIPInputStream(
                    PrimeFactors.class.getResource(PRIMES_FILE).openStream())));

            String line;
            for (int i = 0; i < numberOfRules; i++)
            {
                if (null == (line = br.readLine()))
                {
                    throw new IOException(
                        PRIMES_FILE + " only contained " + i + " primes");
                }
                primes[i] = Integer.parseInt(line);
            }
        } catch (IOException e) {
            throw new RuntimeException(
                "Cannot access " + PRIMES_FILE + " properly: " + e.getMessage());
        }
    }

    /**
     * Validates that the product of the factors of each Number in the list
     * equals the number itself.
     */
    private static void validate(List objects)
    {
        for (Iterator i = objects.iterator(); i.hasNext(); )
        {
            int product = 1;
            Number number = (Number) i.next();

            if (1 != number.getQuotient())
            {
                throw new RuntimeException("Error: Quotient != 1: " + number);
            }

            for (Iterator j = number.getFactors().iterator(); j.hasNext(); )
            {
                int factor = ((Integer) j.next()).intValue();
                product *= factor;
            }

            if (number.getValue() != product)
            {
                throw new RuntimeException(
                    "Error: Product of factors doesn't equal number: " + number);
            }
        }
    }

    private static String stopwatch(int i)
    {
        long now = System.currentTimeMillis();
        String message = " [" + (now - timepoint[i])/1000 + " secs]";
        timepoint[i] = now;
        return message;
    }

    /**
     * Helper method for printing out verbose messages.
     */
    private static void verbose(String message)
    {
        if (verbose)
        {
            System.out.println(message);
        }
    }
}
