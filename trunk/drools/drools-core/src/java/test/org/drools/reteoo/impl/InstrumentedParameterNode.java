
package org.drools.reteoo.impl;

import org.drools.WorkingMemory;
import org.drools.FactHandle;
import org.drools.AssertionException;
import org.drools.RetractionException;
import org.drools.rule.Declaration;

import java.util.List;
import java.util.ArrayList;

public class InstrumentedParameterNode extends ParameterNodeImpl
{
    private List assertedObjects;
    private List retractedObjects;

    public InstrumentedParameterNode(ObjectTypeNodeImpl inputNode,
                                     Declaration decl)
    {
        super( inputNode,
               decl );

        this.assertedObjects  = new ArrayList();
        this.retractedObjects = new ArrayList();
    }

    protected void assertObject(FactHandle handle,
                                Object object,
                                WorkingMemory workingMemory) throws AssertionException
    {
        super.assertObject( handle,
                            object,
                            workingMemory );

        this.assertedObjects.add( object );
    }

    protected void retractObject(FactHandle handle,
                                 WorkingMemory workingMemory) throws RetractionException
    {
        super.retractObject( handle,
                             workingMemory );

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
