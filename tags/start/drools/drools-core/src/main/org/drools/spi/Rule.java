
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

    public Rule(String name)
    {
        setState( STATE_DECL );

        this.name  = name;

        this.parameterDeclarations = Collections.EMPTY_SET;
        this.localDeclarations     = Collections.EMPTY_SET;

        this.filterConditions      = Collections.EMPTY_SET;
        this.assignmentConditions  = Collections.EMPTY_SET;
    }

    private int getState()
    {
        return this.state;
    }

    private void setState(int state)
    {
        this.state = state;
    }

    public boolean isValid()
    {
        return ( ( ! getParameterDeclarations().isEmpty() )
                 &&
                 ( ! ( getFilterConditions().isEmpty()
                       &&
                       getAssignmentConditions().isEmpty() ) ) );
    }

    public String getName()
    {
        return this.name;
    }

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

    protected void addLocalDeclaration(Declaration declaration)
    {
        setState( STATE_OTHER );

        if ( this.localDeclarations == Collections.EMPTY_SET )
        {
            this.localDeclarations = new HashSet();
        }

        this.localDeclarations.add( declaration );
    }

    public Set getParameterDeclarations()
    {
        return this.parameterDeclarations;
    }

    public Set getLocalDeclarations()
    {
        return this.localDeclarations;
    }

    public void addFilterCondition(FilterCondition filterCondition)
    {
        setState( STATE_OTHER );

        if ( this.filterConditions == Collections.EMPTY_SET )
        {
            this.filterConditions = new HashSet();
        }

        this.filterConditions.add( filterCondition );
    }

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

    public Set getFilterConditions()
    {
        return this.filterConditions;
    }

    public Set getAssignmentConditions()
    {
        return this.assignmentConditions;
    }
}
