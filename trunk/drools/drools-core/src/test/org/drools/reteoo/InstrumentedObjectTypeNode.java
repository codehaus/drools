
package org.drools.reteoo;

import org.drools.WorkingMemory;
import org.drools.AssertionException;
import org.drools.RetractionException;
import org.drools.spi.ObjectType;

import java.util.List;
import java.util.ArrayList;

public class InstrumentedObjectTypeNode extends ObjectTypeNode
{
    private List assertedObjects;
    private List retractedObjects;

    public InstrumentedObjectTypeNode(ObjectType objectType)
    {
        super( objectType );

        this.assertedObjects  = new ArrayList();
        this.retractedObjects = new ArrayList();
    }

    public void assertObject(Object object,
                             WorkingMemory memory) throws AssertionException
    {
        super.assertObject( object,
                            memory );

        this.assertedObjects.add( object );
    }

    public void retractObject(Object object,
                              WorkingMemory memory) throws RetractionException
    {
        super.retractObject( object,
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
