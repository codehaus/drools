
package org.drools.semantic.xml;

import org.drools.spi.ObjectType;

import org.dom4j.Document;
import org.dom4j.Element;

public class Dom4jXmlObjectType implements ObjectType
{
    private String nsUri;
    private String name;

    public Dom4jXmlObjectType(String nsUri,
                              String name)
    {
        this.nsUri = nsUri;
        this.name  = name;
    }

    public String getNsUri()
    {
        return this.nsUri;
    }

    public String getName()
    {
        return this.name;
    }

    public boolean matches(Object object)
    {
        if ( ! ( object instanceof Document ) )
        {
            return false;
        }

        Document doc = (Document) object;

        Element root = doc.getRootElement();

        if ( this.nsUri == null )
        {
            if ( ! root.getNamespaceURI().equals( "" ) )
            {
                return false;
            }
        }
        else
        {
            if ( ! root.getNamespaceURI().equals( this.nsUri ) )
            {
                return false;
            }
        }

        if ( ! root.getName().equals( this.name ) )
        {
            return false;
        }
        
        return true;
    }
}
