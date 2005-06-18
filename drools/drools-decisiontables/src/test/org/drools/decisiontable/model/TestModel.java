/*
 * Created on 15/06/2005
 */
package org.drools.decisiontable.model;

/**
 * 
 * @author <a href="mailto:michael.neale@gmail.com"> Michael Neale</a>
 * 
 * This class is just to test end to end rule execution.
 */
public class TestModel
{

    private boolean fireRule  = true;

    private boolean ruleFired = false;

    /**
     * @return Returns the fireRule.
     */
    public boolean isFireRule()
    {
        return fireRule;
    }

    /**
     * @param rule
     *            The fireRule to set.
     */
    public void setFireRule(boolean rule)
    {
        fireRule = rule;
    }

    /**
     * @return Returns the ruleFired.
     */
    public boolean isRuleFired()
    {
        return ruleFired;
    }

    /**
     * @param fired
     *            The ruleFired to set.
     */
    public void setRuleFired(boolean fired)
    {
        ruleFired = fired;
    }

}
