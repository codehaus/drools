/*
 * Created on Oct 21, 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package org.drools.smf;

import java.util.HashSet;
import java.util.Set;

import org.drools.rule.Declaration;
import org.drools.rule.Rule;

/**
 * @author mproctor
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class InstrumentedRule extends Rule
{
    
    private Set                allDeclarations;
    
    public InstrumentedRule(String name)
    {
        super(name);
    }
    
    public void setDeclarations(Declaration[] declarations)
    {        
        allDeclarations = new HashSet();
        for (int i = 0; i < declarations.length; i++)
        {
            allDeclarations.add(declarations[i]);
        }
    }

    /**
     * Retrieve the array of all <code>Declaration</code> s of this rule.
     * 
     * @return The array of declarations.
     */
    public Declaration[] getAllDeclarations()
    {
        return ( Declaration[] ) this.allDeclarations
                                                     .toArray( Declaration.EMPTY_ARRAY );
    }    

}
