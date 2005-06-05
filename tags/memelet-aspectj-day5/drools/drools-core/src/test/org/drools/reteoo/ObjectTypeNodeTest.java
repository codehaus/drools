package org.drools.reteoo;

/*
 * $Id: ObjectTypeNodeTest.java,v 1.14 2005-05-08 04:05:13 dbarnett Exp $
 *
 * Copyright 2001-2005 (C) The Werken Company. All Rights Reserved.
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
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spi.MockObjectType;

public class ObjectTypeNodeTest extends DroolsTestCase
{
    public void testAssertObject() throws Exception
    {
        Rule rule = new Rule( getName( ) );

        Declaration decl = rule.addParameterDeclaration( "object", new MockObjectType( Object.class ) );

        ObjectTypeNode objectTypeNode = new ObjectTypeNode( new MockObjectType( String.class ) );

        InstrumentedParameterNode paramNode = new InstrumentedParameterNode( null, decl );
        paramNode.addTupleSink( new InstrumentedTupleSink( ) );

        objectTypeNode.addParameterNode( paramNode );

        WorkingMemoryImpl memory = new WorkingMemoryImpl( new RuleBaseImpl( new Rete( ) ) );

        Object string1 = "cheese";
        Object object1 = new Object( );

        FactHandleImpl handle1 = new FactHandleImpl( 1 );
        FactHandleImpl handle2 = new FactHandleImpl( 2 );

        memory.putObject( handle1, string1 );

        memory.putObject( handle2, object1 );

        objectTypeNode.assertObject( handle1, string1, memory );

        objectTypeNode.assertObject( handle2, object1, memory );

        List asserted = paramNode.getAssertedObjects( );

        assertEquals( 1, asserted.size( ) );

        assertSame( string1, asserted.get( 0 ) );

        Collection paramNodes = objectTypeNode.getParameterNodes( );

        assertEquals( 1, paramNodes.size( ) );

        assertTrue( paramNodes.contains( paramNode ) );
    }
}
