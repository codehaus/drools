package org.drools.spi;

import org.drools.FactException;

import java.util.List;

public interface KnowledgeHelper
{
    void assertObject(Object object) throws FactException;

    void assertObject(Object object,
                             boolean dynamic ) throws FactException;

    void modifyObject(Object object) throws FactException;

    void modifyObject( Object oldObject,
                              Object newObject ) throws FactException;

    void retractObject( Object object ) throws FactException;

    String getRuleName();

    List getObjects();

    List getObjects( Class objectClass );

    void clearAgenda();
}
