
package org.drools.semantic.java.io;

import org.drools.spi.Rule;
import org.drools.spi.RuleSet;
import org.drools.spi.InvalidRuleException;
import org.drools.spi.DuplicateRuleNameException;

import org.dom4j.Element;
import org.dom4j.ElementPath;

class RuleHandler extends BaseRuleSetHandler
{
    public RuleHandler(RuleSetReader reader)
    {
        super( reader );
    }

    public void onStart(ElementPath path)
    {
        Element elem = path.getCurrent();
        String  name = elem.attributeValue( "name" );

        Rule rule = new Rule( name );

        getReader().setCurrentRule( rule );
    }

    public void onEnd(ElementPath path)
    {
        Rule rule = getReader().getCurrentRule();

        try
        {
            getReader().getRuleSet().addRule( rule );
            getReader().setCurrentRule( null );
        }
        catch (InvalidRuleException e)
        {
            e.printStackTrace();
        }
        catch (DuplicateRuleNameException e)
        {
            e.printStackTrace();
        }
    }
}
