package org.drools.reteoo;

/*
 * $Id: Builder.java,v 1.47 2004-11-03 02:35:15 simon Exp $
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

import org.drools.RuleBase;
import org.drools.RuleIntegrationException;
import org.drools.conflict.DefaultConflictResolver;
import org.drools.rule.Declaration;
import org.drools.rule.Extraction;
import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.spi.Condition;
import org.drools.spi.ConflictResolver;
import org.drools.spi.ObjectType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * Builds the Rete-OO network for a <code>RuleSet</code>.
 *
 * @see org.drools.rule.RuleSet
 *
 * @author <a href="mailto:bob@werken.com">bob mcwhirter </a>
 *
 * @task Make joinForCondition actually be intelligent enough to build optimal
 *       joins. Currently using forgy's original description of 2-input nodes,
 *       which I feel (but don't know for sure, is sub-optimal.
 */
public class Builder
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Rete network to build against. */
    private Rete                rete;

    /** Rule-sets added. */
    private List                ruleSets;

    private FactHandleFactory   factHandleFactory;

    private ConflictResolver    conflictResolver;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /**
     * Construct a <code>Builder</code> against an existing <code>Rete</code>
     * network.
     */
    public Builder()
    {
        reset( );
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /**
     * Build the <code>RuleBase</code>.
     *
     * @return The rule-base.
     */
    public RuleBase buildRuleBase()
    {
        RuleBase ruleBase = new RuleBaseImpl( this.rete, this.conflictResolver, this.factHandleFactory );

        reset();

        return ruleBase;
    }

    /**
     * Set the <code>FactHandleFactory</code>.
     *
     * @param factHandleFactory The fact handle factory.
     */
    public void setFactHandleFactory( FactHandleFactory factHandleFactory )
    {
        this.factHandleFactory = factHandleFactory;
    }

    /**
     * Set the <code>ConflictResolver</code>.
     *
     * @param conflictResolver The conflict resolver.
     */
    public void setConflictResolver(ConflictResolver conflictResolver)
    {
        this.conflictResolver = conflictResolver;
    }

    /**
     * Add a <code>RuleSet</code> to the network.
     *
     * @param ruleSet The rule-set to add.
     *
     * @throws RuleIntegrationException if an error prevents complete
     *         construction of the network for the <code>Rule</code>.
     */
    public void addRuleSet(RuleSet ruleSet) throws RuleIntegrationException
    {
        this.ruleSets.add( ruleSet );

        Rule[] rules = ruleSet.getRules( );

        for ( int i = 0; i < rules.length; ++i )
        {
            addRule( ruleSet, rules[i] );
        }
    }

    /**
     * Add a <code>Rule</code> to the network.
     *
     * @param ruleSet The rule-set containing the rule.
     * @param rule The rule to add.
     *
     * @throws RuleIntegrationException if an error prevents complete
     *         construction of the network for the <code>Rule</code>.
     */
    protected void addRule(RuleSet ruleSet, Rule rule) throws RuleIntegrationException
    {
        Set factExtracts = new HashSet( Arrays.asList( rule.getExtractions( ) ) );
        List conds = new ArrayList( Arrays.asList( rule.getConditions( ) ) );

        Set leafNodes;

        boolean performedJoin;
        boolean attachedExtract;
        boolean joinedForCondition;

        leafNodes = createParameterNodes( rule );

        while ( true )
        {
            joinedForCondition = false;

            if ( !conds.isEmpty( ) )
            {
                attachConditions( conds, leafNodes );
            }

            attachedExtract = attachExtractions( factExtracts, leafNodes );

            performedJoin = createJoinNodes( leafNodes );

            if ( !performedJoin && !attachedExtract && !conds.isEmpty( ) )
            {
                joinedForCondition = joinForCondition( conds, leafNodes );
            }

            if ( joinedForCondition )
            {
                continue;
            }

            if ( ( performedJoin || attachedExtract ) && leafNodes.size( ) > 1 )
            {
                continue;
            }

            if ( leafNodes.size( ) > 1 )
            {
                joinArbitrary( leafNodes );
            }
            else if ( !attachedExtract )
            {
                break;
            }
        }

        if ( leafNodes.size( ) != 1 )
        {
            throw new RuleIntegrationException( rule );
        }

        TupleSource lastNode = ( TupleSource ) leafNodes.iterator( ).next( );

        TerminalNode terminal = new TerminalNode( lastNode, rule );
    }

    /**
     * Create the <code>ParameterNode</code> s for the <code>Rule</code>,
     * and link into the network.
     *
     * @param rule The rule.
     *
     * @return A <code>Set</code> of <code>ParameterNodes</code> created and
     *         linked into the network.
     */
    Set createParameterNodes(Rule rule)
    {
        Set leafNodes = new HashSet( );

        Set parameterDecls = new HashSet( Arrays.asList( rule.getParameterDeclarations() ) );

        Iterator declIter = parameterDecls.iterator( );
        Declaration eachDecl;

        ObjectType objectType;
        ObjectTypeNode objectTypeNode;
        ParameterNode paramNode;

        while ( declIter.hasNext( ) )
        {
            eachDecl = ( Declaration ) declIter.next( );

            objectType = eachDecl.getObjectType( );

            objectTypeNode = this.rete.getOrCreateObjectTypeNode( objectType );

            paramNode = new ParameterNode( rule, objectTypeNode, eachDecl );

            leafNodes.add( paramNode );

        }

        return leafNodes;
    }

    /**
     * Create and attach <code>Condition</code> s to the network.
     *
     * <p>
     * It may not be possible to satisfy all filder conditions on the first
     * pass. This method removes satisfied conditions from the
     * <code>Condition</code> parameter, and leaves unsatisfied ones in the
     * <code>Set</code>.
     * </p>
     *
     * @param conds Set of <code>Conditions</code> to attempt attaching.
     * @param leafNodes The current attachable leaf nodes of the network.
     */
    void attachConditions(List conds, Set leafNodes)
    {
        Iterator condIter = conds.iterator( );
        Condition eachCond;
        TupleSource tupleSource;

        ConditionNode conditionNode;
        int order = 0;
        while ( condIter.hasNext( ) )
        {
            eachCond = ( Condition ) condIter.next( );

            tupleSource = findMatchingTupleSourceForCondition( eachCond,
                                                               leafNodes );

            if ( tupleSource == null )
            {
                continue;
            }

            condIter.remove( );

            conditionNode = new ConditionNode( tupleSource, eachCond, order );

            leafNodes.remove( tupleSource );
            leafNodes.add( conditionNode );
        }
    }

    /**
     * Join two arbitrary leaves in order to satisfy a filter that currently
     * cannot be applied.
     *
     * @param conds The filter conditions remaining.
     * @param leafNodes Available leaf nodes.
     *
     * @return <code>true</code> if a join was possible, otherwise,
     *         <code>false</code>.
     */
    boolean joinForCondition(List conds, Set leafNodes)
    {
        return joinArbitrary( leafNodes );
    }

    /**
     * Join two arbitrary leaves in order to satisfy a filter that currently
     * cannot be applied.
     *
     * @param leafNodes Available leaf nodes.
     *
     * @return <code>true</code> if successfully joined some nodes, otherwise
     *         <code>false</code>.
     */
    boolean joinArbitrary(Set leafNodes)
    {
        Iterator leafIter = leafNodes.iterator( );

        TupleSource left = ( TupleSource ) leafIter.next( );

        if ( !leafIter.hasNext( ) )
        {
            return false;
        }

        leafIter.remove( );

        TupleSource right = ( TupleSource ) leafIter.next( );

        leafIter.remove( );

        JoinNode joinNode = new JoinNode( left, right );

        leafNodes.add( joinNode );

        return true;
    }

    /**
     * Create and attach <code>JoinNode</code> s to the network.
     *
     * <p>
     * It may not be possible to join all <code>leafNodes</code>.
     * </p>
     *
     * <p>
     * Any <code>leafNodes</code> member that particiates in a <i>join </i> is
     * removed from the <code>leafNodes</code> collection, and replaced by the
     * joining <code>JoinNode</code>.
     * </p>
     *
     * @param leafNodes The current attachable leaf nodes of the network.
     *
     * @return <code>true</code> if at least one <code>JoinNode</code> was
     *         created, else <code>false</code>.
     */
    boolean createJoinNodes(Set leafNodes)
    {
        boolean performedJoin = false;

        Object[] nodesArray = leafNodes.toArray( );

        TupleSource left;
        TupleSource right;

        for ( int i = 0; i < nodesArray.length; ++i )
        {
            left = ( TupleSource ) nodesArray[i];

            if ( leafNodes.contains( left ) )
            {
                for ( int j = i + 1; j < nodesArray.length; ++j )
                {
                    right = ( TupleSource ) nodesArray[j];

                    if ( leafNodes.contains( right ) && canBeJoined( left, right ) )
                    {
                        leafNodes.remove( left );
                        leafNodes.remove( right );

                        leafNodes.add( new JoinNode( left, right ) );

                        performedJoin = true;

                        break;
                    }
                }
            }
        }

        return performedJoin;
    }

    /**
     * Determine if two <code>TupleSource</code> s can be joined.
     *
     * @param left The left tuple source
     * @param right The right tuple source
     *
     * @return <code>true</code> if they can be joined (they share at least
     *         one common member declaration), else <code>false</code>.
     */
    boolean canBeJoined(TupleSource left, TupleSource right)
    {
        Set leftDecls = left.getTupleDeclarations( );
        Iterator rightDeclIter = right.getTupleDeclarations( ).iterator( );

        while ( rightDeclIter.hasNext( ) )
        {
            if ( leftDecls.contains( rightDeclIter.next( ) ) )
            {
                return true;
            }
        }

        return false;
    }

    /**
     * Create and attach <code>Extraction</code> s to the network.
     *
     * <p>
     * It may not be possible to satisfy all <code>Extraction</code>, in
     * which case, unsatisfied conditions will remain in the <code>Set</code>
     * passed in as <code>Extraction</code>.
     * </p>
     *
     * @param factExtracts Set of <code>Extractions</code> to attach to the
     *        network.
     * @param leafNodes The current attachable leaf nodes of the network.
     *
     * @return <code>true</code> if fact extractions have been attached,
     *         otherwise <code>false</code>.
     */
    boolean attachExtractions(Set factExtracts, Set leafNodes)
    {
        boolean attached = false;
        boolean cycleAttached;

        do
        {
            cycleAttached = false;

            Iterator extractIter = factExtracts.iterator( );
            Extraction eachExtract;
            TupleSource tupleSource;

            ExtractionNode extractNode;
            while ( extractIter.hasNext( ) )
            {
                eachExtract = ( Extraction ) extractIter.next( );

                tupleSource = findMatchingTupleSourceForExtraction(
                                                                    eachExtract,
                                                                    leafNodes );

                if ( tupleSource == null )
                {
                    continue;
                }

                extractIter.remove( );

                extractNode = new ExtractionNode(
                                                  tupleSource,
                                                  eachExtract
                                                             .getTargetDeclaration( ),
                                                  eachExtract.getExtractor( ) );

                leafNodes.remove( tupleSource );
                leafNodes.add( extractNode );

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

    /**
     * Locate a <code>TupleSource</code> suitable for attaching the
     * <code>Condition</code>.
     *
     * @param condition The <code>Condition</code> to attach.
     * @param sources Candidate <code>TupleSources</code>.
     *
     * @return Matching <code>TupleSource</code> if a suitable one can be
     *         found, else <code>null</code>.
     */
    TupleSource findMatchingTupleSourceForCondition(Condition condition,
                                                    Set sources)
    {
        Iterator sourceIter = sources.iterator( );
        TupleSource eachSource;

        while ( sourceIter.hasNext( ) )
        {
            eachSource = ( TupleSource ) sourceIter.next( );

            if ( matches( condition, eachSource.getTupleDeclarations( ) ) )
            {
                return eachSource;
            }
        }

        return null;
    }

    /**
     * Locate a <code>TupleSource</code> suitable for attaching the
     * <code>Extraction</code>.
     *
     * @param extract The <code>Extraction</code> to attach.
     * @param sources Candidate <code>TupleSources</code>.
     *
     * @return Matching <code>TupleSource</code> if a suitable one can be
     *         found, else <code>null</code>.
     */
    TupleSource findMatchingTupleSourceForExtraction(Extraction extract,
                                                     Set sources)
    {
        Declaration targetDecl = extract.getTargetDeclaration( );

        Iterator sourceIter = sources.iterator( );
        TupleSource eachSource;

        Set decls;

        while ( sourceIter.hasNext( ) )
        {
            eachSource = ( TupleSource ) sourceIter.next( );

            decls = eachSource.getTupleDeclarations( );

            if ( decls.contains( targetDecl ) )
            {
                continue;
            }

            if ( matches( extract, decls ) )
            {
                return eachSource;
            }
        }

        return null;
    }

    /**
     * Determine if a set of <code>Declarations</code> match those required by
     * a <code>Condition</code>.
     *
     * @param condition The <code>Condition</code>.
     * @param declarations The set of <code>Declarations</code> to compare
     *        against.
     *
     * @return <code>true</code> if the set of <code>Declarations</code> is
     *         a super-set of the <code>Declarations</code> required by the
     *         <code>Condition</code>.
     */
    boolean matches(Condition condition, Set declarations)
    {
        return matches( condition.getRequiredTupleMembers( ), declarations );
    }

    /**
     * Determine if a set of <code>Declarations</code> match those required by
     * a <code>Extraction</code>.
     *
     * @param extract The <code>Extraction</code>.
     * @param declarations The set of <code>Declarations</code> to compare
     *        against.
     *
     * @return <code>true</code> if the set of <code>Declarations</code> is
     *         a super-set of the <code>Declarations</code> required by the
     *         <code>Condition</code>.
     */
    boolean matches(Extraction extract, Set declarations)
    {
        return matches( extract.getRequiredTupleMembers( ), declarations );
    }

    /**
     * Determine if a set of <code>Declarations</code> is a super set of
     * required <code>Declarations</code>
     *
     * @param requiredDecls The required <code>Declarations</code>.
     * @param declarations The set of <code>Declarations</code> to compare
     *        against.
     *
     * @return <code>true</code> if the set of <code>Declarations</code> is
     *         a super-set of the <code>Declarations</code> required by the
     *         <code>Condition</code>.
     */
    boolean matches(Declaration[] requiredDecls, Set declarations)
    {
        for ( int i = 0; i < requiredDecls.length; ++i )
        {
            if ( !declarations.contains( requiredDecls[i] ) )
            {
                return false;
            }
        }

        return true;
    }

    /**
     * Reset the internal state.
     */
    private void reset()
    {
        this.rete = new Rete();
        this.ruleSets = new ArrayList();
        this.factHandleFactory = new DefaultFactHandleFactory();
        this.conflictResolver = DefaultConflictResolver.getInstance();
    }
}