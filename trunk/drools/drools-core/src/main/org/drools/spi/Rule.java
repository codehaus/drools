
package org.drools.spi;

import java.util.Set;
import java.util.HashSet;
import java.util.Collections;

/** A set of {@link Condition}s and an {@link Action}.
 *
 *  The <code>Conditions</code> describe the circumstances
 *  that represent a match for this rule.  The <code>Action</code>
 *  gets fired when the <code>Conditions</code> match.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class Rule
{
    private static int STATE_DECL  = 1;
    private static int STATE_OTHER = 2;
    
    private int state;

    private String name;

    private Set    parameterDeclarations;
    private Set    localDeclarations;

    private Set    filterConditions;
    private Set    assignmentConditions;

    private Action action;

    /** Construct.
     *
     *  @param name The name of this rule.
     */
    public Rule(String name)
    {
        setState( STATE_DECL );

        this.name  = name;

        this.parameterDeclarations = Collections.EMPTY_SET;
        this.localDeclarations     = Collections.EMPTY_SET;

        this.filterConditions      = Collections.EMPTY_SET;
        this.assignmentConditions  = Collections.EMPTY_SET;
    }

    /** Produce output suitable for debugging.
     */
    public String toString()
    {
        return "[Rule: name='" + this.name
            + "'; paramDecls=" + this.parameterDeclarations
            + "; localDecls=" + this.localDeclarations
            + "; filterConds=" + this.filterConditions
            + "; assignConds=" + this.assignmentConditions
            + "]";
    }

    /** Get current state for validity checking.
     *
     *  @return The current state.
     */
    private int getState()
    {
        return this.state;
    }

    /** Set current state for validity checking.
     *
     *  @param state The state to set.
     */
    private void setState(int state)
    {
        this.state = state;
    }

    /** Determine if this rule is internally consistent and valid.
     *
     *  @return <code>true</code> if this rule is valid, else <code>false</code>.
     */
    public boolean isValid()
    {
        return ( ( ! getParameterDeclarations().isEmpty() )
                 &&
                 ( ! ( getFilterConditions().isEmpty()
                       &&
                       getAssignmentConditions().isEmpty() ) ) );
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

        if ( getFilterConditions().isEmpty()
             &&
             getAssignmentConditions().isEmpty() )
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
     *  <p>
     *  When constructing a rule, all parameter declarations must be
     *  specified before specifying any conditions.  Failure to do so
     *  may result in an invalid rule.  Attempting to add parameter
     *  declarations after a condition has been added will result in
     *  a <code>DeclarationAlreadyCompleteException</code>.
     *
     *  @param declaration The <i>root fact object</i> <code>Declaration</code> to add.
     *
     *  @throws DeclarationAlreadyCompleteException if any <code>Conditions</code> have
     *          already been added to this <code>Rule</code>.
     */
    public void addParameterDeclaration(Declaration declaration) throws DeclarationAlreadyCompleteException
    {
        if ( getState() == STATE_DECL )
        {
            if ( this.parameterDeclarations == Collections.EMPTY_SET )
            {
                this.parameterDeclarations = new HashSet();
            }
            
            this.parameterDeclarations.add( declaration );
        }
        else
        {
            throw new DeclarationAlreadyCompleteException( this );
        }
    }

    private void addLocalDeclaration(Declaration declaration)
    {
        setState( STATE_OTHER );

        if ( this.localDeclarations == Collections.EMPTY_SET )
        {
            this.localDeclarations = new HashSet();
        }

        this.localDeclarations.add( declaration );
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
        return this.localDeclarations;
    }

    /** Add a <code>FilterCondition</code> to this rule.
     *
     *  @param filterCondition The <code>FilterCondition</code> to add.
     */
    public void addFilterCondition(FilterCondition filterCondition)
    {
        setState( STATE_OTHER );

        if ( this.filterConditions == Collections.EMPTY_SET )
        {
            this.filterConditions = new HashSet();
        }

        this.filterConditions.add( filterCondition );
    }

    /** Add a consistent <code>AssignmentCondition</code> to this rule.
     *
     *  @param assignmentCondition the <code>AssignmentCondition</code> to add.
     */
    public void addAssignmentCondition(AssignmentCondition assignmentCondition)
    {
        setState( STATE_OTHER );

        if ( this.assignmentConditions == Collections.EMPTY_SET )
        {
            this.assignmentConditions = new HashSet();
        }

        this.assignmentConditions.add( assignmentCondition );

        Declaration decl = assignmentCondition.getTargetDeclaration();

        if ( ! this.parameterDeclarations.contains( decl ) )
        {
            addLocalDeclaration( decl );
        }
    }

    /** Retrieve the <code>Set</code> of <code>FilterConditions</code> for
     *  this rule.
     *
     *  @return The <code>Set</code> of <code>FilterConditions</code>.
     */
    public Set getFilterConditions()
    {
        return this.filterConditions;
    }

    /** Retrieve the <code>Set</code> of <code>AssignmentConditions</code> for
     *  this rule.
     *
     *  @return The <code>Set</code> of <code>AssignmentConditions</code>.
     */
    public Set getAssignmentConditions()
    {
        return this.assignmentConditions;
    }

    /** Set the <code>Action</code> that is associated with the
     *  successful match of this rule.
     *
     *  @param action The <code>Action</code> to attach to this <code>Rule</code>.
     */
    public void setAction(Action action)
    {
        this.action = action;
    }

    /** Retrieve the <code>Action</code> associated with this <code>Rule</code>.
     *
     *  @return The <code>Action</code>.
     */
    public Action getAction()
    {
        return this.action;
    }
}
