package org.drools.reteoo;

/*
 * $Id: Builder.java,v 1.67 2004-12-06 15:36:14 simon Exp $
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
import org.drools.RuleSetIntegrationException;
import org.drools.conflict.DefaultConflictResolver;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.spi.Condition;
import org.drools.spi.ConflictResolver;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Builds the Rete-OO network for a <code>RuleSet</code>.
 *
 * @see org.drools.rule.RuleSet
 *
 * @author <a href="mailto:bob@werken.com">bob mcwhirter </a>
 *
 * TODO Make joinForCondition actually be intelligent enough to build optimal
 * joins. Currently using forgy's original description of 2-input nodes, which I
 * feel (but don't know for sure, is sub-optimal.
 */
public class Builder
{
    // ------------------------------------------------------------
    // Instance members
    // ------------------------------------------------------------

    /** Rete network to build against. */
    private Rete rete;

    /** Rule-sets added. */
    private List ruleSets;

    private Map applicationData;

    private FactHandleFactory factHandleFactory;

    private ConflictResolver conflictResolver;

    // ------------------------------------------------------------
    // Constructors
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
    // Instance methods
    // ------------------------------------------------------------

    /**
     * Build the <code>RuleBase</code>.
     *
     * @return The rule-base.
     */
    public RuleBase buildRuleBase()
    {
        RuleBase ruleBase = new RuleBaseImpl( this.rete,
                                              this.conflictResolver,
                                              this.factHandleFactory,
                                              this.ruleSets,
                                              this.applicationData);

        reset( );

        return ruleBase;
    }

    /**
     * Set the <code>FactHandleFactory</code>.
     *
     * @param factHandleFactory The fact handle factory.
     */
    public void setFactHandleFactory(FactHandleFactory factHandleFactory)
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
     * @throws RuleIntegrationException
     *             if an error prevents complete construction of the network for
     *             the <code>Rule</code>.
     */
    public void addRuleSet(RuleSet ruleSet)
            throws RuleIntegrationException, RuleSetIntegrationException
    {
        this.ruleSets.add( ruleSet );

        Map newApplicationData = ruleSet.getApplicationData( );
        Iterator it = newApplicationData.keySet( ).iterator( );
        String identifier;
        Class type;
        while ( it.hasNext( ) )
        {
            identifier = ( String ) it.next( );
            type = ( Class ) newApplicationData.get( identifier );
            if ( this.applicationData.containsKey( identifier ) && !this.applicationData.get( identifier ).equals( type ) )
            {
                throw new RuleSetIntegrationException( ruleSet );
            }

        }
        this.applicationData.putAll( newApplicationData );

        Rule[] rules = ruleSet.getRules( );

        for ( int i = 0; i < rules.length; ++i )
        {
            addRule( rules[i] );
        }
    }

