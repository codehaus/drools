
package org.drools.semantic.xml;

public class XmlObjectType
{
    private String nsUri;
    private String tagName;

    public XmlObjectType(String nsUri,
                         String tagName)
    {
        this.nsUri   = nsUri;
        this.tagName = tagName;
    }

    public String getNsUri()
    {
        return this.nsUri;
    }

    public String getTagName()
    {
        return this.tagName;
    }

    public boolean matches(Object object)
    {
        // requires Jaxen MultiNavigator

        return false;
    }
}

