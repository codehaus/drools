
package org.drools.reteoo;

import org.drools.reteoo.impl.ReteImpl;
import org.drools.rule.Rule;
import org.drools.rule.Declaration;
import org.drools.rule.FactExtraction;
import org.drools.rule.DeclarationAlreadyCompleteException;
import org.drools.spi.ObjectType;
import org.drools.spi.InstrumentedCondition;
import org.drools.spi.InstrumentedFactExtractor;
import org.drools.semantic.java.JavaObjectType;

import junit.framework.TestCase;

import java.util.Set;
import java.util.HashSet;
import java.util.Iterator;

public class BuilderTest extends TestCase
{
    private ReteImpl rete;
    private Builder  builder;

    private ObjectType stringType;
    private ObjectType objectType;

    private Declaration stringDecl;
    private Declaration objectDecl;

    private Rule rule1;

    public BuilderTest(String name)
    {
        super( name );
    }

    public void setUp()
    {
        this.rete     = new ReteImpl();
        this.builder  = new Builder( this.rete );

        this.stringType = new JavaObjectType( String.class );
        this.objectType = new JavaObjectType( Object.class );

        this.rule1 = new Rule( "cheese" );

        try
        {
            this.stringDecl = new Declaration( this.stringType,
                                               "string" );

            this.objectDecl = new Declaration( this.objectType,
                                               "object" );

            this.rule1.addParameterDeclaration( this.stringDecl );
            
            this.rule1.addParameterDeclaration( this.objectDecl );
        }
        catch (DeclarationAlreadyCompleteException e)
        {
            fail( e.toString() );
        }
    }

    public void tearDown()
    {
        this.rete    = null;
        this.builder = null;

        this.stringType = null;
        this.objectType = null;

        this.rule1 = null;
    }

    public void testCreateParameterNodes()
    {
        Set nodes = this.builder.createParameterNodes( this.rule1 );

        assertEquals( 2,
                      nodes.size() );

        Set decls    = new HashSet();

        Iterator      nodeIter = nodes.iterator();
        ParameterNode eachNode = null;

        while ( nodeIter.hasNext() )
        {
            eachNode = (ParameterNode) nodeIter.next();

            decls.add( eachNode.getDeclaration() );
        }

        assertEquals( 2,
                      decls.size() );

        assertTrue( decls.contains( this.stringDecl ) );
        assertTrue( decls.contains( this.objectDecl ) );
    }

    public void testMatches()
    {
        Set decls = new HashSet();

        InstrumentedCondition cond = new InstrumentedCondition();

        cond.addDeclaration( this.stringDecl );
        cond.addDeclaration( this.objectDecl );

        assertTrue( ! this.builder.matches( cond,
                                            decls ) );

        decls.add( this.stringDecl );

        assertTrue( ! this.builder.matches( cond,
                                            decls ) );

        decls.add( this.objectDecl );

        assertTrue( this.builder.matches( cond,
                                          decls ) );
    }

    public void testFindMatchingTupleSourceForExtraction()
    {
        Set sources = new HashSet();

        MockTupleSource source = null;

        InstrumentedFactExtractor extractor = null;

        FactExtraction factExtract = null;
        
        TupleSource found = null;

        // ----------------------------------------
        // ----------------------------------------

        source = new MockTupleSource();

        source.addTupleDeclaration( this.objectDecl );

        sources.add( source );

        extractor = new InstrumentedFactExtractor();

        extractor.addDeclaration( this.stringDecl );

        factExtract = new FactExtraction( this.objectDecl,
                                          extractor );

        found = this.builder.findMatchingTupleSourceForExtraction( factExtract,
                                                                   sources );

        assertNull( found );
        
        // ----------------------------------------
        // ----------------------------------------

        sources.clear();

        source = new MockTupleSource();

        source.addTupleDeclaration( this.objectDecl );

        source.addTupleDeclaration( this.stringDecl );

        sources.add( source );

        found = this.builder.findMatchingTupleSourceForExtraction( factExtract,
                                                                   sources );

        assertNull( found );

        // ----------------------------------------
        // ----------------------------------------

        sources.clear();

        source = new MockTupleSource();

        source.addTupleDeclaration( this.stringDecl );

        sources.add( source );

        found = this.builder.findMatchingTupleSourceForExtraction( factExtract,
                                                                   sources );

        assertNotNull( found );

        assertSame( source,
                    found );
    }

    public void testFindMatchingTupleSourceForCondition()
    {
        Set sources = new HashSet();

        MockTupleSource source = null;

        // ----------------------------------------
        // ----------------------------------------

        source = new MockTupleSource();

        source.addTupleDeclaration( this.stringDecl );

        sources.add( source );

        InstrumentedCondition cond = new InstrumentedCondition();

        cond.addDeclaration( this.stringDecl );

        TupleSource found = null;

        found = this.builder.findMatchingTupleSourceForCondition( cond,
                                                                  sources );

        assertNotNull( found );

        assertSame( source,
                    found );

        // ----------------------------------------
        // ----------------------------------------

        sources.clear();

        source = new MockTupleSource();

        source.addTupleDeclaration( this.objectDecl );

        found = this.builder.findMatchingTupleSourceForCondition( cond,
                                                                  sources );

        assertNull( found );

        // ----------------------------------------
        // ----------------------------------------

        cond.addDeclaration( this.objectDecl );

        sources.clear();

        source = new MockTupleSource();

        source.addTupleDeclaration( this.objectDecl );
        source.addTupleDeclaration( this.stringDecl );

        sources.add( source );

        found = this.builder.findMatchingTupleSourceForCondition( cond,
                                                                  sources );

        assertNotNull( found );

        assertSame( source,
                    found );
    }
}
