package org.drools.reteoo;

import org.drools.AssertionException;
import org.drools.FactHandle;
import org.drools.RetractionException;
import org.drools.rule.Declaration;
import org.drools.rule.Rule;

import java.util.ArrayList;
import java.util.List;

public class InstrumentedParameterNode extends ParameterNode
{
    private List        assertedObjects;

    private List        retractedObjects;

    private static Rule rule;

    /**
     *
     * this is a nasty hack, but need to make sure that the rule is passed via
     * the super call
     */
    static
    {
        rule = new Rule( "test-rule 1" );

        //add consequence
        rule.setConsequence( new org.drools.spi.InstrumentedConsequence( ) );
        //add condition
        rule.addCondition( new org.drools.spi.InstrumentedCondition( ) );
    }

    public InstrumentedParameterNode(ObjectTypeNode inputNode, Declaration decl)
    {
        super( inputNode, decl );

        this.assertedObjects = new ArrayList( );
        this.retractedObjects = new ArrayList( );
    }

    protected void assertObject(FactHandle handle,
                                Object object,
                                WorkingMemoryImpl workingMemory) throws AssertionException
    {
        super.assertObject( handle, object, workingMemory );

        this.assertedObjects.add( object );
    }

    protected void retractObject(FactHandle handle,
                                 WorkingMemoryImpl workingMemory) throws RetractionException
    {
        super.retractObject( handle, workingMemory );

        this.retractedObjects.add( handle );
    }

    public List getAssertedObjects()
    {
        return this.assertedObjects;
    }

    public List getRetractedObjects()
    {
        return this.retractedObjects;
    }
}