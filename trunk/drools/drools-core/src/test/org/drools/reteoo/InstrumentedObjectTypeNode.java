package org.drools.reteoo;

import org.drools.WorkingMemory;
import org.drools.FactHandle;
import org.drools.AssertionException;
import org.drools.RetractionException;
import org.drools.spi.ObjectType;

import java.util.List;
import java.util.ArrayList;

public class InstrumentedObjectTypeNode
    extends ObjectTypeNode
{
    private List assertedObjects;
    private List retractedObjects;

    public InstrumentedObjectTypeNode(ObjectType objectType)
    {
        super( objectType );

        this.assertedObjects  = new ArrayList();
        this.retractedObjects = new ArrayList();
    }

    public void assertObject(FactHandle handle,
                             Object object,
                             WorkingMemoryImpl memory) throws AssertionException
    {
        super.assertObject( handle,
                            object,
                            memory );

        this.assertedObjects.add( object );
    }

    public void retractObject(FactHandle handle,
                              Object object,
                              WorkingMemoryImpl memory) throws RetractionException
    {
        super.retractObject( handle,
                             object,
                             memory );

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
