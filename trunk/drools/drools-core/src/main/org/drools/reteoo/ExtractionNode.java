package org.drools.reteoo;

/*
 * $Id: ExtractionNode.java,v 1.20 2004-10-27 07:15:54 simon Exp $
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

import org.drools.AssertionException;
import org.drools.FactException;
import org.drools.FactHandle;
import org.drools.RetractionException;
import org.drools.rule.Declaration;
import org.drools.spi.Extractor;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * <i>extraction </i> node in the Rete-OO network.
 *
 * @see ExtractionNode
 *
 * @author <a href="mailto:bob@eng.werken.com">bob mcwhirter </a>
 */
class ExtractionNode extends TupleSource implements TupleSink
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** All declarations for this node. */
    private Set         tupleDeclarations;

    /** Declaration on LHS. */
    private Declaration targetDeclaration;

    /** extrator. */
    private Extractor   extractor;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /**
     * Construct.
     *
     * @param tupleSource Parent tuple source.
     * @param targetDeclaration Target of extraction.
     * @param extractor The fact extractor to use.
     */
    public ExtractionNode(TupleSource tupleSource,
                          Declaration targetDeclaration,
                          Extractor extractor)
    {
        this.extractor = extractor;
        this.targetDeclaration = targetDeclaration;

        Set sourceDecls = tupleSource.getTupleDeclarations( );

        this.tupleDeclarations = new HashSet( sourceDecls.size( ) + 1 );

        this.tupleDeclarations.addAll( sourceDecls );
        this.tupleDeclarations.add( targetDeclaration );

        tupleSource.setTupleSink( this );
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /**
     * Retrieve the <code>Declaration</code> which is the target of the
     * extraction.
     *
     * @see Declaration
     *
     * @return The target <code>Declaration</code>.
     */
    public Declaration getTargetDeclaration()
    {
        return this.targetDeclaration;
    }

    /**
     * Retrieve the <code>Extractor</code> used to generate the
     * right-hand-side value for the extraction.
     *
     * @see Extractor
     *
     * @return The <code>Extrator</code>.
     */
    public Extractor getExtractor()
    {
        return this.extractor;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //     org.drools.reteoo.impl.TupleSource
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Retrieve the <code>Set</code> of <code>Declaration</code> s in the
     * propagated <code>Tuples</code>.
     *
     * @see Declaration
     *
     * @return The <code>Set</code> of <code>Declarations</code> in progated
     *         <code>Tuples</code>.
     */
    public Set getTupleDeclarations()
    {
        return this.tupleDeclarations;
    }

    /**
     * Assert a new <code>Tuple</code>.
     *
     * @param tuple The <code>Tuple</code> being asserted.
     * @param workingMemory The working memory seesion.
     *
     * @throws AssertionException If an error occurs while asserting.
     */
    public void assertTuple(ReteTuple tuple, WorkingMemoryImpl workingMemory) throws AssertionException
    {
        Object value = getExtractor( ).extractFact( tuple );

        // Extractions should never evaluate to null
        // Extractions with same target should be of same type and value
        if ((value == null)||(!checkExtractorOk(value, tuple)))
        {
            return;
        }

        ReteTuple newTuple = new ReteTuple( tuple );

        newTuple.putTargetDeclarationColumn( getTargetDeclaration( ), value );

        propagateAssertTuple( newTuple, workingMemory );
    }

    /**
     * Retract tuples.
     *
     * @param key The tuple key.
     * @param workingMemory The working memory seesion.
     *
     * @throws RetractionException If an error occurs while retracting.
     */
    public void retractTuples(TupleKey key, WorkingMemoryImpl workingMemory) throws RetractionException
    {
        propagateRetractTuples( key, workingMemory );
    }

    /**
     * Modify tuples.
     *
     * @param trigger The root fact object handle.
     * @param newTuples Modification replacement tuples.
     * @param workingMemory The working memory session.
     *
     * @throws FactException If an error occurs while modifying.
     */
    public void modifyTuples(FactHandle trigger,
                             TupleSet newTuples,
                             WorkingMemoryImpl workingMemory) throws FactException
    {
        Set retractedKeys = new HashSet( );

        Iterator tupleIter = newTuples.iterator( );
        ReteTuple eachTuple = null;

        while ( tupleIter.hasNext( ) )
        {
            eachTuple = ( ReteTuple ) tupleIter.next( );

            retractedKeys.add( eachTuple.getKey( ) );
        }

        Iterator keyIter = retractedKeys.iterator( );
        TupleKey eachKey = null;

        while ( keyIter.hasNext( ) )
        {
            eachKey = ( TupleKey ) keyIter.next( );

            propagateRetractTuples( eachKey, workingMemory );
        }

        tupleIter = newTuples.iterator( );

        while ( tupleIter.hasNext( ) )
        {
            eachTuple = ( ReteTuple ) tupleIter.next( );

            assertTuple( eachTuple, workingMemory );
        }
    }

    /**
     * For targets shared by extractors the
     * extracted fact should be the same.
     * If the given declaration is a targetDeclaration
     * for one it must be for the other, as its a common
     * declaration.
     *
     * @param value
     * @param tuple
     * @return
     */
    boolean checkExtractorOk(Object value, ReteTuple tuple)
    {
        Set otherTargetDecls = tuple.getDeclarations( );
        Declaration decl = this.targetDeclaration;

        if ((otherTargetDecls != null)&&otherTargetDecls.contains(decl))
        {
            if (!value.equals(tuple.get(decl)))
            {
                return false;
            }
        }
        return true;
    }

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //     java.lang.Object
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Produce a debug string.
     *
     * @return The debug string.
     */
    public String toString()
    {
        return "[ExtractionNodeImpl: target=" + getTargetDeclaration( )
               + "; extractor=" + getExtractor( ) + "]";
    }

}