
package org.drools.spi;

import junit.framework.TestCase;

public class RuleTest extends TestCase
{
    public RuleTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
    }

    public void tearDown()
    {
    }

    /** A completely empty Rule MUST be considered invalid.
     */
    public void testIsValid_Empty()
    {
        Rule rule = new Rule( "cheese" );

        assertTrue( ! rule.isValid() );
    }

    /** A Rule that contains only parameter delcarations
     *  MUST be considered invalid.
     */
    public void testIsValid_DeclOnly()
    {
        Rule rule = new Rule( "cheese" );

        try
        {
            rule.addParameterDeclaration( new Declaration( null,
                                                           null ) );
        }
        catch (DeclarationAlreadyCompleteException e)
        {
            fail( e.toString() );
        }

        assertTrue( ! rule.isValid() );
    }

    /** A Rule with a parameter declaration and at least one
     *  FilterCondition MUST be considered valid.
     */
    public void testIsValid_FilterOnly()
    {
        Rule rule = new Rule( "cheese" );

        try
        {
            rule.addParameterDeclaration( new Declaration( null,
                                                           null ) );
        }
        catch (DeclarationAlreadyCompleteException e)
        {
            fail( e.toString() );
        }

        rule.addFilterCondition( new TrueFilterCondition() );

        assertTrue( rule.isValid() );
    }

    /** A Rule with a parameter declaration and at least one
     *  AssignmentCondition MUST be considered valid.
     */
    public void testIsValid_AssignmentOnly()
    {
        Rule rule = new Rule( "cheese" );

        try
        {
            rule.addParameterDeclaration( new Declaration( null,
                                                           null ) );
        }
        catch (DeclarationAlreadyCompleteException e)
        {
            fail( e.toString() );
        }

        rule.addAssignmentCondition( new AssignmentCondition( null,
                                                              null ) );

        assertTrue( rule.isValid() );
    }

    /** A Rule with a parameter declaration and FilterConditions
     *  and AssignmentConditions MUST be considered valid.
     */
    public void testIsValid_FilterAndAssignment()
    {
        Rule rule = new Rule( "cheese" );

        
        try
        {
            rule.addParameterDeclaration( new Declaration( null,
                                                           null ) );
        }
        catch (DeclarationAlreadyCompleteException e)
        {
            fail( e.toString() );
        }

        rule.addFilterCondition( new TrueFilterCondition() );
        rule.addAssignmentCondition( new AssignmentCondition( null,
                                                              null ) );

        assertTrue( rule.isValid() );
    }

    /** A Rule MUST accept new parameter Declarations until
     *  some condition is added, and then it MUST throw a
     *  DeclarationAlreadyCompleteException if an attempt is
     *  made to add more parameter Declarations.
     */
    public void testDeclComplete()
    {
        Rule rule = new Rule( "cheese" );

        try
        {
            rule.addParameterDeclaration( new Declaration( null,
                                                           "object1" ) );
        }
        catch (DeclarationAlreadyCompleteException e)
        {
            fail( e.toString() );
        }

        rule.addFilterCondition( new TrueFilterCondition() );

        try
        {
            rule.addParameterDeclaration( new Declaration( null,
                                                           "object2" ) );

            fail( "Should have thrown DeclarationAlreadyCompletelyException" );
        }
        catch (DeclarationAlreadyCompleteException e)
        {
            // expected and correct.

            assertSame( rule,
                        e.getRule() );
        }
    }
}
