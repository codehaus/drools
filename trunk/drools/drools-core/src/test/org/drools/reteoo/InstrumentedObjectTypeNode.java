package org.drools.reteoo;

import org.drools.WorkingMemory;
import org.drools.FactHandle;
import org.drools.FactException;
import org.drools.spi.ObjectType;

import java.util.List;
import java.util.ArrayList;

public class InstrumentedObjectTypeNode
    extends ObjectTypeNode
{
    private List assertedObjects;
    private List retractedHandles;

    public InstrumentedObjectTypeNode(ObjectType objectType)
    {
        super( objectType );

        this.assertedObjects  = new ArrayList();
        this.retractedHandles = new ArrayList();
    }

    public void assertObject(FactHandle handle,
                             Object object,
                             WorkingMemoryImpl memory)
        throws FactException
    {
        super.assertObject( handle,
                            object,
                            memory );

        this.assertedObjects.add( object );
    }

    public void retractObject(FactHandle handle,
                              WorkingMemoryImpl memory)
        throws FactException
    {
        super.retractObject( handle,
                             memory );

        this.retractedHandles.add( handle );
    }

    public List getAssertedObjects()
    {
        return this.assertedObjects;
    }

    public List getRetractedHandles()
    {
        return this.retractedHandles;
    }
}
