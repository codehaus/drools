/*
 * Created on Oct 21, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.drools.smf;

import org.drools.rule.Declaration;
import org.drools.rule.Rule;

import java.util.Arrays;
import java.util.List;
import java.util.ArrayList;

/**
 * @author mproctor
 */
public class InstrumentedRule extends Rule
{

    private List allDeclarations;

    public InstrumentedRule(String name)
    {
        super(name);
    }

    public void setDeclarations(Declaration[] declarations)
    {
        // Make a defensive copy!
        allDeclarations = new ArrayList( Arrays.asList( declarations ) );
    }

    public List getAllDeclarations()
    {
        return this.allDeclarations;
    }
}
