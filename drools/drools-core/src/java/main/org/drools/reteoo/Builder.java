
package org.drools.reteoo;

import org.drools.spi.Rule;
import org.drools.spi.Declaration;
import org.drools.spi.ObjectType;
import org.drools.spi.FilterCondition;
import org.drools.spi.AssignmentCondition;
import org.drools.spi.Condition;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

/** Builds the Rete-OO network for a {@link org.drools.spi.RuleSet}.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class Builder
{
    /** Root node to build against. */
    private RootNode rootNode;

    /** Total-ordering priority counter. */
    private int priorityCounter;

    /** Construct a <code>Builder</code> against an existing
     *  {@link RootNode} in the network.
     *
     *  @param rootNode The network to add on to.
     */
    public Builder(RootNode rootNode)
    {
        this.rootNode = rootNode;
    }

    /** Retrieve the <code>RootNode</code> this <code>Builder</code>
     *  appends to.
     *
     *  @return The <code>RootNode</code>.
     */
    public RootNode getRootNode()
    {
        return this.rootNode;
    }

    /** Add a {@link Rule} to the network.
     *
     *  @param rule The rule to add.
     *
     *  @throws ReteConstructionException if an error prevents complete
     *          construction of the network for the <code>Rule</code>.
     */
    public void addRule(Rule rule) throws ReteConstructionException
    {
        Set assignmentConds = new HashSet( rule.getAssignmentConditions() );
        Set filterConds     = new HashSet( rule.getFilterConditions() );

        Set attachableNodes = null;

        boolean performedJoin     = false;
        boolean attachAssign      = false;
        boolean cycleAttachAssign = false;
        
        attachableNodes = createParameterNodes( rule );

        // System.err.println( " 1->" + attachableNodes );

        do 
        {
            performedJoin = false;
            attachAssign  = false;

            if ( ! filterConds.isEmpty() )
            {
                attachFilterConditions( filterConds,
                                        attachableNodes );
            }

            // System.err.println( " 2->" + attachableNodes );

            attachAssign = attachAssignmentConditions( assignmentConds,
                                                       attachableNodes );

            // System.err.println( " 3->" + attachableNodes );

            performedJoin = createJoinNodes( attachableNodes );

            // System.err.println( " 4->" + attachableNodes );
        }
        while ( ! attachableNodes.isEmpty() 
                &&
                ( performedJoin
                  ||
                  attachAssign
                  ) );

        TupleSource lastNode = (TupleSource) attachableNodes.iterator().next();

        TerminalNode terminal = new TerminalNode( lastNode,
                                                  rule.getAction(),
                                                  ++this.priorityCounter);
    }

    /** Create the {@link ParameterNode}s for the <code>Rule</code>,
     *  and link into the network.
     *
     *  @param rule The rule.
     *
     *  @return A <code>Set</code> of <code>ParameterNodes</code> created
     *          and linked into the network.
     */
    protected Set createParameterNodes(Rule rule)
    {
        Set attachableNodes = new HashSet();

        Set      parameterDecls  = rule.getParameterDeclarations();

        Iterator    declIter = parameterDecls.iterator();
        Declaration eachDecl = null;

        ObjectType     objectType     = null;
        ObjectTypeNode objectTypeNode = null;
        ParameterNode  paramNode      = null;

        while ( declIter.hasNext() )
        {
            eachDecl = (Declaration) declIter.next();

            objectType = eachDecl.getObjectType();

            objectTypeNode = getRootNode().getOrCreateObjectTypeNode( objectType );

            paramNode = new ParameterNode( objectTypeNode,
                                           eachDecl );

            attachableNodes.add( paramNode );
            
        }

        return attachableNodes;
    }
    

    /** Create and attach {@link FilterCondition}s to the network.
     *
     *  <p>
     *  It may not be possible to satisfy all filder conditions
     *  on the first pass.  This method removes satisfied conditions
     *  from the <code>filterCond</code> parameter, and leaves
     *  unsatisfied ones in the <code>Set</code>.
     *  </p>
     *
     *  @param filterConds Set of <code>FilterConditions</code>
     *         to attempt attaching.
     *  @param attachableNodes The current attachable leaf nodes
     *         of the network.
     */
    protected void attachFilterConditions(Set filterConds,
                                          Set attachableNodes)
    {
        Iterator        condIter    = filterConds.iterator();
        FilterCondition eachCond    = null;
        TupleSource     tupleSource = null;

        FilterNode filterNode = null;

        while ( condIter.hasNext() )
        {
            eachCond = (FilterCondition) condIter.next();

            tupleSource = findMatchingTupleSourceForFiltering( eachCond,
                                                               attachableNodes );

            if ( tupleSource == null )
            {
                continue;
            }

            condIter.remove();

            filterNode = new FilterNode( tupleSource,
                                         eachCond );

            attachableNodes.remove( tupleSource );

            attachableNodes.add( filterNode );
        }
    }

    /** Create and attach {@link JoinNode}s to the network.
     *
     *  <p>
     *  It may not be possible to join all <code>attachableNodes</code>.
     *  </p>
     *
     *  <p>
     *  Any <code>attachabeNodes</code> member that particiates
     *  in a <i>join</i> is removed from the <code>attachableNodes</code>
     *  collection, and replaced by the joining <code>JoinNode</code>.
     *  </p>
     *
     *  @param attachableNodes The current attachable leaf nodes of
     *         the network.
     *
     *  @return <code>true</code> if at least one <code>JoinNode</code>
     *          was created, else <code>false</code>.
     */
    protected boolean createJoinNodes(Set attachableNodes)
    {
        // System.err.println( "ENTER joinNodes" );
        boolean performedJoin = false;

        Object[] leftNodes  = attachableNodes.toArray();
        Object[] rightNodes = attachableNodes.toArray();

        TupleSource left  = null;
        TupleSource right = null;

        JoinNode joinNode = null;

      outter:
        for ( int i = 0 ; i < leftNodes.length ; ++i )
        {
            left = (TupleSource) leftNodes[i];

            if ( ! attachableNodes.contains( left ) )
            {
                continue outter;
            }

          inner:
            for ( int j = i + 1; j < rightNodes.length ; ++j )
            {
                right = (TupleSource) rightNodes[j];

                if ( ! attachableNodes.contains( right ) )
                {
                    continue inner;
                }

                if ( canBeJoined( left,
                                  right ) );

                {
                    joinNode = new JoinNode( left,
                                             right );

                    attachableNodes.remove( left );
                    attachableNodes.remove( right );

                    attachableNodes.add( joinNode );

                    performedJoin = true;

                    // System.err.println( joinNode + " from " + left + " and " + right );
                    // System.err.println( attachableNodes );

                    continue outter;
                }
            }
        }

        // System.err.println( "EXIT joinNodes" );
        return performedJoin;
    }

    /** Determine if two {@link TupleSource}s can be joined.
     *
     *  @return <code>true</code> if they can be joined (they share
     *          at least one common member declaration), else
     *          <code>false</code>.
     */
    protected boolean canBeJoined(TupleSource left,
                                  TupleSource right)
    {
        Set      leftDecls     = left.getTupleDeclarations();
        Iterator rightDeclIter = right.getTupleDeclarations().iterator();

        while ( rightDeclIter.hasNext() )
        {
            if ( leftDecls.contains( rightDeclIter.next() ) )
            {
                return true;
            }
        }

        return false;
    }

    /** Create and attach {@link AssignmentCondition}s to the network.
     *
     *  <p>
     *  It may not be possible to satisfy all <code>assignmentConds</code>,
     *  in which case, unsatisfied conditions will remain in the <code>Set</code>
     *  passed in as <code>assignmentConds</code>.
     *  </p>
     *
     *  @param assignmentConds Set of <code>AssignmentConditions</code> to
     *         attach to the network.
     *  @param attachableNodes The current attachable leaf nodes of
     *         the network.
     */
    protected boolean attachAssignmentConditions(Set assignmentConds,
                                                 Set attachableNodes)
    {
        boolean attached      = false;
        boolean cycleAttached = false;

        do
        {
            cycleAttached = false;

            Iterator            condIter  = assignmentConds.iterator();
            AssignmentCondition eachCond  = null;
            TupleSource         tupleSource = null;
            
            AssignmentNode assignNode = null;
            
            while ( condIter.hasNext() )
            {
                eachCond = (AssignmentCondition) condIter.next();
                
                tupleSource = findMatchingTupleSourceForAssignment( eachCond,
                                                                    attachableNodes );
                
                if ( tupleSource == null )
                {
                    continue;
                }
                
                condIter.remove();
                
                assignNode = new AssignmentNode( tupleSource,
                                                 eachCond.getTargetDeclaration(),
                                                 eachCond.getFactExtractor() );
                
                attachableNodes.remove( tupleSource );
                attachableNodes.add( assignNode );
                
                cycleAttached = true;
            }

            if ( cycleAttached )
            {
                attached = true;
            }
        }
        while ( cycleAttached );

        return attached;
    }

    /** Locate a <code>TupleSource</code> suitable for attaching
     *  the <code>FilterCondition</code>.
     *
     *  @param condition The <code>Condition</code> to attach.
     *  @param sources Candidate <code>TupleSources</code>.
     *
     *  @return Matching <code>TupleSource</code> if a suitable one
     *          can be found, else <code>null</code>.
     */
    protected TupleSource findMatchingTupleSourceForFiltering(FilterCondition condition,
                                                              Set sources)
    {
        Iterator    sourceIter = sources.iterator();
        TupleSource eachSource = null;

        Set decls = null;

        while ( sourceIter.hasNext() )
        {
            eachSource = (TupleSource) sourceIter.next();

            decls = eachSource.getTupleDeclarations();

            if ( matches( condition,
                          decls ) )
            {
                return eachSource;
            }
        }

        return null;
    }

    /** Locate a <code>TupleSource</code> suitable for attaching
     *  the <code>AssignmentCondition</code>.
     *
     *  @param condition The <code>Condition</code> to attach.
     *  @param sources Candidate <code>TupleSources</code>.
     *
     *  @return Matching <code>TupleSource</code> if a suitable one
     *          can be found, else <code>null</code>.
     */
    protected TupleSource findMatchingTupleSourceForAssignment(AssignmentCondition condition,
                                                               Set sources)
    {
        Declaration targetDecl = condition.getTargetDeclaration();

        Iterator    sourceIter = sources.iterator();
        TupleSource eachSource = null;

        Set decls = null;

        while ( sourceIter.hasNext() )
        {
            eachSource = (TupleSource) sourceIter.next();

            decls = eachSource.getTupleDeclarations();
            // System.err.println( "decls -> " + decls );

            if ( decls.contains( targetDecl ) )
            {
                continue;
            }

            if ( matches( condition,
                          decls ) )
            {
                return eachSource;
            }
        }

        return null;
    }

    /** Determine if a set of <code>Declarations</code> match those
     *  required by a <code>Condition</code>.
     *
     *  @param condition The <code>Condition</code>.
     *  @param declarations The set of <code>Declarations</code> to compare against.
     *
     *  @return <code>true</code> if the set of <code>Declarations</code> is a
     *          super-set of the <code>Declarations</code> required by the
     *          <code>Condition</code>.
     */
    protected boolean matches(Condition condition,
                              Set declarations)
    {
        Declaration[] requiredDecls = condition.getRequiredTupleMembers();

        for ( int i = 0 ; i < requiredDecls.length ; ++i )
        {
            if ( ! declarations.contains( requiredDecls[i] ) )
            {
                return false;
            }
        }

        return true;
    }
                                                  
}
