
package org.drools.reteoo;

import org.drools.WorkingMemory;
import org.drools.AssertionException;
import org.drools.RetractionException;
import org.drools.spi.Declaration;
import org.drools.spi.Tuple;
import org.drools.spi.FactExtractor;

import java.util.Set;
import java.util.HashSet;

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

    public Declaration getTargetDeclaration()
    {
        return this.targetDeclaration;
    }

    public FactExtractor getFactExtractor()
    {
        return this.factExtractor;
    }

    public void assertTuple(TupleSource tupleSource,
                            Tuple tuple,
                            WorkingMemory workingMemory) throws AssertionException
    {


        Object value = getFactExtractor().extractFact( tuple );

        ReteTuple newTuple = new ReteTuple();

        newTuple.putAll( tuple );

        newTuple.put( getTargetDeclaration(),
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
