
package org.drools.semantic.java;

import org.drools.spi.FactExtractor;
import org.drools.spi.Declaration;
import org.drools.spi.Tuple;
import org.drools.spi.FactExtractionException;

import org.apache.commons.beanutils.PropertyUtils;

import java.lang.reflect.InvocationTargetException;

/** <code>FactExtractor</code> capable of extracting simple
 *  JavaBean style facts from a single object.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class JavaBeanFactExtractor extends JavaFactExtractor
{
    private String propertyName;

    public JavaBeanFactExtractor(Declaration objectDecl,
                                 String propertyName)
    {
        super( objectDecl );

        this.propertyName = propertyName;
    }

    public String getPropertyName()
    {
        return this.propertyName;
    }

    public Object extractFactFromObject(Object object) throws FactExtractionException
    {
        Object propValue = null;

        try
        {
            propValue = PropertyUtils.getProperty( object,
                                                   getPropertyName() );
        }
        catch (IllegalAccessException e)
        {
            throw new FactExtractionException( e );
        }
        catch (NoSuchMethodException e)
        {
            throw new FactExtractionException( e );
        }
        catch (InvocationTargetException e)
        {
            throw new FactExtractionException( e );
        }

        return propValue;
    }
}
