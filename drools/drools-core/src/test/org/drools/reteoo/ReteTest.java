package org.drools.reteoo;

/*
 * $Id: ReteTest.java,v 1.10 2005-05-08 04:05:13 dbarnett Exp $
 *
 * Copyright 2003-2005 (C) The Werken Company. All Rights Reserved.
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

import java.util.Collection;
import java.util.List;

import org.drools.DroolsTestCase;
import org.drools.spi.MockObjectType;

public class ReteTest extends DroolsTestCase
{
    private Rete                       rete;

    private InstrumentedObjectTypeNode objectTypeNode;

    private InstrumentedObjectTypeNode stringTypeNode;

    public void setUp()
    {
        this.rete = new Rete( );

        this.objectTypeNode = new InstrumentedObjectTypeNode( new MockObjectType( Object.class ) );
        this.stringTypeNode = new InstrumentedObjectTypeNode( new MockObjectType( String.class ) );

        this.rete.addObjectTypeNode( this.objectTypeNode );
        this.rete.addObjectTypeNode( this.stringTypeNode );
    }

    public void tearDown()
    {
        this.rete = null;
    }

    public void testGetObjectTypeNodes() throws Exception
    {
        Collection objectTypeNodes = this.rete.getObjectTypeNodes( );

        assertEquals( 2, objectTypeNodes.size( ) );

        assertTrue( objectTypeNodes.contains( this.objectTypeNode ) );
        assertTrue( objectTypeNodes.contains( this.stringTypeNode ) );
    }

    /**
     * All objects asserted to a RootNode must be propagated to all children
     * ObjectTypeNodes.
     */
    public void testAssertObject() throws Exception
    {
        Object object1 = new Object( );
        String string1 = "cheese";

        this.rete.assertObject( new FactHandleImpl( 1 ), object1, null );

        this.rete.assertObject( new FactHandleImpl( 2 ), string1, null );

        List asserted = null;

        // ----------------------------------------

        asserted = this.objectTypeNode.getAssertedObjects( );

        assertEquals( 2, asserted.size( ) );

        assertSame( object1, asserted.get( 0 ) );

        assertSame( string1, asserted.get( 1 ) );

        // ----------------------------------------

        asserted = this.stringTypeNode.getAssertedObjects( );

        assertEquals( 2, asserted.size( ) );

        assertSame( object1, asserted.get( 0 ) );

        assertSame( string1, asserted.get( 1 ) );
    }

    /**
     * All objects retracted from a RootNode must be propagated to all children
     * ObjectTypeNodes.
     */
    public void testRetractObject() throws Exception
    {
        WorkingMemoryImpl memory = new WorkingMemoryImpl( new RuleBaseImpl( new Rete( ) ) );

        FactHandleImpl handle1 = new FactHandleImpl( 1 );
        FactHandleImpl handle2 = new FactHandleImpl( 2 );

        memory.putObject( handle1, "cheese1" );

        memory.putObject( handle2, "cheese2" );

        this.rete.retractObject( handle1, memory );

        this.rete.retractObject( handle2, memory );

        List retracted = null;

        // ----------------------------------------

        retracted = this.objectTypeNode.getRetractedHandles( );

        assertLength( 2, retracted );

        assertContains( handle1, retracted );

        assertContains( handle2, retracted );

        // ----------------------------------------

        retracted = this.stringTypeNode.getRetractedHandles( );

        assertLength( 2, retracted );

        assertContains( handle1, retracted );

        assertContains( handle2, retracted );
    }
}
