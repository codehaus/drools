
package org.drools.semantic.java;

import org.drools.spi.Tuple;
import org.drools.spi.Declaration;

import bsh.Interpreter;
import bsh.EvalError;

import java.util.Set;
import java.util.Iterator;

public class BeanShellUtil
{
    private BeanShellUtil()
    {
    }
    
    public static void setUpInterpreter(Interpreter interp,
                                        Tuple tuple) throws EvalError
    {
        Set         decls    = tuple.getDeclarations();

        Iterator    declIter = decls.iterator();
        Declaration eachDecl = null;

        while ( declIter.hasNext() )
        {
            eachDecl = (Declaration) declIter.next();
            
            interp.set( eachDecl.getIdentifier(),
                        tuple.get( eachDecl ) );
        }
    }

    public static void cleanUpInterpreter(Interpreter interp,
                                          Tuple tuple) throws EvalError
    {
        Set         decls    = tuple.getDeclarations();

        Iterator    declIter = decls.iterator();
        Declaration eachDecl = null;

        while ( declIter.hasNext() )
        {
            eachDecl = (Declaration) declIter.next();
            
            interp.unset( eachDecl.getIdentifier() );
        }
    }
}
