package org.drools.rule;

/*
 $Id: Rule.java,v 1.6 2002-08-10 19:34:32 bob Exp $

 Copyright 2002 (C) The Werken Company. All Rights Reserved.
 
 Redistribution and use of this software and associated documentation
 ("Software"), with or without modification, are permitted provided
 that the following conditions are met:

 1. Redistributions of source code must retain copyright
    statements and notices.  Redistributions must also contain a
    copy of this document.
 
 2. Redistributions in binary form must reproduce the
    above copyright notice, this list of conditions and the
    following disclaimer in the documentation and/or other
    materials provided with the distribution.
 
 3. The name "drools" must not be used to endorse or promote
    products derived from this Software without prior written
    permission of The Werken Company.  For written permission,
    please contact bob@werken.com.
 
 4. Products derived from this Software may not be called "drools"
    nor may "drools" appear in their names without prior written
    permission of The Werken Company. "drools" is a registered
    trademark of The Werken Company.
 
 5. Due credit should be given to The Werken Company.
    (http://drools.werken.com/).
 
 THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE WERKEN COMPANY OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.
 
 */

import org.drools.spi.Condition;
import org.drools.spi.Consequence;

import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

/** A set of <code>Condition</code>s and an <code>Action</code>.
 *
 *  The <code>Conditions</code> describe the circumstances
 *  that represent a match for this rule.  The <code>Action</code>
 *  gets fired when the <code>Conditions</code> match.
 *
 *  @see Condition
 *  @see Action
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
public class Rule
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Name of the rule. */
    private String name;

    /** All declarations. */
    private Set allDeclarations;

    /** Formal parameter decls of the rule. */
    private Set parameterDeclarations;

    /** Conditions. */
    private Set conditions;

    /** Fact extractions */
    private Set factExtractions;

    /** Consequence. */
    private Consequence consequence;

    /** Truthness duration. */
    private long duration;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     *
     *  @param name The name of this rule.
     */
    public Rule(String name)
    {
        this.name  = name;

        this.parameterDeclarations = Collections.EMPTY_SET;
        this.allDeclarations       = Collections.EMPTY_SET;

        this.conditions            = Collections.EMPTY_SET;
        this.factExtractions       = Collections.EMPTY_SET;
    }

    /** Set the truthness duration.
     *
     *  @param seconds The number of seconds the rule
     *         must hold true in order to fire.
     */
    public void setDuration(long seconds)
    {
        this.duration = seconds;
    }

    /** Retrieve the truthness duration.
     *
     *  @return The number of seconds the rule must
     *          hold true in order to fire.
     */
    public long getDuration()
    {
        return this.duration;
    }

    /** Determine if this rule is internally consistent and valid.
     *
     *  @return <code>true</code> if this rule is valid, else <code>false</code>.
     */
    public boolean isValid()
    {
        return ! ( getParameterDeclarations().isEmpty()
                   ||
                   getConditions().isEmpty()
                   ||
                   getFactExtractions().isEmpty() );
    }


    /** Check the validity of this rule, and throw exceptions if
     *  it failed validity tests.
     *
     *  <p>
     *  Possibly exceptions include:
     *  </p>
     * 
     *  <pre>
     *      NoParameterDeclarationException
     *      NoConditionException
     *  </pre>
     *
     *  <p>
     *  A <code>Rule</code> must include at least one
     *  parameter declaration and one condition.
     *  </p>
     *
     *  @throws InvalidRuleException if this rule is in any way invalid.
     */
    public void checkValidity() throws InvalidRuleException
    {
        if ( getParameterDeclarations().isEmpty() )
        {
            throw new NoParameterDeclarationException( this );
        }

        if ( getConditions().isEmpty()
             &&
             getFactExtractions().isEmpty() )
        {
            throw new NoConditionException( this );
        }
    }

    /** Retrieve the name of this rule.
     *
     *  @return The name of this rule.
     */
    public String getName()
    {
        return this.name;
    }

    /** Add a <i>root fact object</i> parameter <code>Declaration</code>
     *  for this <code>Rule</code>.
     *
     *  @param declaration The <i>root fact object</i> <code>Declaration</code> to add.
     */
    public void addParameterDeclaration(Declaration declaration) 
    {
        if ( this.parameterDeclarations == Collections.EMPTY_SET )
        {
            this.parameterDeclarations = new HashSet();
        }
            
        this.parameterDeclarations.add( declaration );
        addDeclaration( declaration );
    }

    /** Add a declaration.
     *
     *  @param declaration The declaration.
     */
    private void addDeclaration(Declaration declaration)
    {
        if ( this.allDeclarations == Collections.EMPTY_SET )
        {
            this.allDeclarations  = new HashSet();
        }

        this.allDeclarations.add( declaration );
    }

    /** Retrieve the set of all <i>root fact object</i> parameter
     *  <code>Declarations</code>.
     *
     *  @return The <code>Set</code> of <code>Declarations</code> which
     *          specify the <i>root fact objects</i>.
     */
    public Set getParameterDeclarations()
    {
        return this.parameterDeclarations;
    }

    /** Retrieve the set of all implied local Declarations.
     *
     *  @return The <code>Set</code> of all implied <code>Declarations</code>
     *          which are implied by the conditions.
     */
    public Set getLocalDeclarations()
    {
        Set localDecls = new HashSet( this.allDeclarations );

        localDecls.removeAll( this.parameterDeclarations );

        return localDecls;
    }

    /** Add a <code>Condition</code> to this rule.
     *
     *  @param condition The <code>Condition</code> to add.
     */
    public void addCondition(Condition condition)
    {
        if ( this.conditions == Collections.EMPTY_SET )
        {
            this.conditions = new HashSet();
        }

        this.conditions.add( condition );
    }

    /** Add a consistent <code>FactExtraction</code> to this rule.
     *
     *  @param factExtraction the <code>FactExtraction</code> to add.
     */
    public void addFactExtraction(FactExtraction factExtraction)
    {
        if ( this.factExtractions == Collections.EMPTY_SET )
        {
            this.factExtractions = new HashSet();
        }

        this.factExtractions.add( factExtraction );

        Declaration decl = factExtraction.getTargetDeclaration();

        addDeclaration( decl );
    }

    /** Retrieve the <code>Set</code> of <code>Conditions</code> for
     *  this rule.
     *
     *  @return The <code>Set</code> of <code>Conditions</code>.
     */
    public Set getConditions()
    {
        return this.conditions;
    }

    /** Retrieve the <code>Set</code> of <code>FactExtractions</code> for
     *  this rule.
     *
     *  @return The <code>Set</code> of <code>FactExtractions</code>.
     */
    public Set getFactExtractions()
    {
        return this.factExtractions;
    }

    /** Set the <code>Consequence</code> that is associated with the
     *  successful match of this rule.
     *
     *  @param consequence The <code>Consequence</code> to attach to this <code>Rule</code>.
     */
    public void setConsequence(Consequence consequence)
    {
        this.consequence = consequence;
    }

    /** Retrieve the <code>Consequence</code> associated with this <code>Rule</code>.
     *
     *  @return The <code>Consequence</code>.
     */
    public Consequence getConsequence()
    {
        return this.consequence;
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
        return "[Rule: name='" + this.name
            + "'; paramDecls=" + this.parameterDeclarations
            + "; localDecls=" + getLocalDeclarations()
            + "; conds=" + this.conditions
            + "; factExtracts=" + this.factExtractions
            + "]";
    }

}
