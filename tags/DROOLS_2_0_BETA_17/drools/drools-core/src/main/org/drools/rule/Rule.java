package org.drools.rule;

/*
 $Id: Rule.java,v 1.30 2004-07-13 17:19:41 dbarnett Exp $

 Copyright 2001-2003 (C) The Werken Company. All Rights Reserved.

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
    permission of The Werken Company. "drools" is a trademark of
    The Werken Company.

 5. Due credit should be given to The Werken Company.
    (http://werken.com/)

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
import org.drools.spi.Duration;

import java.util.Arrays;
import java.util.Set;
import java.util.HashSet;
import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Collections;

import java.io.Serializable;

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
 *  @version $Id: Rule.java,v 1.30 2004-07-13 17:19:41 dbarnett Exp $
 */
public class Rule implements Serializable
{
    // ------------------------------------------------------------
    //     Constants
    // ------------------------------------------------------------

    /** Empty <code>Rule</code> array. */
    public static final Rule[] EMPTY_ARRAY = new Rule[0];

    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Name of the rule. */
    private String name;

    /** Documentation. */
    private String documentation;

    /** Salience value. */
    private int salience;

    /** All declarations. */
    private Set allDeclarations;

    /** Formal parameter decls of the rule. */
    private Set parameterDeclarations;

    /** Conditions. */
    private List conditions;

    /** Extractions */
    private Set extractions;

    /** Consequence. */
    private Consequence consequence;

    /** Truthness duration. */
    private Duration duration;

    /** Load order in RuleSet*/
    private long loadOrder;

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

