package org.drools.rule;

import org.drools.spi.MockObjectType;

import junit.framework.TestCase;

import java.util.Set;

public class RuleTest
    extends TestCase
{
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
        throws Exception
    {
        Rule rule = new Rule( "test-rule" );

        Declaration paramDecl = new Declaration( new MockObjectType( true ),
                                                 "paramVar" );

        rule.addParameterDeclaration( paramDecl );

        Set paramDecls = rule.getParameterDeclarations();

        assertEquals( 1,
                      paramDecls.size() );

        assertTrue( paramDecls.contains( paramDecl ) );

        assertSame( paramDecl,
                    rule.getParameterDeclaration( "paramVar" ) );

        assertNull( rule.getParameterDeclaration( "betty" ) );

        Set localDecls = rule.getLocalDeclarations();

        assertEquals( 0,
                      localDecls.size() );
    }

    public void testLocalDeclarations()
        throws Exception
    {
        Rule rule = new Rule( "test-rule" );

        Declaration paramDecl = new Declaration( new MockObjectType( true ),
                                                 "paramVar" );

        Declaration localDecl = new Declaration( new MockObjectType( true ),
                                                 "localVar" );

        Extraction extraction = new Extraction( localDecl,
                                                null );

        rule.addParameterDeclaration( paramDecl );
        rule.addExtraction( extraction );

        Set paramDecls = rule.getParameterDeclarations();

        assertEquals( 1,
                      paramDecls.size() );

        assertTrue( paramDecls.contains( paramDecl ) );

        Set localDecls = rule.getLocalDeclarations();

        assertEquals( 1,
                      localDecls.size() );

        assertTrue( localDecls.contains( localDecl ) );

        assertSame( localDecl,
                    rule.getDeclaration( "localVar" ) );
    }

    public void testDocumenation()
        throws Exception
    {
        Rule rule = new Rule( "test-rule" );

        assertNull( rule.getDocumentation() );

        rule.setDocumentation( "the cheesiest!" );

        assertEquals( "the cheesiest!",
                      rule.getDocumentation() );
    }

    public void testSalience()
        throws Exception
    {
        Rule rule = new Rule( "test-rule" );

        assertEquals( 0,
                      rule.getSalience() );

        rule.setSalience( 42 );

        assertEquals( 42,
                      rule.getSalience() );
    }
}
