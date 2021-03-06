package org.drools.reteoo;

/*
 * $Id: WorkingMemoryImplTest.java,v 1.7 2005-11-29 01:21:53 michaelneale Exp $
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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.ObjectInput;
import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;

import junit.framework.TestCase;

import org.drools.FactHandle;
import org.drools.RuleBase;
import org.drools.WorkingMemory;

/**
 * @author <a href="mailto:simon@redhillconsulting.com.au">Simon Harris</a>
 */
public class WorkingMemoryImplTest extends TestCase
{
    public void testIdentityBasesUniquenessOfFacts() throws Exception
    {
        Object fact = new Object();

        WorkingMemory memory = new WorkingMemoryImpl( new RuleBaseImpl( new Rete( ) ) );

        FactHandle handleA = memory.assertObject( fact );

        assertNotNull( handleA );

        FactHandle handleB = memory.assertObject( fact );

        assertSame( handleA, handleB );

        List objects = memory.getObjects( );

        assertNotNull( objects );
        assertEquals( 1, objects.size( ) );
        assertSame( fact, objects.get( 0 ) );
    }

    //Serialise twice to test DROOLS-182
    public void testSerializationIsRepeatable() throws Exception
    {
        TestObject fact = new TestObject();

        WorkingMemory memory = getWorkingMemory( new RuleBaseImpl( new Rete( ) ) );

        memory.assertObject( fact );

        memory = serializeWorkingMemory( memory );
        memory = serializeWorkingMemory( memory );

        List objects = memory.getObjects( );

        assertNotNull( objects );
        assertEquals( 1, objects.size( ) );
    }
    
    public void testFactHandleFactoryPerWorkingMemory() throws Exception {
        WorkingMemoryImpl memory = new WorkingMemoryImpl( new RuleBaseImpl( new Rete( ) ) );
        FactHandleFactory fact = memory.getFactHandleFactory();
        
        assertNotNull(fact);

        WorkingMemoryImpl memory2 = new WorkingMemoryImpl( new RuleBaseImpl( new Rete( ) ) );
        FactHandleFactory fact2 = memory2.getFactHandleFactory();
        
        assertTrue(System.identityHashCode(fact) != System.identityHashCode(fact2));  

        assertTrue(memory2.getFactHandleFactory() == memory2.getFactHandleFactory());
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

    final public static class TestObject implements Serializable
    {

    }
}