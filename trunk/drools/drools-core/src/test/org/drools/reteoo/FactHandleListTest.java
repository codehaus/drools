package org.drools.reteoo;

/*
 * $Id: FactHandleListTest.java,v 1.5 2005-05-08 04:05:13 dbarnett Exp $
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

import java.io.Serializable;

import junit.framework.TestCase;

import org.drools.FactHandle;

public class FactHandleListTest extends TestCase
{
    public void testIsSerializable()
    {
        assertTrue(Serializable.class.isAssignableFrom( FactHandleList.class ) );
    }

    public void testSingleValueConstruction()
    {
        FactHandle handle = new FactHandleImpl( 37 );
        FactHandleList list = new FactHandleList( 2, handle );
        assertEquals( 3, list.size( ) );
        assertNull( list.get( 0 ) );
        assertNull( list.get( 1 ) );
        assertSame( handle, list.get( 2 ) );
    }

    public void testJoinConstruction()
    {
        FactHandle handleA = new FactHandleImpl( 37 );
        FactHandle handleB = new FactHandleImpl( 43 );

        FactHandleList list = new FactHandleList( new FactHandleList( 1, handleA ),
                                                  new FactHandleList( 3, handleB ) );

        assertEquals( 4, list.size() );
        assertNull( list.get( 0 ) );
        assertSame( handleA, list.get( 1 ) );
        assertNull( list.get( 2 ) );
        assertSame( handleB, list.get( 3 ) );
    }

    public void testContains()
    {
        FactHandle handle = new FactHandleImpl( 13 );
        FactHandleList list = new FactHandleList( 8, handle );
        assertTrue( list.contains( handle ) );
        assertFalse( list.contains( new FactHandleImpl( 203 ) ) );
    }

    public void testContainsAll()
    {
        FactHandle handleA = new FactHandleImpl( 37 );
        FactHandle handleB = new FactHandleImpl( 43 );
        FactHandle handleC = new FactHandleImpl( 59 );
        FactHandle handleD = new FactHandleImpl( 61 );

        FactHandleList listA = new FactHandleList( 1, handleA );
        FactHandleList listB = new FactHandleList( 3, handleB );
        FactHandleList listC = new FactHandleList( 0, handleC );
        FactHandleList listD = new FactHandleList( 2, handleD );

        FactHandleList listAB = new FactHandleList( listA,
                                                    listB );

        FactHandleList listCD = new FactHandleList( listC,
                                                    listD );

        FactHandleList listACD = new FactHandleList( listA,
                                                     listCD );

        FactHandleList listABCD = new FactHandleList( listAB, listCD );

        assertTrue( listABCD.containsAll( listACD ) );
        assertFalse( listACD.containsAll( listABCD ) );
        assertFalse( listABCD.containsAll( new FactHandleList( 99, handleA ) ) );
    }

    public void testIndexOutOfBoundsThrowsException()
    {
        FactHandle handle = new FactHandleImpl( 253 );
        FactHandleList list = new FactHandleList( 0, handle );
        try
        {
            list.get( 1 );
            fail();
        }
        catch ( ArrayIndexOutOfBoundsException e )
        {
            // expected
        }
    }
}
