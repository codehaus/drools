
package bsh.commands;

import bsh.Interpreter;
import bsh.NameSpace;
import bsh.EvalError;

import org.drools.WorkingMemory;
import org.drools.FactException;

public class modifyObject
{
    public static void invoke(Interpreter interp,
                              NameSpace namespace,
                              Object object) throws EvalError, FactException
    {
        WorkingMemory workingMemory = (WorkingMemory) interp.get( "drools$working$memory" );

        workingMemory.modifyObject( object );
    }
}
