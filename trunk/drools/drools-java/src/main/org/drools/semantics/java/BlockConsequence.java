package org.drools.semantics.java;

/*
 $Id: BlockConsequence.java,v 1.22 2004-07-28 13:34:31 mproctor Exp $

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

import org.drools.WorkingMemory;
import org.drools.spi.Consequence;
import org.drools.spi.ConsequenceException;
import org.drools.spi.Tuple;

import net.janino.ScriptEvaluator;
import net.janino.Scanner;
import java.io.StringReader;

import java.util.Arrays;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.Comparator;
import java.util.Collections;

import org.drools.rule.Declaration;
import org.drools.spi.ObjectType;

import org.drools.spi.KnowledgeHelper;

/** Java block semantics <code>Consequence</code>.
 *
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 *
 *  @version $Id: BlockConsequence.java,v 1.22 2004-07-28 13:34:31 mproctor Exp $
 */
public class BlockConsequence extends Interp
    implements Consequence
{
    /** Interpreted text. */
    private String newline = System.getProperty("line.separator");

    private Script script;

    private static final String[] scriptParamNames = new String[] {"tuple", "decls", "drools", "applicationData"};
    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     *
     *  @param block The statement block.
     */
    public BlockConsequence(String block,
                            Declaration[] availDecls)
        throws Exception
    {
        super(block);
    }

    private void compile(Tuple tuple, Declaration[] availDecls) throws Exception
    {
        String block = getPreparedText(tuple, availDecls);

        //System.err.println("block-----" );
        //System.err.println(block);
        //System.err.println("----------" );
        try
        {
            //code = new ScriptEvaluator(block, Void.TYPE, paramNames, paramTypes, new Class[] {Exception.class});
           script = (Script) ScriptEvaluator.createFastScriptEvaluator(
                             new Scanner(null, new StringReader(block)),
                             Script.class,
                             scriptParamNames,
                             (ClassLoader) null);          // Use current thread's context class loader

        }
        catch (Exception e)
        {
            e.printStackTrace();
            throw e;
        }
        /*
        catch (net.janino.Java.CompileException e)
        {
          throw e;
        }
        catch (net.janino.Parser.ParseException e)
        {
          throw e;
        }
        catch (net.janino.Scanner.ScanException e)
        {
          throw e;
        }
        catch (java.io.IOException e)
        {
          throw e;
        }
        */
    }



    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //     org.drools.spi.Consequence
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /** Execute the consequence for the supplied
     *  matching <code>Tuple</code>.
     *
     *  @param tuple The matching tuple.
     *  @param workingMemory The working memory session.
     *
     *  @throws ConsequenceException If an error occurs while
     *          attempting to invoke the consequence.
     */
    public void invoke(Tuple tuple,
                       WorkingMemory workingMemory)
        throws ConsequenceException
    {
        try
        {
            List decls = new ArrayList( tuple.getDeclarations() );

            Collections.sort( decls,
                              new Comparator() {
                                  public int compare(Object left,
                                                     Object right) {
                                      return ((Declaration)left).getIdentifier().compareTo( ((Declaration)right).getIdentifier() );
                                  }

                              } );


            Declaration[] params = (Declaration[]) decls.toArray(Declaration.EMPTY_ARRAY) ;

            if ( script == null ) {
                compile(tuple, params);
            }
            script.invoke(tuple,
                               params,
                               new KnowledgeHelper( tuple ),
                               tuple.getWorkingMemory().getApplicationDataMap());

            /*
            Object[] paramValues = new Object[ params.length + 2];

            paramValues[0] = new KnowledgeHelper( tuple );
            paramValues[1] = workingMemory.getApplicationDataMap();

            for ( int i = 0 ; i < params.length ; i++ ) {
                paramValues[i + 2] = tuple.get( params[i] );
            }
            code.evaluate( paramValues );
            */
        }
        catch (Exception e)
        {
            throw new ConsequenceException( e );
        }
    }

    /** Retrieve the expression.
    *
    *  @return The expression.
    */
   public String getBlock()
   {
       return getText();
   }

    public interface Script
    {
        public void invoke(Tuple tuple,
                           Declaration[] decls,
                           KnowledgeHelper drools,
                           Map applicationData) throws Exception;
    }

}