    /**
     * Add a <code>Rule</code> to the network.
     *
     * @param rule The rule to add.
     *
     * @throws RuleIntegrationException
     *             if an error prevents complete construction of the network for
     *             the <code>Rule</code>.
     */
    protected void addRule(Rule rule) throws RuleIntegrationException
    {
        List conds = new LinkedList( rule.getConditions( ) );
        List leafNodes = createParameterNodes( rule );

        boolean performedJoin;
        boolean joinedForCondition;

        while ( true )
        {
            joinedForCondition = false;

            if ( !conds.isEmpty( ) )
            {
                attachConditions( rule,
                                  conds,
                                  leafNodes );
            }

            performedJoin = createJoinNodes( leafNodes );

            if ( !performedJoin && !conds.isEmpty( ) )
            {
                joinedForCondition = joinForCondition( conds,
                                                       leafNodes );
            }

            if ( joinedForCondition )
            {
                continue;
            }

            if ( leafNodes.size( ) > 1 )
            {
                if ( !performedJoin )
                {
                    joinArbitrary( leafNodes );
                }
            }
            else
            {
                break;
            }
        }

        if ( leafNodes.size( ) != 1 )
        {
            throw new RuleIntegrationException( rule );
        }

        TupleSource lastNode = (TupleSource) leafNodes.iterator( ).next( );

        TerminalNode terminal = new TerminalNode( lastNode,
                                                  rule );
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
    List createParameterNodes(Rule rule)
    {
        List leafNodes = new LinkedList( );
        Iterator declIter = rule.getParameterDeclarations( ).iterator( );

        Declaration eachDecl;
        while ( declIter.hasNext( ) )
        {
            eachDecl = (Declaration) declIter.next( );

            ParameterNode parameterNode = new ParameterNode( this.rete.getOrCreateObjectTypeNode( eachDecl.getObjectType( ) ),
                                                             eachDecl );

            parameterNode.attach( );

            leafNodes.add( parameterNode );
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
     * @param rule The rule.
     * @param conds Set of <code>Conditions</code> to attempt attaching.
     * @param leafNodes The leaf node.
     */
    private void attachConditions( Rule rule,
                                   List conds,
                                   List leafNodes )
    {
        Iterator condIter = conds.iterator( );
        Condition eachCond;
        TupleSource tupleSource;

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

            leafNodes.add( new ConditionNode( rule,
                                              tupleSource,
                                              eachCond ) );
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
    private boolean joinForCondition(List conds,
                                     List leafNodes)
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
    private boolean joinArbitrary(List leafNodes)
    {
        Iterator leafIter = leafNodes.iterator( );

        TupleSource left = (TupleSource) leafIter.next( );

        if ( !leafIter.hasNext( ) )
        {
            return false;
        }

        leafIter.remove( );

        TupleSource right = (TupleSource) leafIter.next( );

        leafIter.remove( );

        leafNodes.add( new JoinNode( left,
                                     right ) );

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
    private boolean createJoinNodes(List leafNodes)
    {
        boolean performedJoin = false;

        Object[] nodesArray = leafNodes.toArray( );

        TupleSource left;
        TupleSource right;

        for ( int i = 0; i < nodesArray.length; ++i )
        {
            left = (TupleSource) nodesArray[i];

            if ( leafNodes.contains( left ) )
            {
                for ( int j = i + 1; j < nodesArray.length; ++j )
                {
                    right = (TupleSource) nodesArray[j];

                    if ( leafNodes.contains( right ) && canBeJoined( left,
                                                                     right ) )
                    {
                        leafNodes.remove( left );
                        leafNodes.remove( right );

                        leafNodes.add( new JoinNode( left,
                                                     right ) );

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
    private boolean canBeJoined(TupleSource left,
                                TupleSource right)
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
     * Locate a <code>TupleSource</code> suitable for attaching the
     * <code>Condition</code> and remove it.
     *
     * @param condition The <code>Condition</code> to attach.
     * @param sources Candidate <code>TupleSources</code>.
     *
     * @return Matching <code>TupleSource</code> if a suitable one can be
     *         found, else <code>null</code>.
     */
    TupleSource findMatchingTupleSourceForCondition(Condition condition,
                                                    List sources)
    {
        Iterator sourceIter = sources.iterator( );
        TupleSource eachSource;

        while ( sourceIter.hasNext( ) )
        {
            eachSource = (TupleSource) sourceIter.next( );

            if ( matches( condition,
                          eachSource.getTupleDeclarations( ) ) )
            {
                sourceIter.remove( );
                return eachSource;
            }
        }

        return null;
    }

    /**
     * Determine if a set of <code>Declarations</code> match those required by
     * a <code>Condition</code>.
     *
     * @param condition
     *            The <code>Condition</code>.
     * @param declarations
     *            The set of <code>Declarations</code> to compare against.
     *
     * @return <code>true</code> if the set of <code>Declarations</code> is
     *         a super-set of the <code>Declarations</code> required by the
     *         <code>Condition</code>.
     */
    boolean matches(Condition condition,
                    Set declarations)
    {
        return containsAll( declarations,
                            condition.getRequiredTupleMembers( ) );
    }

    /**
     * Determine if a set of <code>Declarations</code> is a super set of
     * required <code>Declarations</code>
     *
     * @param declarations
     *            The set of <code>Declarations</code> to compare against.
     *
     * @param requiredDecls
     *            The required <code>Declarations</code>.
     * @return <code>true</code> if the set of <code>Declarations</code> is
     *         a super-set of the <code>Declarations</code> required by the
     *         <code>Condition</code>.
     */
    private boolean containsAll(Set declarations,
                                Declaration[] requiredDecls)
    {
        for ( int i = requiredDecls.length - 1; i >= 0; i-- )
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
        this.rete = new Rete( );
        this.ruleSets = new ArrayList( );
        this.applicationData = new HashMap( );
        this.factHandleFactory = new DefaultFactHandleFactory( );
        this.conflictResolver = DefaultConflictResolver.getInstance( );
    }
}
