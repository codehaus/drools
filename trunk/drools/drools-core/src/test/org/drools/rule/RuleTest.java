package org.drools.rule;

import org.drools.DroolsTestCase;
import org.drools.spi.MockObjectType;

import java.util.Set;

public class RuleTest
    extends DroolsTestCase
{
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

        Declaration[] paramDecls = rule.getParameterDeclarations();

        assertLength( 1,
                      paramDecls );

        assertContains( paramDecl,
                        paramDecls );

        assertSame( paramDecl,
                    rule.getParameterDeclaration( "paramVar" ) );

        assertNull( rule.getParameterDeclaration( "betty" ) );

        Declaration[] localDecls = rule.getLocalDeclarations();

        assertLength( 0,
                      localDecls );
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

        Declaration[] paramDecls = rule.getParameterDeclarations();

        assertLength( 1,
                      paramDecls );

        assertContains( paramDecl,
                        paramDecls );

        Declaration[] localDecls = rule.getLocalDeclarations();

        assertLength( 1,
                      localDecls );

        assertContains( localDecl,
                        localDecls );

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
