
package org.drools.semantic.xml;

import org.drools.spi.FactExtractor;
import org.drools.spi.Declaration;
import org.drools.spi.Tuple;
import org.drools.spi.FactExtractionException;

import org.jaxen.JaxenException;
import org.jaxen.dom4j.XPath;

public class Dom4jXPathFactExtractor implements FactExtractor
{
    private Declaration declaration;
    private XPath       xpath;

    public Dom4jXPathFactExtractor(Declaration declaration,
                                   XPath xpath)
    {
        this.declaration = declaration;
        this.xpath       = xpath;
    }
    
    public Declaration getDocumentDeclaration()
    {
        return this.declaration;
    }

    public XPath getXPath()
    {
        return this.xpath;
    }

    public Declaration[] getRequiredTupleMembers()
    {
        return new Declaration[] {
            getDocumentDeclaration()
        };
    }

    public Object extractFact(Tuple tuple) throws FactExtractionException
    {
        Object document = tuple.get( getDocumentDeclaration() );

        Object result   = null;

        try
        {
            result = getXPath().evaluate( document );
        }
        catch (JaxenException e)
        {
            throw new FactExtractionException( e );
        }

        return result;
    }
}
