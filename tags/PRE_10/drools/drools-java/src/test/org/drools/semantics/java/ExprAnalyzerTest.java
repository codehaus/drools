package org.drools.semantics.java;

import org.drools.rule.Declaration;

import junit.framework.TestCase;

public class ExprAnalyzerTest extends TestCase
{
    private Declaration a;
    private Declaration b;
    private Declaration c;
    private Declaration d;

    private Declaration[] allDecls;

    private ExprAnalyzer analyzer;

    public ExprAnalyzerTest(String name)
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

        this.analyzer = new ExprAnalyzer();
    }

    public void tearDown()
    {
        this.a = null;
        this.b = null;
        this.c = null;
        this.d = null;

        this.analyzer = null;
    }

    public void testAnalyze_Exact() throws Exception
    {
        Declaration[] decls = this.analyzer.analyze( "a + b + c + d",
                                                     this.allDecls );

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

    public void testAnalyze_Subset() throws Exception
    {
        Declaration[] decls = this.analyzer.analyze( "a + b + d",
                                                     this.allDecls  );

        assertEquals( 3,
                      decls.length );

        assertContains( decls,
                        this.a );

        assertContains( decls,
                        this.b );

        assertContains( decls,
                        this.d );
    }

    public void testAnalyze_Superset() throws Exception
    {
        try
        {
            this.analyzer.analyze( "a + b + c + d + e",
                                   this.allDecls );

            // fail( "Should have thrown MissingDeclarationException" );
        }
        catch (MissingDeclarationException e)
        {
            // expected and correct

            assertEquals( "e",
                          e.getIdentifier() );
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
