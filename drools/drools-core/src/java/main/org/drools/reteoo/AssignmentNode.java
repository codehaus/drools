
package org.drools.reteoo;

import org.drools.WorkingMemory;
import org.drools.AssertionException;
import org.drools.RetractionException;
import org.drools.spi.Declaration;
import org.drools.spi.FactExtractor;

import java.util.Set;
import java.util.HashSet;

/** <i>Consistent-Assignment</i> node in the Rete-OO network.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class AssignmentNode extends TupleSource implements TupleSink
{
    private Set           tupleDeclarations;
    private Declaration   targetDeclaration;
    private FactExtractor factExtractor;

    public AssignmentNode(TupleSource tupleSource,
                          Declaration targetDeclaration,
                          FactExtractor factExtractor)
    {
        this.factExtractor     = factExtractor;
        this.targetDeclaration = targetDeclaration;

        Set sourceDecls = tupleSource.getTupleDeclarations();

        this.tupleDeclarations = new HashSet( sourceDecls.size() + 1 );

        this.tupleDeclarations.addAll( sourceDecls );
        this.tupleDeclarations.add( targetDeclaration );

        tupleSource.setTupleSink( this );
    }

    public Set getTupleDeclarations()
    {
        return this.tupleDeclarations;
    }

    /** Retrieve the {@link Declaration} which is the target of
     *  the assignment.
     *
     *  @return The target <code>Declaration</code>.
     */
    public Declaration getTargetDeclaration()
    {
        return this.targetDeclaration;
    }

    /** Retrieve the {@link FactExtractor} used to generate the
     *  right-hand-side value for the assignment.
     *
     *  @return The <code>FactExtrator</code>.
     */
    public FactExtractor getFactExtractor()
    {
        return this.factExtractor;
    }

    public void assertTuple(TupleSource tupleSource,
                            ReteTuple tuple,
                            WorkingMemory workingMemory) throws AssertionException
    {
        Object value = getFactExtractor().extractFact( tuple );

        ReteTuple newTuple = new ReteTuple( tuple );

        newTuple.putOtherColumn( getTargetDeclaration(),
                                 value );

        propagateAssertTuple( newTuple,
                              workingMemory );
    }

    public void retractObject(TupleSource tupleSource,
                              Object object,
                              WorkingMemory workingMemory) throws RetractionException
    {
        propagateRetractObject( object,
                                workingMemory );
    }
}
