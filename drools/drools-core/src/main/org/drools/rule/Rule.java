package org.drools.rule;

/*
 * $Id: Rule.java,v 1.45 2004-11-23 22:08:59 dbarnett Exp $
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

import org.drools.spi.Condition;
import org.drools.spi.Consequence;
import org.drools.spi.Duration;
import org.drools.spi.Extractor;
import org.drools.spi.ObjectType;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;

/**
 * A set of <code>Condition</code> s and a <code>Consequence</code>.
 * 
 * The <code>Conditions</code> describe the circumstances that represent a
 * match for this rule. The <code>Consequence</code> gets fired when the
 * <code>Conditions</code> match.
 * 
 * @see Condition
 * @see Consequence
 * 
 * @author <a href="mailto:bob@eng.werken.com">bob mcwhirter </a>
 * @author <a href="mailto:simon@redhillconsulting.com.au">Simon Harris </a>
 * 
 * TODO: Check for missing/duplicate declarations.
 */
public class Rule
    implements
    Serializable
{
    // ------------------------------------------------------------
    // Instance members
    // ------------------------------------------------------------

    /** Name of the rule. */
    private final String name;

    /** Documentation. */
    private String documentation;

    /** Salience value. */
    private int salience;

    /** All declarations. */
    private final SortedSet allDeclarations = new TreeSet( );

    /** The local declarations. */
    private final SortedSet localDeclarations = new TreeSet( );

    /** Formal parameter declarations. */
    private final SortedSet parameterDeclarations = new TreeSet( );

    /** Conditions. */
    private final List conditions = new ArrayList( );

    /** Extractions */
    private final Set extractions = new HashSet( );

    /** Consequence. */
    private Consequence consequence;

    /** Truthness duration. */
    private Duration duration;

    /** Load order in RuleSet */
    private long loadOrder;

    /** is the consequence of the rule currently being executed? */
    private boolean noLoop;

    private Set imports;

    // ------------------------------------------------------------
    // Constructors
    // ------------------------------------------------------------

    /**
     * Construct.
     * 
     * @param name
     *            The name of this rule.
     */
    public Rule(String name)
    {
        this.name = name;
        this.imports = Collections.EMPTY_SET;
    }

    /**
     * Set the documentation.
     * 
     * @param documentation
     *            The documentation.
     */
    public void setDocumentation(String documentation)
    {
        this.documentation = documentation;
    }

    /**
     * Retrieve the documentation.
     * 
     * @return The documentation or <code>null</code> if none.
     */
    public String getDocumentation()
    {
        return this.documentation;
    }

    /**
     * Set the truthness duration.
     * 
     * <p>
     * This is merely a convenience method for calling
     * {@link #setDuration(Duration)}with a <code>FixedDuration</code>.
     * </p>
     * 
     * @see #setDuration(Duration)
     * @see FixedDuration
     * 
     * @param seconds
     *            The number of seconds the rule must hold true in order to
     *            fire.
     */
    public void setDuration(long seconds)
    {
        this.duration = new FixedDuration( seconds );
    }

    /**
     * Set the truthness duration object.
     * 
     * @param duration
     *            The truth duration object.
     */
    public void setDuration(Duration duration)
    {
        this.duration = duration;
    }

    /**
     * Retrieve the truthness duration object.
     * 
     * @return The truthness duration object.
     */
    public Duration getDuration()
    {
        return this.duration;
    }

    /**
     * Determine if this rule is internally consistent and valid.
     * 
     * @return <code>true</code> if this rule is valid, else
     *         <code>false</code>.
     */
    public boolean isValid()
    {
        return !(getParameterDeclarations( ).isEmpty( ) || getConditions( ).isEmpty( ) || getExtractions( ).isEmpty( )) && getConsequence( ) != null;
    }

    /**
     * Check the validity of this rule, and throw exceptions if it fails
     * validity tests.
     * 
     * <p>
     * Possibly exceptions include:
     * </p>
     * 
     * <pre>
     * NoParameterDeclarationException
     * </pre>
     * 
     * <p>
     * A <code>Rule</code> must include at least one parameter declaration and
     * one condition.
     * </p>
     * 
     * @throws InvalidRuleException
     *             if this rule is in any way invalid.
     */
    public void checkValidity() throws InvalidRuleException
    {
        if ( getParameterDeclarations( ).isEmpty( ) )
        {
            throw new NoParameterDeclarationException( this );
        }
        if ( getConsequence( ) == null )
        {
            throw new NoConsequenceException( this );
        }
    }

    /**
     * Retrieve the name of this rule.
     * 
     * @return The name of this rule.
     */
    public String getName()
    {
        return this.name;
    }

    /**
     * Retrieve the <code>Rule</code> salience.
     * 
     * @return The salience.
     */
    public int getSalience()
    {
        return this.salience;
    }

    /**
     * Set the <code>Rule<code> salience.
     *
     *  @param salience The salience.
     */
    public void setSalience(int salience)
    {
        this.salience = salience;
    }

    public boolean getNoLoop()
    {
        return this.noLoop;
    }

    public void setNoLoop(boolean noLoop)
    {
        this.noLoop = noLoop;
    }

    /**
     * Add a declaration.
     * 
     * @param identifier
     *            The identifier.
     * @param objectType
     *            The type.
     */
    public Declaration addLocalDeclaration(String identifier,
                                           ObjectType objectType)
    {
        Declaration declaration = addDeclaration( identifier,
                                                  objectType );

        this.localDeclarations.add( declaration );

        return declaration;
    }

    /**
     * Add a <i>root fact object </i> parameter <code>Declaration</code> for
     * this <code>Rule</code>.
     * 
     * @param identifier
     *            The identifier.
     * @param objectType
     *            The type.
     * @return The declaration.
     */
    public Declaration addParameterDeclaration(String identifier,
                                               ObjectType objectType)
    {
        Declaration declaration = addDeclaration( identifier,
                                                  objectType );

        this.parameterDeclarations.add( declaration );

        return declaration;
    }

    /**
     * Retrieve a parameter <code>Declaration</code> by identifier.
     * 
     * @param identifier
     *            The identifier.
     * 
     * @return The declaration or <code>null</code> if no declaration matches
     *         the <code>identifier</code>.
     */
    public Declaration getParameterDeclaration(String identifier)
    {
        return getDeclaration( this.parameterDeclarations,
                               identifier );
    }

    /**
     * Add a consistent <code>Extraction</code> to this rule.
     * 
     * @param identifier
     *            The declaration identifier.
     * @param extractor
     *            The extractor.
     * @return extraction the <code>Extraction</code> to add.
     */
    public Extraction addExtraction(String identifier,
                                    Extractor extractor)
    {
        Extraction extraction = new Extraction( getDeclaration( identifier ),
                                                extractor );

        this.extractions.add( extraction );

        return extraction;
    }

    /**
     * Retrieve a <code>Declaration</code> by identifier.
     * 
     * @param identifier
     *            The identifier.
     * 
     * @return The declaration or <code>null</code> if no declaration matches
     *         the <code>identifier</code>.
     */
    public Declaration getDeclaration(String identifier)
    {
        return getDeclaration( this.allDeclarations,
                               identifier );
    }

    /**
     * Retrieve the set of all <i>root fact object </i> parameter
     * <code>Declarations</code>.
     * 
     * @return The <code>Set</code> of <code>Declarations</code> which
     *         specify the <i>root fact objects </i>.
     */
    public SortedSet getParameterDeclarations()
    {
        return Collections.unmodifiableSortedSet( this.parameterDeclarations );
    }

    /**
     * Retrieve the set of all implied local Declarations.
     * 
     * @return The <code>Set</code> of all implied <code>Declarations</code>
     *         which are implied by the conditions.
     */
    public SortedSet getLocalDeclarations()
    {
        return Collections.unmodifiableSortedSet( this.localDeclarations );
    }

    /**
     * Retrieve the array of all <code>Declaration</code> s of this rule.
     * 
     * @return The <code>Set</code> of all <code>Declarations</code>.
     */
    public SortedSet getAllDeclarations()
    {
        return Collections.unmodifiableSortedSet( this.allDeclarations );
    }

    /**
     * Add a <code>Condition</code> to this rule.
     * 
     * @param condition
     *            The <code>Condition</code> to add.
     */
    public void addCondition(Condition condition)
    {
        this.conditions.add( condition );
    }

    /**
     * Retrieve the <code>List</code> of <code>Conditions</code> for this
     * rule.
     * 
     * @return The <code>List</code> of <code>Conditions</code>.
     */
    public List getConditions()
    {
        return Collections.unmodifiableList( this.conditions );
    }

    public int getConditionSize()
    {
        return this.conditions.size( );
    }

    /**
     * Retrieve the <code>Set</code> of <code>Extractions</code> for this
     * rule.
     * 
     * @return The <code>Set</code> of <code>Extractions</code>.
     */
    public Set getExtractions()
    {
        return Collections.unmodifiableSet( this.extractions );
    }

    /**
     * Set the <code>Consequence</code> that is associated with the successful
     * match of this rule.
     * 
     * @param consequence
     *            The <code>Consequence</code> to attach to this
     *            <code>Rule</code>.
     */
    public void setConsequence(Consequence consequence)
    {
        this.consequence = consequence;
    }

    /**
     * Retrieve the <code>Consequence</code> associated with this
     * <code>Rule</code>.
     * 
     * @return The <code>Consequence</code>.
     */
    public Consequence getConsequence()
    {
        return this.consequence;
    }

    public long getLoadOrder()
    {
        return loadOrder;
    }

    void setLoadOrder(long loadOrder)
    {
        this.loadOrder = loadOrder;
    }

    public void setImports(Set imports)
    {
        this.imports = imports;
    }

    public Set getImports()
    {
        return this.imports;
    }

    public String dump(String indent)
    {
        StringBuffer buffer = new StringBuffer( );
        buffer.append( indent ).append( "Rule\n" );
        buffer.append( indent ).append( "----\n" );
        buffer.append( indent ).append( "name: " );
        buffer.append( this.name );
        buffer.append( "\n" );
        buffer.append( indent ).append( "salience: " );
        buffer.append( this.salience );
        buffer.append( "\n" );
        buffer.append( indent ).append( "load order: " );
        buffer.append( this.loadOrder );
        buffer.append( "\n" );
        buffer.append( indent ).append( "duration: " );
        buffer.append( this.duration );
        buffer.append( "\n" );

        for ( Iterator i = this.allDeclarations.iterator( ); i.hasNext( ); )
        {
            buffer.append( indent ).append( i.next( ) );
        }

        for ( Iterator i = this.extractions.iterator( ); i.hasNext( ); )
        {
            buffer.append( indent ).append( i.next( ) );
        }

        for ( Iterator i = this.conditions.iterator( ); i.hasNext( ); )
        {
            buffer.append( indent ).append( "condition:\n" ).append( indent ).append( i.next( ) ).append( '\n' );
        }

        buffer.append( indent ).append( "consequence:\n" );
        buffer.append( indent ).append( this.consequence );
        buffer.append( "\n" );
        return buffer.toString( );
    }

    private Declaration addDeclaration(String identifier,
                                       ObjectType objectType)
    {
        Declaration declaration = new Declaration( identifier,
                                                   objectType,
                                                   allDeclarations.size( ) );

        this.allDeclarations.add( declaration );

        return declaration;
    }

    private Declaration getDeclaration(Collection declarations,
                                       String identifier)
    {
        Declaration eachDecl;

        Iterator declIter = declarations.iterator( );
        while ( declIter.hasNext( ) )
        {
            eachDecl = (Declaration) declIter.next( );

            if ( eachDecl.getIdentifier( ).equals( identifier ) )
            {
                return eachDecl;
            }
        }

        return null;
    }
}