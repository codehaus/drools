package org.drools.semantics.annotation;

import java.util.List;

import org.drools.FactException;

public interface DroolsContext
{
    void assertObject( Object object ) throws FactException;

    void assertObject( Object object, boolean dynamic ) throws FactException;

    void clearAgenda( );

    List getObjects( );

    List getObjects( Class objectClass );

    String getRuleName( );

    void modifyObject( Object object ) throws FactException;

    void modifyObject( Object oldObject, Object newObject ) throws FactException;

    void retractObject( Object object ) throws FactException;
}
