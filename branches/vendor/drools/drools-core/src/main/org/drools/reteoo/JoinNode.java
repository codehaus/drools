
package org.drools.reteoo;

import org.drools.WorkingMemory;
import org.drools.AssertionException;
import org.drools.RetractionException;

import org.drools.spi.Tuple;
import org.drools.spi.Declaration;

import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

/** A two-input Rete-OO <i>join node</i>.
 *
 *  @see TupleSource
 *  @see TupleSink
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class JoinNode extends TupleSource implements TupleSink
{
    /** The left input <code>TupleSource</code>.
     */
    private TupleSource leftInput;

    /** The right input <code>TupleSource</code>.
     */
    private TupleSource rightInput;

    /** A <code>Set</code> of <code>Declarations</code>
     *  common to both left and right input sources.
     */
    private Set commonDeclarations;

    /** Construct.
     *
     *  @param leftInput The left input <code>TupleSource</code>.
     *  @param rightInput The right input <code>TupleSource</code>.
     */
    public JoinNode(TupleSource leftInput,
                    TupleSource rightInput)
    {
        this.leftInput  = leftInput;
        this.rightInput = rightInput;

        determineCommonDeclarations();

        leftInput.setTupleSink( this );
        rightInput.setTupleSink( this );
    }

    /** Set up the <code>Set</code> of common <code>Declarations</code>
     *  across the two input <code>TupleSources</code>.
     */
    private void determineCommonDeclarations()
    {
        this.commonDeclarations = new HashSet();

        Set leftDecls = leftInput.getTupleDeclarations();
        Set rightDecls = leftInput.getTupleDeclarations();

        Iterator    declIter = rightDecls.iterator();
        Declaration eachDecl = null;

        while ( declIter.hasNext() )
        {
            eachDecl = (Declaration) declIter.next();

            if ( leftDecls.contains( eachDecl ) )
            {
                this.commonDeclarations.add( eachDecl );
            }
        }
    }

    /** Retrieve the set of common <code>Declarations</code>
     *  across the two input <code>TupleSources</code>.
     *
     *  @return The <code>Set</code> of common <code>Declarations</code>.
     */
    public Set getCommonDeclarations()
    {
        return this.commonDeclarations;
    }

    public Set getTupleDeclarations()
    {
        Set decls = new HashSet();

        decls.addAll( getLeftInput().getTupleDeclarations() );
        decls.addAll( getRightInput().getTupleDeclarations() );

        return decls;
    }

    /** Retrieve the left input <code>TupleSource</code>.
     *
     *  @return The left input <code>TupleSource</code>.
     */
    protected TupleSource getLeftInput()
    {
        return this.leftInput;
    }

    /** Retrieve the right input <code>TupleSource</code>.
     *
     *  @return The right input <code>TupleSource</code>.
     */
    protected TupleSource getRightInput()
    {
        return this.rightInput;
    }

    public void assertTuple(TupleSource inputSource,
                            Tuple tuple,
                            WorkingMemory workingMemory) throws AssertionException
    {
        JoinMemory memory = workingMemory.getJoinMemory( this );

        List joinedTuples = null;

        if ( inputSource == leftInput )
        {
            joinedTuples = memory.addLeftTuple( tuple );
        }
        else
        {
            joinedTuples = memory.addRightTuple( tuple );
        }

        if ( joinedTuples.isEmpty() )
        {
            return;
        }

        Iterator tupleIter = joinedTuples.iterator();
        Tuple    eachTuple = null;

        while ( tupleIter.hasNext() )
        {
            eachTuple = (Tuple) tupleIter.next();

            propagateAssertTuple( eachTuple,
                                  workingMemory );
        }
    }

    public void retractObject(TupleSource inputSource,
                              Object object,
                              WorkingMemory workingMemory) throws RetractionException
    {
        propagateRetractObject( object,
                                workingMemory );
    }
}