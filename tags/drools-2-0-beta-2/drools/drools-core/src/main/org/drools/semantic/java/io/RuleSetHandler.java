
package org.drools.semantic.java.io;

import org.drools.spi.RuleSet;

import org.dom4j.ElementPath;
import org.dom4j.Element;

class RuleSetHandler extends BaseRuleSetHandler
{
    RuleSetHandler(RuleSetReader reader)
    {
        super( reader );
    }

    public void onStart(ElementPath path)
    {
        Element elem = path.getCurrent();

        String name = elem.attributeValue( "name" );

        getReader().setRuleSet( new RuleSet( name ) );
    }

    public void onEnd(ElementPath path)
    {
        // intentionally left blank
    }
}
