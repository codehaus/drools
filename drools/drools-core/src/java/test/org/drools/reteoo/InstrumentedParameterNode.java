
package org.drools.reteoo;

import org.drools.WorkingMemory;
import org.drools.AssertionException;
import org.drools.RetractionException;
import org.drools.spi.Declaration;

import java.util.List;
import java.util.ArrayList;

public class InstrumentedParameterNode extends ParameterNode
{
    private List assertedObjects;
    private List retractedObjects;

    public InstrumentedParameterNode(ObjectTypeNode inputNode,
                                     Declaration decl)
    {
        super( inputNode,
               decl );

        this.assertedObjects  = new ArrayList();
        this.retractedObjects = new ArrayList();
    }

    protected void assertObject(Object object,
                                WorkingMemory workingMemory) throws AssertionException
    {
        super.assertObject( object,
                            workingMemory );

        this.assertedObjects.add( object );
    }

    protected void retractObject(Object object,
                                 WorkingMemory workingMemory) throws RetractionException
    {
        super.retractObject( object,
                             workingMemory );

        this.retractedObjects.add( object );
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
