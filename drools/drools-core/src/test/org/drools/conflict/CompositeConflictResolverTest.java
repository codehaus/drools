package org.drools.conflict;

import junit.framework.TestCase;
import org.drools.spi.Activation;
import org.drools.spi.MockTuple;
import org.drools.rule.Rule;

/*
 * $Id: ComplexityConflictResolverTest.java,v 1.3 2004/09/16 23:43:08 mproctor
 * Exp $
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

public class CompositeConflictResolverTest extends TestCase
{
    private static final Activation AGENDA_ITEM = new MockAgendaItem(
                                                                      new MockTuple( ),
                                                                      new Rule(
                                                                                "rule" ) );

    public void testAllComponentsAreCalled()
    {
        MockConflictResolver[] resolvers = {new MockConflictResolver( 0 ),
        new MockConflictResolver( 0 ), new MockConflictResolver( 0 ),};

        CompositeConflictResolver resolver = new CompositeConflictResolver(
                                                                            resolvers );

        resolver.compare( AGENDA_ITEM, AGENDA_ITEM );

        assertEquals( 1, resolvers[0].getCount( ) );
        assertEquals( 1, resolvers[1].getCount( ) );
        assertEquals( 1, resolvers[2].getCount( ) );
    }

    public void testPositiveResultTerminatesEvaluation()
    {
        MockConflictResolver[] resolvers = {new MockConflictResolver( 0 ),
        new MockConflictResolver( 1 ), new MockConflictResolver( 2 ),};

        CompositeConflictResolver resolver = new CompositeConflictResolver(
                                                                            resolvers );

        resolver.compare( AGENDA_ITEM, AGENDA_ITEM );

        assertEquals( 1, resolvers[0].getCount( ) );
        assertEquals( 1, resolvers[1].getCount( ) );
        assertEquals( 0, resolvers[2].getCount( ) );
    }

    public void testNegativeResultTerminatesEvaluation()
    {
        MockConflictResolver[] resolvers = {new MockConflictResolver( 0 ),
        new MockConflictResolver( -1 ), new MockConflictResolver( -2 ),};

        CompositeConflictResolver resolver = new CompositeConflictResolver(
                                                                            resolvers );

        resolver.compare( AGENDA_ITEM, AGENDA_ITEM );

        assertEquals( 1, resolvers[0].getCount( ) );
        assertEquals( 1, resolvers[1].getCount( ) );
        assertEquals( 0, resolvers[2].getCount( ) );
    }

    private static final class MockConflictResolver extends
                                                   AbstractConflictResolver
    {
        private final int result;

        private int       count;

        public MockConflictResolver(int result)
        {
            this.result = result;
        }

        public int compare(Activation existing, Activation adding)
        {
            ++count;
            return result;
        }

        public int getCount()
        {
            return count;
        }
    }
}