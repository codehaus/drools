
package org.drools.reteoo;

import org.drools.WorkingMemory;
import org.drools.FactException;
import org.drools.AssertionException;
import org.drools.RetractionException;
import org.drools.ModificationException;

import org.drools.spi.Declaration;
import org.drools.spi.FilterCondition;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

/** Node which filters {@link ReteTuple}s.
 *
 *  <p>
 *  Using a semantic {@link FilterCondition}, this node
 *  may allow or disallow <code>Tuples</code> to proceed
 *  further through the Rete-OO network.
 *  </p>
 *
 *  @see FilterCondition
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class FilterNode extends TupleSource implements TupleSink
{
    /** The semantic <code>FilterCondition</code>. */
    private FilterCondition filterCondition;

    /** The source of incoming <code>Tuples</code>. */
    private TupleSource     tupleSource;

    /** Construct.
     *
     *  @param condition The semantic <code>FilterCondition</code>.
     *  @param tupleSource The source of incoming <code>Tuples</code>.
     */
    public FilterNode(TupleSource tupleSource,
                      FilterCondition filterCondition)
    {
        this.filterCondition = filterCondition;
        this.tupleSource     = tupleSource;

        if ( tupleSource != null )
        {
            this.tupleSource.setTupleSink( this );
        }
    }

    public Set getTupleDeclarations()
    {
        return this.tupleSource.getTupleDeclarations();
    }

    /** Retrieve the <code>FilterCondition</code> associated
     *  with this node.
     *
     *  @return The <code>FilterCondition</code>.
     */
    public FilterCondition getFilterCondition()
    {
        return this.filterCondition;
    }

    public void assertTuple(TupleSource inputSource,
                            ReteTuple tuple,
                            WorkingMemory workingMemory) throws AssertionException
    {
        if ( getFilterCondition().isAllowed( tuple ) )
        {
            propagateAssertTuple( tuple,
                                  workingMemory );
        }
    }

    public void retractTuples(TupleKey key,
                              WorkingMemory workingMemory) throws RetractionException
    {
        propagateRetractTuples( key,
                                workingMemory );
    }

    public void modifyTuples(TupleSource source,
                             Object trigger,
                             TupleSet newTuples,
                             WorkingMemory workingMemory) throws FactException
    {
        Set retractedKeys = new HashSet();

        Iterator  tupleIter = newTuples.iterator();
        ReteTuple eachTuple = null;

        while ( tupleIter.hasNext() )
        {
            eachTuple = (ReteTuple) tupleIter.next();

            if ( ! getFilterCondition().isAllowed( eachTuple ) )
            {
                tupleIter.remove();
                retractedKeys.add( eachTuple.getKey() );
            }
        }

        propagateModifyTuples( trigger,
                               newTuples,
                               workingMemory );

        Iterator keyIter = retractedKeys.iterator();
        TupleKey eachKey = null;

        while ( keyIter.hasNext() )
        {
            eachKey = (TupleKey) keyIter.next();

            propagateRetractTuples( eachKey,
                                    workingMemory );
        }
    }
}