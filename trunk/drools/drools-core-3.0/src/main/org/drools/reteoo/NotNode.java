package org.drools.reteoo;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.drools.AssertionException;
import org.drools.FactException;
import org.drools.RetractionException;

public class NotNode extends BetaNode
{
    // ------------------------------------------------------------
    // Instance methods
    // ------------------------------------------------------------

    /**
     * Construct.
     * 
     * @param leftInput
     *            The left input <code>TupleSource</code>.
     * @param rightInput
     *            The right input <code>TupleSource</code>.
     */
    NotNode(int id,
             TupleSource leftInput,
             ObjectSource rightInput,
             int column)//,
             //BetaNodeDecorator decorator)
    {
        super( id,
              leftInput,
              rightInput,
              column,
              //decorator,
              new BetaNodeBinder( ) );
    }

    /**
     * Construct.
     * 
     * @param leftInput
     *            The left input <code>TupleSource</code>.
     * @param rightInput
     *            The right input <code>TupleSource</code>.
     */
    NotNode(int id,
             TupleSource leftInput,
             ObjectSource rightInput,
             int column,
             //BetaNodeDecorator decorator,
             BetaNodeBinder joinNodeBinder)
    {    
        super( id,
               leftInput,
               rightInput, 
               column,
               joinNodeBinder );
    }
    
    /**
     * Assert a new <code>Tuple</code> from the left input.
     * 
     * @param tuple
     *            The <code>Tuple</code> being asserted.
     * @param workingMemory
     *            The working memory seesion.
     * @throws AssertionException
     *             If an error occurs while asserting.
     */
    public void assertTuple(ReteTuple leftTuple,
                            PropagationContext context,
                            WorkingMemoryImpl workingMemory) throws FactException
    {
        BetaMemory memory = ( BetaMemory ) workingMemory.getNodeMemory( this );
        if ( !memory.contains( leftTuple.getKey( ) ) )
        {            
            TupleMatches tupleMatches = new TupleMatches( leftTuple );            
            memory.put( leftTuple.getKey(),
                        tupleMatches );
                   
            FactHandleImpl handle = null;

            BetaNodeBinder binder = getJoinNodeBinder();
            Iterator it = memory.getRightMemory().iterator( );
            while ( it.hasNext( ) )
            {
                handle = (FactHandleImpl) it.next( );
                if ( binder.isAllowed( handle,
                                      leftTuple,
                                      workingMemory ) )
                {             
                    tupleMatches.addMatch( handle );
                }                                
            }
            
            if ( tupleMatches.getMatches().size() == 0 )
            {
                TupleSet tupleSet = new TupleSet( );
                tupleSet.addTuple( leftTuple );
                propagateAssertTuples( tupleSet,
                                       context,
                                       workingMemory );                  
            }               
                      
        }
    }

    /**
     * Assert a new <code>Tuple</code> from the right input.
     * 
     * @param tuple
     *            The <code>Tuple</code> being asserted.
     * @param workingMemory
     *            The working memory seesion.
     */
    public void assertObject(Object object,
                             FactHandleImpl handle,
                             PropagationContext context,
                             WorkingMemoryImpl workingMemory) throws FactException
    {
        BetaMemory memory = ( BetaMemory ) workingMemory.getNodeMemory( this );
        if ( !memory.contains( handle ) )
        {
            memory.add( handle );
            
            ReteTuple leftTuple = null;
            TupleMatches tupleMatches = null;

            TupleSet tupleSet = new TupleSet( );
            BetaNodeBinder binder = getJoinNodeBinder();
            Iterator it = memory.getLeftMemory().values( ).iterator( );
            
            while ( it.hasNext( ) )
            {        
                tupleMatches = (TupleMatches) it.next( );
                leftTuple = tupleMatches.getTuple( );
                if ( binder.isAllowed( object,
                                       handle,
                                       leftTuple,
                                       workingMemory ) )
                {
                    tupleMatches.addMatch( handle );
                }
                
                if ( tupleMatches.getMatches().size() == 0 )
                {
                    tupleSet.addTuple( leftTuple );
                }    
            }
            
            propagateAssertTuples( tupleSet,
                                   context,
                                   workingMemory );                        
        }
    }
    
    /**
     * Retract tuples.
     * 
     * @param key
     *            The tuple key.
     * @param workingMemory
     *            The working memory seesion.
     * @throws RetractionException
     *             If an error occurs while retracting.
     */
    public void retractTuples(TupleKey leftKey,
                              PropagationContext context,
                              WorkingMemoryImpl workingMemory) throws FactException
    {
        BetaMemory memory = ( BetaMemory ) workingMemory.getNodeMemory( this );

        if ( memory.contains( leftKey ) )
        {
            List keys = new ArrayList( 1 );
            keys.add( leftKey );
            memory.remove( leftKey );
            propagateRectractTuples( keys,
                                     context,
                                     workingMemory );                         
        }
    }

    /**
     * Retract tuples.
     * 
     * @param key
     *            The tuple key.
     * @param workingMemory
     *            The working memory seesion.
     * @throws AssertionException 
     */
    public void retractObject(FactHandleImpl handle,
                              PropagationContext context,
                              WorkingMemoryImpl workingMemory) throws AssertionException, FactException
    {
        BetaMemory memory = (BetaMemory) workingMemory.getNodeMemory( this );

        if ( memory.contains( handle ) )
        {
            TupleMatches tupleMatches = null;        
            TupleSet tupleSet = new TupleSet( );
            memory.remove( handle );
            Iterator it = memory.getLeftMemory().values( ).iterator( );
            
            while ( it.hasNext( ) )
            {
                tupleMatches = (TupleMatches) it.next( );
                if ( tupleMatches.matched( handle ) )
                {
                    tupleMatches.removeMatch( handle );           
                }
                
                if ( tupleMatches.getMatches().size() == 0 )
                {
                    tupleSet.addTuple( tupleMatches.getTuple() );
                }                                  
            }
            
            propagateAssertTuples( tupleSet,
                                   context,
                                   workingMemory );
        }
    }    
    
}
