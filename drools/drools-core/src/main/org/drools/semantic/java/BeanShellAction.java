
package org.drools.semantic.java;

import org.drools.WorkingMemory;
import org.drools.spi.Tuple;
import org.drools.spi.Action;
import org.drools.spi.ActionInvokationException;

public class BeanShellAction implements Action
{
    private String script;

    public BeanShellAction(String script)
    {
        this.script = script;
    }

    public void invoke(Tuple tuple,
                       WorkingMemory workingMemory) throws ActionInvokationException
    {

    }
}
