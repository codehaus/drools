
package org.drools.semantic.xml;

import org.drools.spi.ObjectType;

import org.dom4j.Document;
import org.dom4j.Element;

/** An {@link ObjectType} based upon the Namespace and
 *  root tag in a <code>dom4j</code> <code>Document</code>.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class Dom4jXmlObjectType implements ObjectType
{
    /** The namespace URI of this type. */
    private String nsUri;

    /** The root tag name of this type. */
    private String name;

    /** Construct.
     *
     *  @param nsUri The namespace URI for this object type.
     *  @param name The root element name for this object type.
     */
    public Dom4jXmlObjectType(String nsUri,
                              String name)
    {
        this.nsUri = nsUri;
        this.name  = name;
    }

    /** Retrieve the namespace URI for this object type.
     *
     *  @return The namespace URI.
     */
    public String getNsUri()
    {
        return this.nsUri;
    }

    /** Retrieve the root element name for this object type.
     *
     *  @return The root element name.
     */
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
