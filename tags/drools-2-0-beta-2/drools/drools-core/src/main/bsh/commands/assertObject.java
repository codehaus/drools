
package bsh.commands;

import bsh.Interpreter;
import bsh.NameSpace;
import bsh.EvalError;

import org.drools.WorkingMemory;
import org.drools.AssertionException;

public class assertObject
{
    public static void invoke(Interpreter interp,
                              NameSpace namespace,
                              Object object) throws EvalError, AssertionException
    {
        WorkingMemory workingMemory = (WorkingMemory) interp.get( "drools$working$memory" );

        workingMemory.assertObject( object );
    }
}
