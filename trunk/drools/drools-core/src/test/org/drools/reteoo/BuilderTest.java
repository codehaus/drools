package org.drools.reteoo;

import junit.framework.TestCase;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;
import org.drools.spi.InstrumentedCondition;
import org.drools.spi.MockObjectType;
import org.drools.spi.ObjectType;

import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class BuilderTest extends TestCase
{
    private Builder     builder;

    private ObjectType  stringType;

    private ObjectType  objectType;

    private Declaration stringDecl;

    private Declaration objectDecl;

    private Rule        rule1;

    public void setUp() throws Exception
    {
        this.builder = new Builder( );

        this.stringType = new MockObjectType();
        this.objectType = new MockObjectType();

        this.rule1 = new Rule( "cheese" );

        this.stringDecl = this.rule1.addParameterDeclaration( "string", this.stringType );

        this.objectDecl = this.rule1.addParameterDeclaration( "object", this.objectType );
    }

    public void testCreateParameterNodes()
    {
        List nodes = this.builder.createParameterNodes( this.rule1 );

        assertEquals( 2, nodes.size( ) );

        Set decls = new HashSet( );

        Iterator nodeIter = nodes.iterator( );
        ParameterNode eachNode;

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

    public void testFindMatchingTupleSourceForCondition()
    {
        List sources = new LinkedList( );

        MockTupleSource source;

        // ----------------------------------------
        // ----------------------------------------

        source = new MockTupleSource( );

        source.addTupleDeclaration( this.stringDecl );

        sources.add( source );

        InstrumentedCondition cond = new InstrumentedCondition( );

        cond.addDeclaration( this.stringDecl );

        TupleSource found;

        found = this.builder.findMatchingTupleSourceForCondition( cond, sources );

        assertNotNull( found );

        assertSame( source, found );

        // ----------------------------------------
        // ----------------------------------------

        sources.clear( );

        source = new MockTupleSource( );

        source.addTupleDeclaration( this.objectDecl );

        found = this.builder.findMatchingTupleSourceForCondition( cond, sources );

        assertNull( found );

        // ----------------------------------------
        // ----------------------------------------

        cond.addDeclaration( this.objectDecl );

        sources.clear( );

        source = new MockTupleSource( );

        source.addTupleDeclaration( this.objectDecl );
        source.addTupleDeclaration( this.stringDecl );

        sources.add( source );

        found = this.builder.findMatchingTupleSourceForCondition( cond, sources );

        assertNotNull( found );

        assertSame( source, found );
    }
}