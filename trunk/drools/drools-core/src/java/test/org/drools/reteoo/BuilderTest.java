
package org.drools.reteoo;

import org.drools.reteoo.impl.ReteImpl;
import org.drools.spi.Rule;
import org.drools.spi.Declaration;
import org.drools.spi.ObjectType;
import org.drools.spi.AssignmentCondition;
import org.drools.spi.DeclarationAlreadyCompleteException;
import org.drools.spi.InstrumentedFilterCondition;
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

        InstrumentedFilterCondition filterCond = new InstrumentedFilterCondition();

        filterCond.addDeclaration( this.stringDecl );
        filterCond.addDeclaration( this.objectDecl );

        assertTrue( ! this.builder.matches( filterCond,
                                            decls ) );

        decls.add( this.stringDecl );

        assertTrue( ! this.builder.matches( filterCond,
                                            decls ) );

        decls.add( this.objectDecl );

        assertTrue( this.builder.matches( filterCond,
                                          decls ) );
    }

    public void testFindMatchingTupleSourceForAssignment()
    {
        Set sources = new HashSet();

        MockTupleSource source = null;

        InstrumentedFactExtractor extractor = null;

        AssignmentCondition assignCond = null;

        TupleSource found = null;

        // ----------------------------------------
        // ----------------------------------------

        source = new MockTupleSource();

        source.addTupleDeclaration( this.objectDecl );

        sources.add( source );

        extractor = new InstrumentedFactExtractor();

        extractor.addDeclaration( this.stringDecl );

        assignCond = new AssignmentCondition( this.objectDecl,
                                              extractor );

        found = this.builder.findMatchingTupleSourceForAssignment( assignCond,
                                                                   sources );

        assertNull( found );
        
        // ----------------------------------------
        // ----------------------------------------

        sources.clear();

        source = new MockTupleSource();

        source.addTupleDeclaration( this.objectDecl );

        source.addTupleDeclaration( this.stringDecl );

        sources.add( source );

        found = this.builder.findMatchingTupleSourceForAssignment( assignCond,
                                                                   sources );

        assertNull( found );

        // ----------------------------------------
        // ----------------------------------------

        sources.clear();

        source = new MockTupleSource();

        source.addTupleDeclaration( this.stringDecl );

        sources.add( source );

        found = this.builder.findMatchingTupleSourceForAssignment( assignCond,
                                                                   sources );

        assertNotNull( found );

        assertSame( source,
                    found );
    }

    public void testFindMatchingTupleSourceForFiltering()
    {
        Set sources = new HashSet();

        MockTupleSource source = null;

        // ----------------------------------------
        // ----------------------------------------

        source = new MockTupleSource();

        source.addTupleDeclaration( this.stringDecl );

        sources.add( source );

        InstrumentedFilterCondition filterCond = new InstrumentedFilterCondition();

        filterCond.addDeclaration( this.stringDecl );

        TupleSource found = null;

        found = this.builder.findMatchingTupleSourceForFiltering( filterCond,
                                                                  sources );

        assertNotNull( found );

        assertSame( source,
                    found );

        // ----------------------------------------
        // ----------------------------------------

        sources.clear();

        source = new MockTupleSource();

        source.addTupleDeclaration( this.objectDecl );

        found = this.builder.findMatchingTupleSourceForFiltering( filterCond,
                                                                  sources );

        assertNull( found );

        // ----------------------------------------
        // ----------------------------------------

        filterCond.addDeclaration( this.objectDecl );

        sources.clear();

        source = new MockTupleSource();

        source.addTupleDeclaration( this.objectDecl );
        source.addTupleDeclaration( this.stringDecl );

        sources.add( source );

        found = this.builder.findMatchingTupleSourceForFiltering( filterCond,
                                                                  sources );

        assertNotNull( found );

        assertSame( source,
                    found );
    }
}
