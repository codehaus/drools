package org.drools.reteoo;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import junit.framework.TestCase;

import org.drools.rule.Declaration;
import org.drools.rule.Extraction;
import org.drools.rule.Rule;
import org.drools.spi.InstrumentedCondition;
import org.drools.spi.InstrumentedExtractor;
import org.drools.spi.MockObjectType;
import org.drools.spi.ObjectType;

public class BuilderTest extends TestCase
{
    private Builder     builder;

    private ObjectType  stringType;

    private ObjectType  objectType;

    private Declaration stringDecl;

    private Declaration objectDecl;

    private Rule        rule1;

    public void setUp()
    {
        this.builder = new Builder( );

        this.stringType = new MockObjectType( true );
        this.objectType = new MockObjectType( true );

        this.rule1 = new Rule( "cheese" );

        this.stringDecl = new Declaration( this.stringType, "string" );

        this.objectDecl = new Declaration( this.objectType, "object" );

        this.rule1.addParameterDeclaration( this.stringDecl );

        this.rule1.addParameterDeclaration( this.objectDecl );
    }

    public void testCreateParameterNodes()
    {
        Set nodes = this.builder.createParameterNodes( this.rule1 );

        assertEquals( 2, nodes.size( ) );

        Set decls = new HashSet( );

        Iterator nodeIter = nodes.iterator( );
        ParameterNode eachNode = null;

        while ( nodeIter.hasNext( ) )
        {
            eachNode = ( ParameterNode ) nodeIter.next( );

            decls.add( eachNode.getDeclaration( ) );
        }

        assertEquals( 2, decls.size( ) );

        assertTrue( decls.contains( this.stringDecl ) );
        assertTrue( decls.contains( this.objectDecl ) );
    }

    public void testMatches()
    {
        Set decls = new HashSet( );

        InstrumentedCondition cond = new InstrumentedCondition( );

        cond.addDeclaration( this.stringDecl );
        cond.addDeclaration( this.objectDecl );

        assertTrue( !this.builder.matches( cond, decls ) );

        decls.add( this.stringDecl );

        assertTrue( !this.builder.matches( cond, decls ) );

        decls.add( this.objectDecl );

        assertTrue( this.builder.matches( cond, decls ) );
    }

    public void testFindMatchingTupleSourceForExtraction()
    {
        Set sources = new HashSet( );

        MockTupleSource source = null;

        InstrumentedExtractor extractor = null;

        Extraction extract = null;

        TupleSource found = null;

        // ----------------------------------------
        // ----------------------------------------

        source = new MockTupleSource( );

        source.addTupleDeclaration( this.objectDecl );

        sources.add( source );

        extractor = new InstrumentedExtractor( );

        extractor.addDeclaration( this.stringDecl );

        extract = new Extraction( this.objectDecl, extractor );

        found = this.builder.findMatchingTupleSourceForExtraction( extract,
                                                                   sources );

        //sources contains objectsDecl, not stringDecl
        //So extractor is not able to find a match for its stringDecl
        assertNull( found );

        // ----------------------------------------
        // ----------------------------------------

        sources.clear( );

        source = new MockTupleSource( );

        source.addTupleDeclaration( this.objectDecl );

        source.addTupleDeclaration( this.stringDecl );

        sources.add( source );

        found = this.builder.findMatchingTupleSourceForExtraction( extract,
                                                                   sources );

        //sources contains stringDecl and objectDecl
        //So extractor finds a match to its stringDecl
        assertNotNull( found );

        assertSame( source, found );

        // ----------------------------------------
        // ----------------------------------------

        sources.clear( );

        source = new MockTupleSource( );

        source.addTupleDeclaration( this.stringDecl );

        sources.add( source );

        found = this.builder.findMatchingTupleSourceForExtraction( extract,
                                                                   sources );

        //sources contains stringDecl
        //So extractor finds a match to its stringDecl

        assertNotNull( found );

        assertSame( source, found );
    }

    public void testFindMatchingTupleSourceForCondition()
    {
        Set sources = new HashSet( );

        MockTupleSource source = null;

        // ----------------------------------------
        // ----------------------------------------

        source = new MockTupleSource( );

        source.addTupleDeclaration( this.stringDecl );

        sources.add( source );

        InstrumentedCondition cond = new InstrumentedCondition( );

        cond.addDeclaration( this.stringDecl );

        TupleSource found = null;

        found = this.builder
                            .findMatchingTupleSourceForCondition( cond, sources );

        assertNotNull( found );

        assertSame( source, found );

        // ----------------------------------------
        // ----------------------------------------

        sources.clear( );

        source = new MockTupleSource( );

        source.addTupleDeclaration( this.objectDecl );

        found = this.builder
                            .findMatchingTupleSourceForCondition( cond, sources );

        assertNull( found );

        // ----------------------------------------
        // ----------------------------------------

        cond.addDeclaration( this.objectDecl );

        sources.clear( );

        source = new MockTupleSource( );

        source.addTupleDeclaration( this.objectDecl );
        source.addTupleDeclaration( this.stringDecl );

        sources.add( source );

        found = this.builder
                            .findMatchingTupleSourceForCondition( cond, sources );

        assertNotNull( found );

        assertSame( source, found );
    }
}