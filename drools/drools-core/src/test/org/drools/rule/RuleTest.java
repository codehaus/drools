package org.drools.rule;

import org.drools.spi.MockObjectType;

import junit.framework.TestCase;

import java.util.Set;

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

    public void testConstruct()
    {
        Rule rule = new Rule( "test-rule" );

        assertTrue( ! rule.isValid() );

        try
        {
            rule.checkValidity();

            fail( "Should have thrown InvalidRuleException" );
        }
        catch (InvalidRuleException e)
        {
            // expected and correct
        }
    }

    public void testParameterDeclarations()
    {
        Rule rule = new Rule( "test-rule" );

        Declaration paramDecl = new Declaration( new MockObjectType( true ),
                                                 "paramVar" );

        rule.addParameterDeclaration( paramDecl );

        Set paramDecls = rule.getParameterDeclarations();

        assertEquals( 1,
                      paramDecls.size() );

        assertTrue( paramDecls.contains( paramDecl ) );

        Set localDecls = rule.getLocalDeclarations();

        assertEquals( 0,
                      localDecls.size() );
    }

    public void testLocalDeclarations()
    {
        Rule rule = new Rule( "test-rule" );

        Declaration paramDecl = new Declaration( new MockObjectType( true ),
                                                 "paramVar" );

        Declaration localDecl = new Declaration( new MockObjectType( true ),
                                                 "paramVar" );

        FactExtraction extraction = new FactExtraction( localDecl,
                                                        null );

        rule.addParameterDeclaration( paramDecl );
        rule.addFactExtraction( extraction );

        Set paramDecls = rule.getParameterDeclarations();

        assertEquals( 1,
                      paramDecls.size() );

        assertTrue( paramDecls.contains( paramDecl ) );

        Set localDecls = rule.getLocalDeclarations();

        assertEquals( 1,
                      localDecls.size() );

        assertTrue( localDecls.contains( localDecl ) );
    }
}
