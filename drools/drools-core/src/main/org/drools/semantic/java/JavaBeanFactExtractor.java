
package org.drools.semantic.java;

import org.drools.spi.FactExtractor;
import org.drools.spi.Declaration;
import org.drools.spi.Tuple;
import org.drools.spi.FactExtractionException;

import org.apache.commons.beanutils.PropertyUtils;

public class JavaBeanFactExtractor extends JavaFactExtractor
{
    public JavaBeanFactExtractor(Declaration objectDecl)
    {
        super( objectDecl );
    }

    public Object extractFactFromObject(Object object) throws FactExtractionException
    {
        return null;
    }
}
