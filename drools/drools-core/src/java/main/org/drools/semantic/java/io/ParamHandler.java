
package org.drools.semantic.java.io;

import org.drools.semantic.java.JavaObjectType;

import org.drools.rule.Declaration;

import org.dom4j.Element;
import org.dom4j.ElementPath;

class ParamHandler extends BaseRuleSetHandler
{
    ParamHandler(RuleSetReader reader)
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
            
            getReader().getCurrentRule().addParameterDeclaration( decl );
            getReader().addDeclaration( decl );
        }
        catch (ClassNotFoundException e)
        {
            e.printStackTrace();
        }
    }
}

