
package org.drools.semantic.xml;

import org.drools.spi.FactExtractor;
import org.drools.spi.Declaration;
import org.drools.spi.Tuple;
import org.drools.spi.FactExtractionException;

import org.jaxen.JaxenException;
import org.jaxen.dom4j.Dom4jXPath;

/** Extracts new fact objects from a Dom4j <code>Document</code>
 *  object using the results of XPath expressions.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class Dom4jXPathFactExtractor implements FactExtractor
{
    private Declaration declaration;
    private Dom4jXPath  xpath;

    /** Construct.
     *
     * @param declaration The variable in which this extractor
     *        expects the context <code>Document</code> to be bound to.
     * @param xpath The fact-extracting XPath expression object.
     */
    public Dom4jXPathFactExtractor(Declaration declaration,
                                   Dom4jXPath xpath)
    {
        this.declaration = declaration;
        this.xpath       = xpath;
    }
    
    /** Retrieve the <code>Declaration</code> expected to hold
     *  the <code>Document</code>.
     *
     *  @return The Declaration of the expected Document.
     */
    public Declaration getDocumentDeclaration()
    {
        return this.declaration;
    }

    /** Retrieve the XPath expression object.
     *
     *  @return The XPath expression object.
     */
    public Dom4jXPath getXPath()
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
