package org.drools.semantics.jelly;

/*
 $Id: JellyConsequence.java,v 1.7 2003-03-25 19:47:32 tdiesler Exp $

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

import org.apache.commons.jelly.JellyContext;
import org.apache.commons.jelly.Script;
import org.apache.commons.jelly.XMLOutput;
import org.drools.WorkingMemory;
import org.drools.rule.Declaration;
import org.drools.spi.Consequence;
import org.drools.spi.ConsequenceException;
import org.drools.spi.Tuple;

import java.util.Iterator;
import java.util.Set;

/** Jelly-script semantics <code>Consequence</code>.
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirteR</a>
 *
 *  @version $Id: JellyConsequence.java,v 1.7 2003-03-25 19:47:32 tdiesler Exp $
 */
public class JellyConsequence implements Consequence
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** The script. */
    private Script script;

    /** The output sink. */
    private XMLOutput output;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     *
     *  @param script The script to execute when matched.
     *  @param output The output sink for the script.
     */
    public JellyConsequence(Script script,
                            XMLOutput output)
    {
        this.script = script;
        this.output = output;
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
                       WorkingMemory workingMemory) throws ConsequenceException
    {
        JellyContext context = new JellyContext();

        Set decls = tuple.getDeclarations();

        Iterator    declIter = decls.iterator();
        Declaration eachDecl = null;
        Object      eachObj  = null;

        while ( declIter.hasNext() )
        {
            eachDecl = (Declaration) declIter.next();

            context.setVariable( eachDecl.getIdentifier(),
                                 tuple.get( eachDecl ) );

        }

        WorkingMemory origMemory = null;

        try
        {
            origMemory = (WorkingMemory) context.getVariable( "org.drools.working-memory" );

            context.setVariable( "org.drools.working-memory",
                                 workingMemory );

            context.setVariable( "appData",
                                 workingMemory.getApplicationData() );
            
            this.script.run( context,
                             this.output  );
        }
        catch (Exception e)
        {
            throw new ConsequenceException( e );
        }
        finally
        {
            if ( origMemory != null )
            {
                context.setVariable( "org.drools.working-memory",
                                     origMemory );
            }
        }
    }
}
