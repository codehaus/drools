package org.drools.examples;

/*
 * $Id: FibonacciNativeTest.java,v 1.3 2004-11-07 18:38:53 mproctor Exp $
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

import org.drools.FactException;
import org.drools.RuleBase;
import org.drools.RuleBaseBuilder;
import org.drools.WorkingMemory;
import org.drools.reteoo.Dumper;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.spi.Condition;
import org.drools.spi.Consequence;
import org.drools.spi.ConsequenceException;
import org.drools.spi.Tuple;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;

import junit.framework.TestCase;

import java.io.Serializable;
/**
 * This Fibonacci example demonstrates how to build a native RuleSet without
 * using one of the many Semantic Modules.
 */

public class FibonacciNativeTest extends TestCase implements Serializable
{
    public void testFibonacci() throws Exception
    {
        // <rule-set name="fibonacci" ...>
        RuleSet ruleSet = new RuleSet( "fibonacci" );

        // <rule name="Bootstrap 1" salience="20">
        Rule bootstrap1Rule = new Rule( "Bootstrap 1" );
        bootstrap1Rule.setSalience( 20 );

        // Reuse the Java semantics ObjectType
        // so Drools can identify the Fibonacci class
        ClassObjectType fibonacciType = new ClassObjectType( Fibonacci.class );

        // Build the declaration and specify it as a parameter of the Bootstrap1
        // Rule
        // <parameter identifier="f">
        //   <class>org.drools.examples.fibonacci.Fibonacci</class>
        // </parameter>
        final Declaration fDeclaration = new Declaration( fibonacciType, "f" );
        bootstrap1Rule.addParameterDeclaration( fDeclaration );

        // Build and Add the Condition to the Bootstrap1 Rule
        // <java:condition>f.getSequence() == 1</java:condition>
        Condition conditionBootstrap1A = new Condition( )
        {
            public boolean isAllowed(Tuple tuple)
            {
                Fibonacci f = ( Fibonacci ) tuple.get( fDeclaration );
                return ( f.getSequence( ) == 1 );
            }

            public Declaration[] getRequiredTupleMembers()
            {
                return new Declaration[]{fDeclaration};
            }

            public String toString()
            {
                return "f.getSequence() == 1";
            }
        };
        bootstrap1Rule.addCondition( conditionBootstrap1A );

        // <java:condition>f.getValue() == -1</java:condition>
        Condition conditionBootstrap1B = new Condition( )
        {
            public boolean isAllowed(Tuple tuple)
            {
                Fibonacci f = ( Fibonacci ) tuple.get( fDeclaration );
                return ( f.getValue( ) == -1 );
            }

            public Declaration[] getRequiredTupleMembers()
            {
                return new Declaration[]{fDeclaration};
            }

            public String toString()
            {
                return "f.getValue() == -1";
            }
        };
        bootstrap1Rule.addCondition( conditionBootstrap1B );

        // Build and Add the Consequence to the Bootstrap1 Rule
        // <java:consequence>
        //   f.setValue( 1 );
        //   System.err.println( f.getSequence() + " == " + f.getValue() );
        //   drools.modifyObject( f );
        // </java:consequence>
        Consequence bootstrapConsequence = new Consequence( )
        {
            public void invoke(Tuple tuple, WorkingMemory workingMemory) throws ConsequenceException
            {
                Fibonacci f = ( Fibonacci ) tuple.get( fDeclaration );
                f.setValue( 1 );

                try
                {
                    workingMemory
                                 .modifyObject(
                                                tuple
                                                     .getFactHandleForObject( f ),
                                                f );
                }
                catch ( FactException e )
                {
                    throw new ConsequenceException( e );
                }

                FibTotal total = (FibTotal) workingMemory.getApplicationData("fibtotal");
                total.setTotal(total.getTotal() + 1);
            }
        };
        bootstrap1Rule.setConsequence( bootstrapConsequence );
        ruleSet.addRule( bootstrap1Rule );

        // <rule name="Bootstrap 2">
        Rule bootstrap2Rule = new Rule( "Bootstrap 2" );

        // Specify the declaration as a parameter of the Bootstrap2 Rule
        // <parameter identifier="f">
        //   <class>org.drools.examples.fibonacci.Fibonacci</class>
        // </parameter>
        bootstrap2Rule.addParameterDeclaration( fDeclaration );

        // Build and Add the Conditions to the Bootstrap1 Rule
        // <java:condition>f.getSequence() == 2</java:condition>
        Condition conditionBootstrap2A = new Condition( )
        {
            public boolean isAllowed(Tuple tuple)
            {
                Fibonacci f = ( Fibonacci ) tuple.get( fDeclaration );
                return ( f.getSequence( ) == 2 );
            }

            public Declaration[] getRequiredTupleMembers()
            {
                return new Declaration[]{fDeclaration};
            }

            public String toString()
            {
                return "f.getSequence() == 2";
            }
        };
        bootstrap2Rule.addCondition( conditionBootstrap2A );

        // <java:condition>f.getValue() == -1</java:condition>
        Condition conditionBootstrap2B = new Condition( )
        {
            public boolean isAllowed(Tuple tuple)
            {
                Fibonacci f = ( Fibonacci ) tuple.get( fDeclaration );
                return ( f.getValue( ) == -1 );
            }

            public Declaration[] getRequiredTupleMembers()
            {
                return new Declaration[]{fDeclaration};
            }

            public String toString()
            {
                return "f.getValue() == -1";
            }
        };
        bootstrap2Rule.addCondition( conditionBootstrap2B );

        // Add the Consequence to the Bootstrap2 Rule.
        // It's identical to the Bootstrap1 Consequence.
        bootstrap2Rule.setConsequence( bootstrapConsequence );
        ruleSet.addRule( bootstrap2Rule );

        // <rule name="Recurse" salience="10">
        Rule recurseRule = new Rule( "Recurse" );
        recurseRule.setSalience( 10 );

        // <parameter identifier="f">
        //   <class>org.drools.examples.fibonacci.Fibonacci</class>
        // </parameter>
        recurseRule.addParameterDeclaration( fDeclaration );

        // <java:condition>f.getValue() == -1</java:condition>
        Condition conditionRecurse = new Condition( )
        {
            public boolean isAllowed(Tuple tuple)
            {
                Fibonacci f = ( Fibonacci ) tuple.get( fDeclaration );
                return ( f.getValue( ) == -1 );
            }

            public Declaration[] getRequiredTupleMembers()
            {
                return new Declaration[]{fDeclaration};
            }

            public String toString()
            {
                return "f.getValue() == -1";
            }
        };
        recurseRule.addCondition( conditionRecurse );

        // <java:consequence>
        //   System.err.println( "recurse for " + f.getSequence() );
        //   drools.assertObject( new Fibonacci( f.getSequence() - 1 ) );
        // </java:consequence>
        Consequence recurseConsequence = new Consequence( )
        {
            public void invoke(Tuple tuple, WorkingMemory workingMemory) throws ConsequenceException
            {
                Fibonacci f = ( Fibonacci ) tuple.get( fDeclaration );
                try
                {
                    workingMemory
                                 .assertObject( new Fibonacci(
                                                               f.getSequence( ) - 1 ) );
                }
                catch ( FactException e )
                {
                    throw new ConsequenceException( e );
                }
            }
        };
        recurseRule.setConsequence( recurseConsequence );
        ruleSet.addRule( recurseRule );

        // <rule name="Calculate">
        Rule calculateRule = new Rule( "Calculate" );

        // <parameter identifier="f1">
        //   <class>org.drools.examples.fibonacci.Fibonacci</class>
        // </parameter>
        final Declaration fDeclaration1 = new Declaration( fibonacciType, "f1" );
        calculateRule.addParameterDeclaration( fDeclaration1 );

        // <parameter identifier="f2">
        //   <class>org.drools.examples.fibonacci.Fibonacci</class>
        // </parameter>
        final Declaration fDeclaration2 = new Declaration( fibonacciType, "f2" );
        calculateRule.addParameterDeclaration( fDeclaration2 );

        // <parameter identifier="f3">
        //   <class>org.drools.examples.fibonacci.Fibonacci</class>
        // </parameter>
        final Declaration fDeclaration3 = new Declaration( fibonacciType, "f3" );
        calculateRule.addParameterDeclaration( fDeclaration3 );

        // <java:condition>f2.getSequence() ==
        // (f1.getSequence()+1)</java:condition>
        Condition conditionCalculateA = new Condition( )
        {
            public boolean isAllowed(Tuple tuple)
            {
                Fibonacci f1 = ( Fibonacci ) tuple.get( fDeclaration1 );
                Fibonacci f2 = ( Fibonacci ) tuple.get( fDeclaration2 );
                return ( f2.getSequence( ) == ( f1.getSequence( ) + 1 ) );
            }

            public Declaration[] getRequiredTupleMembers()
            {
                return new Declaration[]{fDeclaration1, fDeclaration2};
            }

            public String toString()
            {
                return "f2.getSequence() == (f1.getSequence()+1)";
            }
        };
        calculateRule.addCondition( conditionCalculateA );

        // <java:condition>f3.getSequence() ==
        // (f2.getSequence()+1)</java:condition>
        Condition conditionCalculateB = new Condition( )
        {
            public boolean isAllowed(Tuple tuple)
            {
                Fibonacci f2 = ( Fibonacci ) tuple.get( fDeclaration2 );
                Fibonacci f3 = ( Fibonacci ) tuple.get( fDeclaration3 );
                return ( f3.getSequence( ) == ( f2.getSequence( ) + 1 ) );
            }

            public Declaration[] getRequiredTupleMembers()
            {
                return new Declaration[]{fDeclaration2, fDeclaration3};
            }

            public String toString()
            {
                return "f3.getSequence() == (f2.getSequence()+1)";
            }
        };
        calculateRule.addCondition( conditionCalculateB );

        // <java:condition>f1.getValue() != -1</java:condition>
        Condition conditionCalculateC = new Condition( )
        {
            public boolean isAllowed(Tuple tuple)
            {
                Fibonacci f1 = ( Fibonacci ) tuple.get( fDeclaration1 );
                return ( f1.getValue( ) != -1 );
            }

            public Declaration[] getRequiredTupleMembers()
            {
                return new Declaration[]{fDeclaration1};
            }

            public String toString()
            {
                return "f1.getValue() != -1";
            }
        };
        calculateRule.addCondition( conditionCalculateC );

        // <java:condition>f2.getValue() != -1</java:condition>
        Condition conditionCalculateD = new Condition( )
        {
            public boolean isAllowed(Tuple tuple)
            {
                Fibonacci f2 = ( Fibonacci ) tuple.get( fDeclaration2 );
                return ( f2.getValue( ) != -1 );
            }

            public Declaration[] getRequiredTupleMembers()
            {
                return new Declaration[]{fDeclaration2};
            }

            public String toString()
            {
                return "f2.getValue() != -1";
            }
        };
        calculateRule.addCondition( conditionCalculateD );

        // <java:condition>f3.getValue() == -1</java:condition>
        Condition conditionCalculateE = new Condition( )
        {
            public boolean isAllowed(Tuple tuple)
            {
                Fibonacci f3 = ( Fibonacci ) tuple.get( fDeclaration3 );
                return ( f3.getValue( ) == -1 );
            }

            public Declaration[] getRequiredTupleMembers()
            {
                return new Declaration[]{fDeclaration3};
            }

            public String toString()
            {
                return "f3.getValue() == -1";
            }
        };
        calculateRule.addCondition( conditionCalculateE );

        // <java:consequence>
        //   f3.setValue( f1.getValue() + f2.getValue() );
        //   System.err.println( f3.getSequence() + " == " + f3.getValue() );
        //   drools.modifyObject( f3 );
        //   drools.retractObject( f1 );
        // </java:consequence>
        Consequence calculateConsequence = new Consequence( )
        {
            public void invoke(Tuple tuple, WorkingMemory workingMemory) throws ConsequenceException
            {
                Fibonacci f1 = ( Fibonacci ) tuple.get( fDeclaration1 );
                Fibonacci f2 = ( Fibonacci ) tuple.get( fDeclaration2 );
                Fibonacci f3 = ( Fibonacci ) tuple.get( fDeclaration3 );

                f3.setValue( f1.getValue( ) + f2.getValue( ) );
                try
                {
                    workingMemory
                                 .modifyObject(
                                                tuple
                                                     .getFactHandleForObject( f3 ),
                                                f3 );
                    workingMemory
                                 .retractObject( tuple
                                                      .getFactHandleForObject( f1 ) );
                }
                catch ( FactException e )
                {
                    throw new ConsequenceException( e );
                }

                FibTotal total = (FibTotal) workingMemory.getApplicationData("fibtotal");
                total.setTotal(total.getTotal() + 1);
            }
        };
        calculateRule.setConsequence( calculateConsequence );
        ruleSet.addRule( calculateRule );

        // Build the RuleSet.
        RuleBaseBuilder builder = new RuleBaseBuilder( );
        builder.addRuleSet( ruleSet );
        RuleBase ruleBase = builder.build( );
        WorkingMemory workingMemory = getWorkingMemory( ruleBase );
        workingMemory.addEventListener(new TestWorkingMemoryEventListener());

        workingMemory.setApplicationData("fibtotal", new FibTotal());

        // Assert the facts, and fire the rules.
        Fibonacci fibonacci = new Fibonacci( 50 );
        long start = System.currentTimeMillis( );
        workingMemory.assertObject( fibonacci );

        //test serialization
        workingMemory = serializeWorkingMemory( workingMemory );
        workingMemory = serializeWorkingMemory( workingMemory );

        workingMemory.fireAllRules( );

        //test serialization
        workingMemory = serializeWorkingMemory( workingMemory );
        workingMemory = serializeWorkingMemory( workingMemory );

        //test application ran correctly
        assertEquals(2, workingMemory.getObjects().size());
        Fibonacci a = (Fibonacci) workingMemory.getObjects().get(0);
        Fibonacci b = (Fibonacci) workingMemory.getObjects().get(1);
        assertEquals(50, a.getSequence());
        assertEquals(49, b.getSequence());

        assertEquals(12586269025L, a.getValue());
        assertEquals(7778742049L, b.getValue());

        //test application data
        FibTotal total = (FibTotal) workingMemory.getApplicationData("fibtotal");
        assertEquals(50, total.getTotal());

        //test listener
        TestWorkingMemoryEventListener listener = (TestWorkingMemoryEventListener) workingMemory.getListeners().get(0);
        assertEquals(50, listener.asserted);
        assertEquals(48, listener.retracted);
        assertEquals(50, listener.modified);
        //can't test this as it changes on each run
        //assertEquals(2024, listener.tested);
        assertEquals(100, listener.created);
        assertEquals(99, listener.fired);
        assertEquals(1, listener.cancelled);

    }

    public static class Fibonacci implements Serializable
    {
        private int  sequence;

        private long value;

        public Fibonacci(int sequence)
        {
            this.sequence = sequence;
            this.value = -1;
        }

        public int getSequence()
        {
            return this.sequence;
        }

        public void setValue(long value)
        {
            this.value = value;
        }

        public long getValue()
        {
            return this.value;
        }

        public String toString()
        {
            return "Fibonacci(" + this.sequence + "/" + this.value + ")";
        }
    }

    public static class FibTotal implements Serializable
    {
        int total = 0;

        public void setTotal(int total)
        {
            this.total = total;
        }

        public int getTotal()
        {
            return this.total;
        }
    }

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
}