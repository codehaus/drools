package org.drools.reteoo;

/*
 * $Id: TupleKeyTest.java,v 1.10 2005-05-08 04:05:13 dbarnett Exp $
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

import junit.framework.TestCase;

public class TupleKeyTest extends TestCase
{
    public void testNothing()
    {
    }
    /*
     * public void testPutAll() { this.key.put( this.decl1, this.handle1,
     * this.obj1 );
     *
     * this.key.put( this.decl2, this.handle2, this.obj2 );
     *
     * TupleKey otherKey = new TupleKey();
     *
     * otherKey.putAll( this.key );
     *
     * assertEquals( 2, otherKey.size() );
     *
     * assertTrue( otherKey.containsDeclaration( this.decl1 ) ); assertTrue(
     * otherKey.containsDeclaration( this.decl2 ) );
     *
     * assertTrue( otherKey.containsFactHandle( this.handle1 ) );
     * assertTrue( otherKey.containsFactHandle( this.handle2 ) );
     *
     * assertSame( this.obj1, otherKey.get( this.decl1 ) ); assertSame(
     * this.obj2, otherKey.get( this.decl2 ) );
     *
     * assertSame( this.handle1, otherKey.getFactHandle( this.obj1 ) );
     *
     * assertSame( this.handle2, otherKey.getFactHandle( this.obj2 ) ); }
     *
     * public void testContainsAll_Exact() { TupleKey otherKey = new TupleKey();
     *
     * this.key.put( this.decl1, this.handle1, this.obj1 );
     *
     * this.key.put( this.decl2, this.handle2, this.obj2 );
     *
     * otherKey.put( this.decl1, this.handle1, this.obj1 );
     *
     * otherKey.put( this.decl2, this.handle2, this.obj2 );
     *
     * assertTrue( this.key.containsAll( otherKey ) ); assertTrue(
     * otherKey.containsAll( this.key ) ); }
     *
     * public void testContainsAll_Superset() { TupleKey otherKey = new
     * TupleKey();
     *
     * this.key.put( this.decl1, this.handle1, this.obj1 );
     *
     * this.key.put( this.decl2, this.handle2, this.obj2 );
     *
     * otherKey.put( this.decl1, this.handle1, this.obj1 );
     *
     * assertTrue( this.key.containsAll( otherKey ) ); assertTrue( !
     * otherKey.containsAll( this.key ) ); }
     *
     * public void testContainsAll_Subset() { TupleKey otherKey = new
     * TupleKey();
     *
     * this.key.put( this.decl1, this.handle1, this.obj1 );
     *
     * otherKey.put( this.decl1, this.handle1, this.obj1 );
     *
     * otherKey.put( this.decl2, this.handle2, this.obj2 );
     *
     *
     * assertTrue( ! this.key.containsAll( otherKey ) ); assertTrue(
     * otherKey.containsAll( this.key ) ); }
     *
     * public void testContainsAll_Null_Null() { TupleKey otherKey = new
     * TupleKey();
     *
     * this.key.put( this.decl1, this.handle1, this.obj1 );
     *
     * this.key.put( this.decl2, this.handle2, null );
     *
     * otherKey.put( this.decl1, this.handle1, this.obj1 );
     *
     * otherKey.put( this.decl2, this.handle2, null );
     *
     * assertTrue( this.key.containsAll( otherKey ) ); assertTrue(
     * otherKey.containsAll( this.key ) ); }
     *
     * public void testContainsAll_MismatchValues() { TupleKey otherKey = new
     * TupleKey();
     *
     * this.key.put( this.decl1, this.handle1, this.obj1 );
     *
     * this.key.put( this.decl2, this.handle2, this.obj2 );
     *
     * otherKey.put( this.decl1, this.handle1, this.obj1 );
     *
     * otherKey.put( this.decl2, this.handle2, new Object() );
     *
     * assertTrue( this.key.containsAll( otherKey ) ); assertTrue(
     * otherKey.containsAll( this.key ) ); }
     *
     * public void testContainsAll_MismatchHandle() { TupleKey otherKey = new
     * TupleKey();
     *
     * this.key.put( this.decl1, this.handle1, this.obj1 );
     *
     * this.key.put( this.decl2, this.handle2, this.obj2 );
     *
     * otherKey.put( this.decl1, this.handle1, this.obj1 );
     *
     * otherKey.put( this.decl2, new FactHandleImpl( 42 ), this.obj2 );
     *
     * assertTrue( ! this.key.containsAll( otherKey ) ); assertTrue( !
     * otherKey.containsAll( this.key ) ); }
     *
     * public void testContainsAll_MismatchDecls() { TupleKey otherKey = new
     * TupleKey();
     *
     * Declaration decl = new Declaration( new MockObjectType( Object.class ),
     * "yetAnother" );
     *
     * this.key.put( this.decl1, this.handle1, this.obj1 );
     *
     * this.key.put( this.decl2, this.handle2, this.obj2 );
     *
     * otherKey.put( this.decl1, this.handle1, this.obj1 );
     *
     * otherKey.put( decl, new FactHandleImpl( 42 ), this.obj2 );
     *
     * assertTrue( ! this.key.containsAll( otherKey ) ); assertTrue( !
     * otherKey.containsAll( this.key ) ); }
     *
     * public void testEquals_WrongClass() { this.key.put( this.decl1,
     * this.handle1, this.obj1 );
     *
     * assertFalse( new Object().equals( this.key ) ); assertFalse(
     * this.key.equals( new Object() ) ); }
     *
     * public void testEquals_SameObject() { this.key.put( this.decl1,
     * this.handle1, this.obj1 );
     *
     * assertEquals( this.key, this.key ); }
     *
     * public void testEquals_EqualButDifferent() { TupleKey otherKey = new
     * TupleKey();
     *
     * this.key.put( this.decl1, this.handle1, this.obj1 );
     *
     * this.key.put( this.decl2, this.handle2, this.obj2 );
     *
     * otherKey.put( this.decl1, this.handle1, new Object() );
     *
     * otherKey.put( this.decl2, this.handle2, new Object() );
     *
     * assertTrue( this.key.equals( otherKey ) ); assertTrue( otherKey.equals(
     * this.key ) ); }
     *
     * public void testHashCode_EqualButDifferent() { TupleKey otherKey = new
     * TupleKey();
     *
     * this.key.put( this.decl1, this.handle1, this.obj1 );
     *
     * this.key.put( this.decl2, this.handle2, this.obj2 );
     *
     * otherKey.put( this.decl1, this.handle1, "object-1" );
     *
     * otherKey.put( this.decl2, this.handle2, "object-2" );
     *
     * assertEquals( this.key.hashCode(), otherKey.hashCode() ); }
     *
     * public void testEquals_NoObjects() { TupleKey key1 = new TupleKey(
     * this.handle1 ); TupleKey key2 = new TupleKey( this.handle1 );
     *
     * assertEquals( key1, key2 );
     *
     * assertEquals( key1.hashCode(), key2.hashCode() ); }
     */
}
