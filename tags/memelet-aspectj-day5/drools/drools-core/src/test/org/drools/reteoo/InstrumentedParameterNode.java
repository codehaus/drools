package org.drools.reteoo;

/*
 * $Id: InstrumentedParameterNode.java,v 1.13 2005-05-08 04:05:13 dbarnett Exp $
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

import java.util.ArrayList;
import java.util.List;

import org.drools.AssertionException;
import org.drools.FactHandle;
import org.drools.RetractionException;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;

public class InstrumentedParameterNode extends ParameterNode
{
    private List        assertedObjects;

    private List        retractedObjects;

    private static Rule rule;

    /**
     *
     * this is a nasty hack, but need to make sure that the rule is passed via
     * the super call
     */
    static
    {
        rule = new Rule( "test-rule 1" );

        //add consequence
        rule.setConsequence( new org.drools.spi.InstrumentedConsequence( ) );
        //add condition
        rule.addCondition( new org.drools.spi.InstrumentedCondition( ) );
    }

    public InstrumentedParameterNode(ObjectTypeNode inputNode, Declaration decl)
    {
        super( inputNode, decl );

        this.assertedObjects = new ArrayList( );
        this.retractedObjects = new ArrayList( );
    }

    protected void assertObject(FactHandle handle,
                                Object object,
                                WorkingMemoryImpl workingMemory) throws AssertionException
    {
        super.assertObject( handle, object, workingMemory );

        this.assertedObjects.add( object );
    }

    protected void retractObject(FactHandle handle,
                                 WorkingMemoryImpl workingMemory) throws RetractionException
    {
        super.retractObject( handle, workingMemory );

        this.retractedObjects.add( handle );
    }

    public List getAssertedObjects()
    {
        return this.assertedObjects;
    }

    public List getRetractedObjects()
    {
        return this.retractedObjects;
    }
}
