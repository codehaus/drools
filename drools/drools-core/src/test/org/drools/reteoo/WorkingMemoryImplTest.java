/*
 * The RedHill Consulting, Pty. Ltd. Software License, Version 1.0
 *
 * Copyright (c) 2003-04 RedHill Consulting, Pty. Ltd.  All rights reserved.
 *
 * Redistribution and use in source or binary forms IS NOT PERMITTED
 * without prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED ``AS IS'' AND ANY EXPRESSED OR IMPLIED
 * WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 * OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 * DISCLAIMED.  IN NO EVENT SHALL REDHILL CONSULTING OR ITS CONTRIBUTORS
 * BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY,
 * OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA,
 * OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 * THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR
 * TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF
 * THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF
 * SUCH DAMAGE.
 */
package org.drools.reteoo;

import junit.framework.TestCase;
import org.drools.FactHandle;

import java.util.List;

/**
 * @author <a href="mailto:simon@redhillconsulting.com.au">Simon Harris</a>
 * @version $Id: WorkingMemoryImplTest.java,v 1.1 2004-10-26 23:05:23 simon Exp $
 */
public class WorkingMemoryImplTest extends TestCase
{
    public void testIdentityBasesUniquenessOfFacts() throws Exception
    {
        Object fact = new Object();

        WorkingMemoryImpl impl = new WorkingMemoryImpl( new RuleBaseImpl( new Rete( ) ) );

        FactHandle handleA = impl.assertObject( fact );

        assertNotNull( handleA );

        FactHandle handleB = impl.assertObject( fact );

        assertSame( handleA, handleB );

        List objects = impl.getObjects( );

        assertNotNull( objects );
        assertEquals( 1, objects.size( ) );
        assertSame( fact, objects.get( 0 ) );
    }
}