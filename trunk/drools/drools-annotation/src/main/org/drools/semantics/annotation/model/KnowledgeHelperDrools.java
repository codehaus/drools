package org.drools.semantics.annotation.model;

import java.util.List;

import org.drools.FactException;
import org.drools.semantics.annotation.Drools;
import org.drools.spi.KnowledgeHelper;
import org.drools.spi.Tuple;

class KnowledgeHelperDrools implements Drools
{
    private final KnowledgeHelper knowledgeHelper;

    public KnowledgeHelperDrools(org.drools.rule.Rule rule, Tuple tuple)
    {
        if (rule == null)
        {
            throw new IllegalArgumentException( "Null 'rule' argument" );
        }
        if (tuple == null)
        {
            throw new IllegalArgumentException( "Null 'tuple' argument" );
        }
        this.knowledgeHelper = new KnowledgeHelper( rule, tuple );
    }

    public void assertObject( Object object ) throws FactException
    {
        knowledgeHelper.assertObject( object );
    }

    public void assertObject( Object object, boolean dynamic ) throws FactException
    {
        knowledgeHelper.assertObject( object, dynamic );
    }

    public void clearAgenda( )
    {
        knowledgeHelper.clearAgenda( );
    }

    public List getObjects( )
    {
        return knowledgeHelper.getObjects( );
    }

    public List getObjects( Class objectClass )
    {
        return knowledgeHelper.getObjects( objectClass );
    }

    public String getRuleName( )
    {
        return knowledgeHelper.getRuleName( );
    }

    public void modifyObject( Object object ) throws FactException
    {
        knowledgeHelper.modifyObject( object );
    }

    public void modifyObject( Object oldObject, Object newObject ) throws FactException
    {
        knowledgeHelper.modifyObject( oldObject, newObject );
    }

    public void retractObject( Object object ) throws FactException
    {
        knowledgeHelper.retractObject( object );
    }
}
