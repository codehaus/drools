package org.drools.reteoo;

import org.drools.rule.Declaration;

import java.util.HashSet;
import java.util.Set;

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