package org.drools.reteoo;

import java.util.HashSet;
import java.util.Set;

import org.drools.rule.Declaration;

public class MockTupleSource extends TupleSource
{
    private Set declarations;

    public MockTupleSource()
    {        
        this.declarations = new HashSet( );
    }

    public void addTupleDeclaration(Declaration decl)
    {
        this.declarations.add( decl );
    }

    public Set getTupleDeclarations()
    {
        return this.declarations;
    }
}