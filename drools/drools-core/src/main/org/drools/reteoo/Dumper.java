package org.drools.reteoo;

/*
 * $Id: Dumper.java,v 1.10 2005-12-31 10:05:50 michaelneale Exp $
 * 
 * Copyright 2004-2004 (C) The Werken Company. All Rights Reserved.
 * 
 * Redistribution and use of this software and associated documentation
 * ("Software"), with or without modification, are permitted provided that the
 * following conditions are met:
 * 
 * 1. Redistributions of source code must retain copyright statements and
 * notices. Redistributions must also contain a copy of this document.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 * this list of conditions and the following disclaimer in the documentation
 * and/or other materials provided with the distribution.
 * 
 * 3. The name "drools" must not be used to endorse or promote products derived
 * from this Software without prior written permission of The Werken Company.
 * For written permission, please contact bob@werken.com.
 * 
 * 4. Products derived from this Software may not be called "drools" nor may
 * "drools" appear in their names without prior written permission of The Werken
 * Company. "drools" is a trademark of The Werken Company.
 * 
 * 5. Due credit should be given to The Werken Company. (http://werken.com/)
 * 
 * THIS SOFTWARE IS PROVIDED BY THE WERKEN COMPANY AND CONTRIBUTORS ``AS IS''
 * AND ANY EXPRESSED OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE WERKEN COMPANY OR ITS CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 *  
 */

import java.io.PrintStream;

import org.drools.RuleBase;

/**
 * A rete network dumper. This is an internal implementation.
 * Users should use ReteDumper in the IO module.
 * 
 * @author <a href="mailto:bob@werken.com">bob mcwhirter </a>
 * 
 * @version $Id: Dumper.java,v 1.10 2005-12-31 10:05:50 michaelneale Exp $
 */
public class Dumper
{
    private RuleBaseImpl ruleBase;

    /**
     * You must pass in a real rule base.
     * If you are using RuleBaseLoader, it will provide a serializable proxy.
     * Use the .getWrappedRuleBase method to get the real rulebase.
     * @param ruleBase
     */
    public Dumper(RuleBase ruleBase)
    {
        this.ruleBase = (RuleBaseImpl) ruleBase;
    }

    public void dumpRete(PrintStream out)
    {
        dumpRete( out,
                  "  " );
    }

    public void dumpRete(PrintStream out,
                         String indent)
    {
        new ReteooPrintDumpVisitor( out,
                                    indent ).visit( ruleBase );
    }

    /**
     * Compatible with the GraphViz DOT format.
     */
    public void dumpReteToDot(PrintStream out)
    {
        out.println( "digraph RETEOO {" );
        new ReteooDotDumpVisitor( out ).visit( ruleBase );
        out.println( "}" );
    }
}
