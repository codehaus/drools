
package org.drools.semantic.java.io;

import org.dom4j.Element;
import org.dom4j.ElementPath;

class ImportHandler extends BaseRuleSetHandler
{
    ImportHandler(RuleSetReader reader)
    {
        super( reader );
    }

    public void onEnd(ElementPath path)
    {
        Element elem = path.getCurrent();

        getReader().getImportManager().addImport( elem.getTextTrim() );
    }
}
