
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

/** Rule {@link Action} using <a hef="http://beanshell.org/">BeanShell</a>
 *  for execution.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class BeanShellAction implements Action
{
    /** The BeanShell script. */
    private String      script;

    /** The BeanShell interpreter. */
    private Interpreter interp;

    /** Construct.
     *
     *  @param script The BeanShell script to execute.
     */
    public BeanShellAction(String script)
    {
        this.script = script;

        initializeInterpreter();
    }

    /** Initialize the interpreter
     */
    private void initializeInterpreter()
    {
        this.interp = new Interpreter();
    }

    /** Retrieve the BeanShell script.
     *
     *  @return The script.
     */
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

            this.interp.set( "drools$working$memory",
                             workingMemory );
            
            this.interp.eval( this.getScript() );

            this.interp.unset( "drools$working$memory" );
            
            BeanShellUtil.cleanUpInterpreter( this.interp,
                                              tuple );
        }
        catch (EvalError e)
        {
            initializeInterpreter();
            throw new ActionInvokationException( e );
        }
    }

    public String toString()
    {
        return "[" + this.script + "]";
    }
}
