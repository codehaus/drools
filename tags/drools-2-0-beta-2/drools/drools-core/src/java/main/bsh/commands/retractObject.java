
package bsh.commands;

import bsh.Interpreter;
import bsh.NameSpace;
import bsh.EvalError;

import org.drools.WorkingMemory;
import org.drools.RetractionException;

public class retractObject
{
    public static void invoke(Interpreter interp,
                              NameSpace namespace,
                              Object object) throws EvalError, RetractionException
    {
        WorkingMemory workingMemory = (WorkingMemory) interp.get( "drools$working$memory" );

        workingMemory.retractObject( object );
    }
}
