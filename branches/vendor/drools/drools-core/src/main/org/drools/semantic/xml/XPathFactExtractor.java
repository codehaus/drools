
package org.drools.semantic.xml;

import org.drools.spi.FactExtractor;
import org.drools.spi.Declaration;
import org.drools.spi.Tuple;

import org.jaxen.BaseXPath;

public class XPathFactExtractor implements FactExtractor
{
    private Declaration declaration;
    private BaseXPath   xpath;

    public XPathFactExtractor(Declaration declaration,
                              BaseXPath xpath)
    {
        this.declaration = declaration;
        this.xpath       = xpath;
    }

    public Declaration getDocumentDeclaration()
    {
        return this.declaration;
    }

    public BaseXPath getXPath()
    {
        return this.xpath;
    }

    public Declaration[] getRequiredTupleMembers()
    {
        return new Declaration[] {
            getDocumentDeclaration()
        };
    }

    public Object extractFact(Tuple tuple)
    {
        Object document = tuple.get( getDocumentDeclaration() );

        Object result   = null;

        // result = getXPath().evaluate( ... )

        return result;
    }
}
