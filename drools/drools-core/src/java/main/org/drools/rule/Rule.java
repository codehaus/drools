package org.drools.rule;

/*
 $Id: Rule.java,v 1.10 2002-08-19 00:31:42 bob Exp $

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
import java.util.Iterator;
import java.util.Collections;

/** A set of <code>Condition</code>s and a <code>Consequence</code>.
 *
 *  The <code>Conditions</code> describe the circumstances
 *  that represent a match for this rule.  The <code>Consequence</code>
 *  gets fired when the <code>Conditions</code> match.
 *
 *  @see Condition
 *  @see Consequence
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 *
 *  @version $Id: Rule.java,v 1.10 2002-08-19 00:31:42 bob Exp $
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

    /** Extractions */
    private Set extractions;

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
        this.extractions           = Collections.EMPTY_SET;
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
                   getExtractions().isEmpty() );
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
             getExtractions().isEmpty() )
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
    public void addDeclaration(Declaration declaration)
    {
        if ( this.allDeclarations == Collections.EMPTY_SET )
        {
            this.allDeclarations  = new HashSet();
        }

        this.allDeclarations.add( declaration );
    }

    /** Retrieve a <code>Declaration</code> by identifier.
     *
     *  @param identifier The identifier.
     *
     *  @return The declaration or <code>null</code> if no
     *          declaration matches the <code>identifier</code>.
     */
    public Declaration getDeclaration(String identifier)
    {
        Iterator    declIter = this.allDeclarations.iterator();
        Declaration eachDecl = null;

        while ( declIter.hasNext() )
        {
            eachDecl = (Declaration) declIter.next();

            if ( eachDecl.getIdentifier().equals( identifier ) )
            {
                return eachDecl;
            }
        }

        return null;
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

    /** Add a consistent <code>Extraction</code> to this rule.
     *
     *  @param extraction the <code>Extraction</code> to add.
     */
    public void addExtraction(Extraction extraction)
    {
        if ( this.extractions == Collections.EMPTY_SET )
        {
            this.extractions = new HashSet();
        }

        this.extractions.add( extraction );

        Declaration decl = extraction.getTargetDeclaration();

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

    /** Retrieve the <code>Set</code> of <code>Extractions</code> for
     *  this rule.
     *
     *  @return The <code>Set</code> of <code>Extractions</code>.
     */
    public Set getExtractions()
    {
        return this.extractions;
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
            + "; extractions=" + this.extractions
            + "]";
    }

}
