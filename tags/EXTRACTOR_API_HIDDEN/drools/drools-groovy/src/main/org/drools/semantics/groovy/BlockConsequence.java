package org.drools.semantics.groovy;

/*
 * $Id: BlockConsequence.java,v 1.11 2004-11-22 02:38:37 simon Exp $
 *
 * Copyright 2002 (C) The Werken Company. All Rights Reserved.
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
 * Company. "drools" is a registered trademark of The Werken Company.
 *
 * 5. Due credit should be given to The Werken Company.
 * (http://drools.werken.com/).
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

import groovy.lang.Binding;
import org.drools.WorkingMemory;
import org.drools.spi.Consequence;
import org.drools.spi.ConsequenceException;
import org.drools.spi.Tuple;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * Groovy block semantics <code>Consequence</code>.
 *
 * @author <a href="mailto:james@coredevelopers.net">James Strachan </a>
 * @author <a href="mailto:bob@eng.werken.com">bob mcwhirter </a>
 * @author <a href="mailto:ckl@dacelo.nl">Christiaan ten Klooster </a>
 *
 * @version $Id: BlockConsequence.java,v 1.11 2004-11-22 02:38:37 simon Exp $
 */
public class BlockConsequence extends Exec implements Consequence
{
    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /**
     * Construct.
     *
     * @param text The block text.
     */
    public BlockConsequence(String text, Set imports)
    {
        super( text, imports );
    }

    /**
     * Default constructor - required for serialization
     */
    public BlockConsequence()
    {
        super( );
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -
    //     org.drools.spi.Consequence
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - -

    /**
     * Execute the consequence for the supplied matching <code>Tuple</code>.
     *
     * @param tuple The matching tuple.
     * @param workingMemory The working memory session.
     *
     * @throws ConsequenceException If an error occurs while attempting to
     *         invoke the consequence.
     */
    public void invoke(Tuple tuple, WorkingMemory workingMemory) throws ConsequenceException
    {
        Binding dict = setUpDictionary( tuple );

        dict.setVariable( "__drools_working_memory", workingMemory );
        Map appData = workingMemory.getApplicationDataMap( );
        Map.Entry entry;
        for ( Iterator iterator = appData.entrySet( ).iterator( ); iterator.hasNext(); )
        {
            entry = ( Map.Entry ) iterator.next( );
            dict.setVariable( ( String ) entry.getKey( ), entry.getValue( ) );
        }

        try
        {
            execute( dict );
        }
        catch ( Exception e )
        {
            throw new ConsequenceException( e, tuple.getRule( ) );
        }
    }
}