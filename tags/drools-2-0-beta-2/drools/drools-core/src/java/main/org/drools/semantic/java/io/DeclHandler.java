
package org.drools.semantic.java.io;

import org.drools.semantic.java.JavaObjectType;

import org.drools.spi.Declaration;
import org.drools.spi.DeclarationAlreadyCompleteException;

import org.dom4j.Element;
import org.dom4j.ElementPath;

class DeclHandler extends BaseRuleSetHandler
{
    DeclHandler(RuleSetReader reader)
    {
        super( reader );
    }

    public void onEnd(ElementPath path)
    {
        Element elem = path.getCurrent();

        String type       = elem.attributeValue( "type" );
        String identifier = elem.getTextTrim();

        try
        {
            Class typeClass = getReader().getImportManager().resolveClass( type );
            
            Declaration decl = new Declaration( new JavaObjectType( typeClass ),
                                                identifier );
            
            getReader().addDeclaration( decl );
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}