        this.conditions            = Collections.EMPTY_LIST;
        this.extractions           = Collections.EMPTY_SET;
    }

    /** Set the documentation.
     *
     *  @param documentation The documentation.
     */
    public void setDocumentation(String documentation)
    {
        this.documentation = documentation;
    }

    /** Retrieve the documentation.
     *
     *  @return The documentation or <code>null</code> if none.
     */
    public String getDocumentation()
    {
        return this.documentation;
    }

    /** Set the truthness duration.
     *
     *  <p>
     *  This is merely a convenience method for calling
     *  {@link #setDuration(Duration)} with a <code>FixedDuration</code>.
     *  </p>
     *
     *  @see #setDuration(Duration)
     *  @see FixedDuration
     *
     *  @param seconds The number of seconds the rule
     *         must hold true in order to fire.
     */
    public void setDuration(long seconds)
    {
        this.duration = new FixedDuration( seconds );
    }

    /** Set the truthness duration object.
     *
     *  @param duration The truth duration object.
     */
    public void setDuration(Duration duration)
    {
        this.duration = duration;
    }

    /** Retrieve the truthness duration object.
     *
     *  @return The truthness duration object.
     */
    public Duration getDuration()
    {
        return this.duration;
    }

    /** Determine if this rule is internally consistent and valid.
     *
     *  @return <code>true</code> if this rule is valid, else <code>false</code>.
     */
    public boolean isValid()
    {
        return ( ! ( getParameterDeclarations().length == 0
                     ||
                     getConditions().length == 0
                     ||
                     getExtractions().length == 0 )
                 &&
                 getConsequence() != null );
    }

    /** Check the validity of this rule, and throw exceptions if it fails validity tests.
     *
     *  <p>
     *  Possibly exceptions include:
     *  </p>
     *
     *  <pre>
     *      NoParameterDeclarationException
     *  </pre>
     *
     *  <p>
     *  A <code>Rule</code> must include at least one
     *  parameter declaration and one condition.
     *  </p>
     *
     *  @throws InvalidRuleException if this rule is in any way invalid.
     */
    public void checkValidity()
        throws InvalidRuleException
    {
        if ( getParameterDeclarations().length == 0 )
        {
            throw new NoParameterDeclarationException( this );
        }
        if ( getConditions().length == 0 )
        {
            throw new NoConditionException( this );
        }
        if ( getConsequence() == null )
        {
            throw new NoConsequenceException( this );
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

    /** Retrieve the <code>Rule</code> salience.
     *
     *  @return The salience.
     */
    public int getSalience()
    {
        return this.salience;
    }

    /** Set the <code>Rule<code> salience.
     *
     *  @param salience The salience.
     */
    public void setSalience(int salience)
    {
        this.salience = salience;
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

    /** Retrieve a parameter <code>Declaration</code> by identifier.
     *
     *  @param identifier The identifier.
     *
     *  @return The declaration or <code>null</code> if no
     *          declaration matches the <code>identifier</code>.
     */
    public Declaration getParameterDeclaration(String identifier)
    {
        Iterator    declIter = this.parameterDeclarations.iterator();
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
    public Declaration[] getParameterDeclarations()
    {
        return (Declaration[]) this.parameterDeclarations.toArray( Declaration.EMPTY_ARRAY );
    }

    /** Retrieve the set of all implied local Declarations.
     *
     *  @return The <code>Set</code> of all implied <code>Declarations</code>
     *          which are implied by the conditions.
     */
    public Declaration[] getLocalDeclarations()
    {
        Set localDecls = new HashSet( this.allDeclarations );

        localDecls.removeAll( this.parameterDeclarations );

        return (Declaration[]) localDecls.toArray( Declaration.EMPTY_ARRAY );
    }

    /** Retrieve the array of all <code>Declaration</code>s of
     *  this rule.
     *
     *  @return The array of declarations.
     */
    public Declaration[] getAllDeclarations()
    {
        return (Declaration[]) this.allDeclarations.toArray( Declaration.EMPTY_ARRAY );
    }

    /** Add a <code>Condition</code> to this rule.
     *
     *  @param condition The <code>Condition</code> to add.
     */
    public void addCondition(Condition condition)
    {
        if ( this.conditions == Collections.EMPTY_LIST )
        {
            this.conditions = new ArrayList();
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
    public Condition[] getConditions()
    {
        return (Condition[]) this.conditions.toArray( Condition.EMPTY_ARRAY );
    }

    /** Retrieve the <code>Set</code> of <code>Extractions</code> for
     *  this rule.
     *
     *  @return The <code>Set</code> of <code>Extractions</code>.
     */
    public Extraction[] getExtractions()
    {
        return (Extraction[]) this.extractions.toArray( Extraction.EMPTY_ARRAY );
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

    public long getLoadOrder()
    {
      return loadOrder;
    }

    void setLoadOrder(long loadOrder)
    {
      this.loadOrder = loadOrder;
    }

    public String dump(String indent)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(indent + "Rule\n");
        buffer.append(indent + "----\n");
        buffer.append(indent + "name: ");
        buffer.append(this.name);
        buffer.append("\n");
        buffer.append(indent + "salience: ");
        buffer.append(this.salience);
        buffer.append("\n");
        buffer.append(indent + "load order: ");
        buffer.append(this.loadOrder);
        buffer.append("\n");
        buffer.append(indent + "duration: ");
        buffer.append(this.duration);
        buffer.append("\n");
        Declaration[] declarations = getAllDeclarations();
        for (int i = 0; i < declarations.length; i++)
        {
          buffer.append(declarations[i].dump(indent + " "));
        }

        Extraction[] extractions = getExtractions();
        for (int i = 0; i < extractions.length; i++)
        {
          buffer.append(indent + "extraction:\n");
          buffer.append(extractions[i].dump(indent + " "));
        }

        Condition[] conditions = getConditions();
        for (int i = 0; i < conditions.length; i++)
        {
          buffer.append(indent + "condition:\n");
          buffer.append(indent + conditions[i] + "\n");
        }

        buffer.append(indent + "consequence:\n");
        buffer.append(indent + this.consequence);
        buffer.append("\n");
        return buffer.toString();
    }
    
    /**
     * Compatible with the GraphViz DOT format.
     */
    public long dumpToDot(StringBuffer buffer, long thisNode) {
        buffer.append(thisNode + " [label=\"Rule\\n" +
            "name: " + name + "\\n" +
            "salience: " + salience + "\\n" +
            "load order: " + loadOrder + "\\n" +
            "duration: " + duration + "\\n");
        for (Iterator i = Arrays.asList(getConditions()).iterator(); i.hasNext(); )
        {
            buffer.append("condition: " + i.next() + "\\n");
        }
        buffer.append("consequence: " + consequence + "\"];\n");
        
        long nextNode = thisNode + 1;

        for (Iterator i = Arrays.asList(getAllDeclarations()).iterator(); i.hasNext(); )
        {
            buffer.append(thisNode + " -> " + nextNode + ";\n");
            nextNode = ((Declaration) i.next()).dumpToDot(buffer, nextNode);
        }

        for (Iterator i = Arrays.asList(getExtractions()).iterator(); i.hasNext(); )
        {
            buffer.append(thisNode + " -> " + nextNode + ";\n");
            nextNode = ((Extraction) i.next()).dumpToDot(buffer, nextNode);
        }
        
        return nextNode;
    }
}
