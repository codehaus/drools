package org.drools.spi;

import java.util.Map;
import java.util.Set;

import org.drools.FactException;
import org.drools.FactHandle;

public class KnowledgeHelper
{
    private Tuple tuple;
    private Map objects;

    public KnowledgeHelper(Tuple tuple)
    {
        this.tuple = tuple;
        Set declarationSet = this.tuple.getDeclarations();
        
        objects = tuple.getObjectFactMapping();
    }

    public void assertObject(Object object) throws FactException
    {
        this.tuple.getWorkingMemory().assertObject( object );
    }

    public void modifyObject(Object object) throws FactException
    {
        FactHandle handle = (FactHandle) this.objects.get( object );

        this.tuple.getWorkingMemory().modifyObject( handle, object );
    }

    public void modifyObject(Object oldObject, Object newObject) throws FactException
    {
        FactHandle handle = (FactHandle) this.objects.get( oldObject);

        this.tuple.getWorkingMemory().modifyObject( handle, newObject );
    }

    public void retractObject(Object object) throws FactException
    {
        FactHandle handle = (FactHandle) this.objects.get( object );

        this.tuple.getWorkingMemory().retractObject( handle );
    }

    public String getRuleName()
    {
        return this.tuple.getRule().getName();
    }

    public Object getObjects()
    {
        return this.tuple.getWorkingMemory().getObjects();
    }

    public Object getObjects(Class objectClass)
    {
        return this.tuple.getWorkingMemory().getObjects( objectClass );
    }

    public void clearAgenda()
    {
        this.tuple.getWorkingMemory().clearAgenda();
    }

}
