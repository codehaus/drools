package org.drools.reteoo.impl;

/*
 $Id: ExtractionNodeImpl.java,v 1.3 2002-08-21 05:46:13 bob Exp $

 Copyright 2002 (C) The Werken Company. All Rights Reserved.
 
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
    permission of The Werken Company. "drools" is a registered
    trademark of The Werken Company.
 
 5. Due credit should be given to The Werken Company.
    (http://drools.werken.com/).
 
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

import org.drools.WorkingMemory;
import org.drools.FactException;
import org.drools.AssertionException;
import org.drools.RetractionException;
import org.drools.reteoo.ExtractionNode;
import org.drools.rule.Declaration;
import org.drools.spi.Extractor;

import java.util.Set;
import java.util.HashSet;

/** <i> extraction</i> node in the Rete-OO network.
 *
 *  @see ExtractionNode
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
public class ExtractionNodeImpl extends TupleSourceImpl implements ExtractionNode, TupleSinkImpl
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** All declarations for this node. */
    private Set tupleDeclarations;

    /** Declaration on LHS. */
    private Declaration targetDeclaration;

    /**  extrator. */
    private Extractor extractor;

    // ------------------------------------------------------------
    //     Constructors 
    // ------------------------------------------------------------

    /** Construct.
     *
     *  @param tupleSource Parent tuple source.
     *  @param targetDeclaration Target of extraction.
     *  @param extractor The fact extractor to use.
     */
    public ExtractionNodeImpl(TupleSourceImpl tupleSource,
                              Declaration targetDeclaration,
                              Extractor extractor)
    {
        this.extractor     = extractor;
        this.targetDeclaration = targetDeclaration;

        Set sourceDecls = tupleSource.getTupleDeclarations();

        this.tupleDeclarations = new HashSet( sourceDecls.size() + 1 );

        this.tupleDeclarations.addAll( sourceDecls );
        this.tupleDeclarations.add( targetDeclaration );

        tupleSource.setTupleSink( this );
    }

    // ------------------------------------------------------------
    //     Instance methods 
    // ------------------------------------------------------------

    /** Retrieve the <code>Declaration</code> which is the target of
     *  the extraction.
     *
     *  @see Declaration
     *
     *  @return The target <code>Declaration</code>.
     */
    public Declaration getTargetDeclaration()
    {
        return this.targetDeclaration;
    }

    /** Retrieve the <code>Extractor</code> used to generate the
     *  right-hand-side value for the extraction.
     *
     *  @see Extractor
     *
     *  @return The <code>Extrator</code>.
     */
    public Extractor getExtractor()
    {
        return this.extractor;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     org.drools.reteoo.impl.TupleSource
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Retrieve the <code>Set</code> of <code>Declaration</code>s
     *  in the propagated <code>Tuples</code>.
     *
     *  @see Declaration
     *
     *  @return The <code>Set</code> of <code>Declarations</code>
     *          in progated <code>Tuples</code>.
     */
    public Set getTupleDeclarations()
    {
        return this.tupleDeclarations;
    }

    /** Assert a new <code>Tuple</code>.
     *
     *  @param tuple The <code>Tuple</code> being asserted.
     *  @param workingMemory The working memory seesion.
     *
     *  @throws AssertionException If an error occurs while asserting.
     */
    public void assertTuple(ReteTuple tuple,
                            WorkingMemory workingMemory) throws AssertionException
    {
        Object value = getExtractor().extractFact( tuple );

        ReteTuple newTuple = new ReteTuple( tuple );

        newTuple.putOtherColumn( getTargetDeclaration(),
                                 value );

        propagateAssertTuple( newTuple,
                              workingMemory );
    }

    /** Retract tuples.
     *
     *  @param key The tuple key.
     *  @param workingMemory The working memory seesion.
     *
     *  @throws RetractionException If an error occurs while retracting.
     */
    public void retractTuples(TupleKey key,
                              WorkingMemory workingMemory) throws RetractionException
    {
        propagateRetractTuples( key,
                                workingMemory );
    }

    /** Modify tuples.
     *
     *  @param trigger The root fact object.
     *  @param newTuples Modification replacement tuples.
     *  @param workingMemory The working memory session.
     *
     *  @throws FactException If an error occurs while modifying.
     */
    public void modifyTuples(Object trigger,
                             TupleSet newTuples,
                             WorkingMemory workingMemory) throws FactException
    {
        propagateModifyTuples( trigger,
                               newTuples,
                               workingMemory );
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     java.lang.Object
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Produce a debug string.
     *
     *  @return The debug string.
     */
    public String toString()
    {
        return "[ExtractionNodeImpl: target=" + getTargetDeclaration()
            + "; extractor=" + getExtractor() + "]";
    }
}
