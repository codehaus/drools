package org.drools.semantics.java.bsh;

/*
 $Id: modifyObject.java,v 1.3 2004-06-15 18:24:17 bob Exp $

 Copyright 2002 (C) The Werken Company. All Rights Reserved.

 Redistribution and use of this software and associated documentation
 ("Software"), with or without modification, are permitted provided
 that the following conditions are met:

 1. Redistributions of source code must retain copyright
    statements and notices.  Redistributions must also contain a
    copy of this document.

 2. Redistributions in binary form must reproduce the
    above copyright notice, this list of conditions and the
    following disclaimer in the documentation and/or other
    materials provided with the distribution.

 3. The name "drools" must not be used to endorse or promote
    products derived from this Software without prior written
    permission of The Werken Company.  For written permission,
    please contact bob@werken.com.

 4. Products derived from this Software may not be called "drools"
    nor may "drools" appear in their names without prior written
    permission of The Werken Company. "drools" is a registered
    trademark of The Werken Company.

 5. Due credit should be given to The Werken Company.
    (http://drools.werken.com/).

 THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS
 ``AS IS'' AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT
 NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL
 THE WERKEN COMPANY OR ITS CONTRIBUTORS BE LIABLE FOR ANY DIRECT,
 INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES
 (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION)
 HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT,
 STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED
 OF THE POSSIBILITY OF SUCH DAMAGE.

 */

import bsh.Interpreter;
import bsh.CallStack;
import bsh.EvalError;

import org.drools.WorkingMemory;
import org.drools.FactException;
import org.drools.spi.Tuple;

/** Modify an object in the working memory.
 *
 *  @see WorkingMemory
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
public class modifyObject
{
    /** Invoke the command.
     *
     *  @param interp The bsh interpreter.
     *  @param namespace The bsh namespace.
     *  @param object The object to modify.
     *
     *  @throws EvalError If a bsh evaluation error occurs.
     *  @throws FactException If there is a RETE fact modification exception.
     */
    public static void invoke(Interpreter interp,
                              CallStack callstack,
                              Object object) throws EvalError, FactException
    {
        WorkingMemory workingMemory = (WorkingMemory) interp.get( "drools$working$memory" );
        Tuple         tuple         = (Tuple) interp.get( "drools$tuple" );

        workingMemory.modifyObject( tuple.getFactHandleForObject( object ),
                                    object );
    }
}
