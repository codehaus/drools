
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

public class Builder
{
    private RootNode rootNode;

    public Builder(RootNode rootNode)
    {
        this.rootNode = rootNode;
    }

    public RootNode getRootNode()
    {
        return this.rootNode;
    }

    public void addRule(Rule rule) throws ReteConstructionException
    {
        Set assignmentConds = new HashSet( rule.getAssignmentConditions() );
        Set filterConds     = new HashSet( rule.getFilterConditions() );

        Set attachableNodes = null;

        boolean performedJoin     = false;
        boolean attachAssign      = false;
        boolean cycleAttachAssign = false;
        
        attachableNodes = createParameterNodes( rule );

        do 
        {
            performedJoin = false;
            attachAssign  = false;

            if ( ! filterConds.isEmpty() )
            {
                attachFilterConditions( filterConds,
                                        attachableNodes );
            }

            attachAssign = attachAssignmentConditions( assignmentConds,
                                                       attachableNodes );

            performedJoin = createJoinNodes( attachableNodes );
        }
        while ( ! attachableNodes.isEmpty() 
                &&
                ( performedJoin
                  ||
                  attachAssign
                  ) );
    }

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

    protected boolean createJoinNodes(Set attachableNodes)
    {
        boolean performedJoin = false;

        Object[] leftNodes  = attachableNodes.toArray();
        Object[] rightNodes = attachableNodes.toArray();

        TupleSource left  = null;
        TupleSource right = null;

        JoinNode joinNode = null;

        for ( int i = 0 ; i < leftNodes.length ; ++i )
        {
            left = (TupleSource) leftNodes[i];

            if ( ! attachableNodes.contains( left ) )
            {
                continue;
            }

            for ( int j = i ; j < rightNodes.length ; ++i )
            {
                right = (TupleSource) rightNodes[i];

                if ( ! attachableNodes.contains( right ) )
                {
                    continue;
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
                }
            }
        }

        return performedJoin;
    }

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

    protected TupleSource findMatchingTupleSourceForFiltering(Condition condition,
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
