/*
 * Created on Oct 21, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.drools.smf;

import org.drools.rule.Declaration;
import org.drools.rule.Rule;

import java.util.SortedSet;
import java.util.TreeSet;

/**
 * @author mproctor
 */
public class InstrumentedRule extends Rule
{

    private SortedSet allDeclarations;

    public InstrumentedRule(String name)
    {
        super(name);
    }

    public void setDeclarations(Declaration[] declarations)
    {
        allDeclarations = new TreeSet();
        for (int i = 0; i < declarations.length; i++)
        {
            allDeclarations.add(declarations[i]);
        }
    }

    public SortedSet getAllDeclarations()
    {
        return this.allDeclarations;
    }
}
