package org.drools.semantics.java;

import org.drools.smf.ConfigurationException;

import org.drools.rule.Declaration;

import junit.framework.TestCase;

public class ExprTest extends TestCase
{
    private Declaration a;
    private Declaration b;
    private Declaration c;
    private Declaration d;

    private Declaration[] allDecls;

    private Expr expr;

    public ExprTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
        this.allDecls = new Declaration[4];

        this.a = new Declaration( new ClassObjectType( java.lang.String.class ),
                                  "a" );

        this.allDecls[0] = this.a;

        this.b = new Declaration( new ClassObjectType( java.lang.String.class ),
                                  "b" );

        this.allDecls[1] = this.b;

        this.c = new Declaration( new ClassObjectType( java.lang.String.class ),
                                  "c" );

        this.allDecls[2] = this.c;

        this.d = new Declaration( new ClassObjectType( java.lang.String.class ),
                                  "d" );

        this.allDecls[3] = this.d;

        this.expr = new Expr();
    }

    public void tearDown()
    {
        this.a = null;
        this.b = null;
        this.c = null;
        this.d = null;

        this.expr = null;
    }

    public void testConstruct_Fully() throws Exception
    {
        Expr expr = new Expr( "a + b + d",
                              this.allDecls );

        Declaration[] decls = expr.getRequiredTupleMembers();

        assertEquals( 3,
                      decls.length );

        assertContains( decls,
                        this.a );

        assertContains( decls,
                        this.b );

        assertContains( decls,
                        this.d );
        
    }

    public void testConfigure_None() throws Exception
    {
        this.expr.configure( "10 + 20",
                             this.allDecls );

        assertEquals( 0,
                      this.expr.getRequiredTupleMembers().length );
    }

    public void testConfigure_Exact() throws Exception
    {
        this.expr.configure( "a + b + c + d",
                             this.allDecls );

        Declaration[] decls = this.expr.getRequiredTupleMembers();

        assertEquals( 4,
                      decls.length );

        assertContains( decls,
                        this.a );

        assertContains( decls,
                        this.b );

        assertContains( decls,
                        this.c );

        assertContains( decls,
                        this.d );
    }

    public void testConfigure_Subset() throws Exception
    {
        this.expr.configure( "a + b + d",
                             this.allDecls  );

        Declaration[] decls = this.expr.getRequiredTupleMembers();

        assertEquals( 3,
                      decls.length );

        assertContains( decls,
                        this.a );

        assertContains( decls,
                        this.b );

        assertContains( decls,
                        this.d );
    }

    public void testConfigure_Superset() throws Exception
    {
        try
        {
            this.expr.configure( "a + b + c + d + e",
                                 this.allDecls );

            fail( "Should have thrown MissingDeclarationException" );
        }
        catch (ConfigurationException e)
        {
            // expected and correct

            MissingDeclarationException mde = (MissingDeclarationException) e.getRootCause();
            
            assertEquals( "e",
                          mde.getIdentifier() );
        }
    }

    protected void assertContains(Declaration[] decls,
                                  Declaration decl)
    {
        for ( int i = 0 ; i < decls.length ; ++i )
        {
            if ( decls[i].equals( decl ) )
            {
                return;
            }
        }

        fail( "Declarations does not contain " + decl );
    }
}
