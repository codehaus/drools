
package org.drools.semantic.java.io;

import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.rule.InvalidRuleException;
import org.drools.rule.DuplicateRuleNameException;

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

            System.err.println( e.getRule() );
        }
        catch (DuplicateRuleNameException e)
        {
            e.printStackTrace();
        }
    }
}
