
package org.drools.semantic.java;

import org.drools.WorkingMemory;
import org.drools.spi.Declaration;
import org.drools.spi.Tuple;
import org.drools.spi.Action;
import org.drools.spi.ActionInvokationException;

import bsh.Interpreter;
import bsh.EvalError;

import java.util.Set;
import java.util.Iterator;

public class BeanShellAction implements Action
{
    private String      script;
    private Interpreter interp;

    public BeanShellAction(String script)
    {
        this.script = script;

        initializeInterpreter();
    }

    private void initializeInterpreter()
    {
        this.interp = new Interpreter();
    }

    public String getScript()
    {
        return this.script;
    }

    public void invoke(Tuple tuple,
                       WorkingMemory workingMemory) throws ActionInvokationException
    {

        try
        {
            BeanShellUtil.setUpInterpreter( this.interp,
                                            tuple );
            
            this.interp.eval( this.getScript() );
            
            BeanShellUtil.cleanUpInterpreter( this.interp,
                                              tuple );
        }
        catch (EvalError e)
        {
            initializeInterpreter();
            throw new ActionInvokationException( e );
        }
    }
}
