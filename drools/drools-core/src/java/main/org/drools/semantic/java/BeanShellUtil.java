
package org.drools.semantic.java;

import org.drools.rule.Declaration;
import org.drools.spi.Tuple;

import bsh.Interpreter;
import bsh.EvalError;

import java.util.Set;
import java.util.Iterator;

/** Utilities for merging the Tuple into a form usable
 *  by <a href="http://beanshell.org/">BeanShell.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 */
public class BeanShellUtil
{
    /** Purely static methods.
     */
    private BeanShellUtil()
    {
    }
    
    /** Set up the interpreter with bindings to the
     *  members of the tuple.
     *
     *  @param interp The interpreter to configure.
     *  @param tuple The tuple to map into the interpreter's namespace.
     */
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

    /** Clean up the interpreter's bindings to the
     *  members of the tuple.
     *
     *  @param interp The interpreter to clean up.
     *  @param tuple The tuple to unmap from the interpreter's namespace.
     */
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
