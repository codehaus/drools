
package org.drools.semantic.java.io;

import org.drools.spi.Rule;
import org.drools.semantic.java.BeanShellAction;

import org.dom4j.Element;
import org.dom4j.ElementPath;

class ThenHandler extends BaseRuleSetHandler
{
    ThenHandler(RuleSetReader reader)
    {
        super( reader );
    }

    public void onEnd(ElementPath path)
    {
        Element elem = path.getCurrent();

        String thenBlock = elem.getText();

        Rule rule = getReader().getCurrentRule();

        rule.setAction( new BeanShellAction( thenBlock ) );
    }
}

