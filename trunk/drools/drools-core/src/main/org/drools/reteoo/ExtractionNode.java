package org.drools.reteoo;

import org.drools.FactHandle;
import org.drools.FactException;
import org.drools.AssertionException;
import org.drools.RetractionException;
import org.drools.rule.Declaration;
import org.drools.spi.Extractor;

import java.io.Serializable;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

/** <i> extraction</i> node in the Rete-OO network.
 *
 *  @see ExtractionNode
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
class ExtractionNode
    extends TupleSource
    implements TupleSink, Serializable
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
    public ExtractionNode(TupleSource tupleSource,
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
                            WorkingMemoryImpl workingMemory)
        throws AssertionException
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
                              WorkingMemoryImpl workingMemory)
        throws RetractionException
    {
        propagateRetractTuples( key,
                                workingMemory );
    }

    /** Modify tuples.
     *
     *  @param trigger The root fact object handle.
     *  @param newTuples Modification replacement tuples.
     *  @param workingMemory The working memory session.
     *
     *  @throws FactException If an error occurs while modifying.
     */
    public void modifyTuples(FactHandle trigger,
                             TupleSet newTuples,
                             WorkingMemoryImpl workingMemory)
        throws FactException
    {
        Set retractedKeys = new HashSet();

        Iterator  tupleIter = newTuples.iterator();
        ReteTuple eachTuple = null;

        while ( tupleIter.hasNext() )
        {
            eachTuple = (ReteTuple) tupleIter.next();

            retractedKeys.add( eachTuple.getKey() );
        }

        Iterator keyIter = retractedKeys.iterator();
        TupleKey eachKey = null;

        while ( keyIter.hasNext() )
        {
            eachKey = (TupleKey) keyIter.next();

            propagateRetractTuples( eachKey,
                                    workingMemory );
        }

        tupleIter = newTuples.iterator();

        while ( tupleIter.hasNext() )
        {
            eachTuple = (ReteTuple) tupleIter.next();

            assertTuple( eachTuple,
                         workingMemory );
        }
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
