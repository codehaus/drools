
package org.drools.semantic.java.io;

import org.dom4j.ElementHandler;
import org.dom4j.ElementPath;

abstract class BaseRuleSetHandler implements ElementHandler
{
    private RuleSetReader reader;

    BaseRuleSetHandler(RuleSetReader reader)
    {
        this.reader = reader;
    }

    public RuleSetReader getReader()
    {
        return this.reader;
    }

    public void onStart(ElementPath path)
    {
        // intentially left blank
    }
}
