package org.drools.semantics.java;

/*
 $Id: ExprExtractor.java,v 1.6 2002-08-19 04:30:49 bob Exp $

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

import org.drools.rule.Declaration;
import org.drools.smf.ConfigurableExtractor;
import org.drools.smf.ConfigurationException;
import org.drools.spi.Tuple;
import org.drools.spi.ExtractionException;

/** Java expression semantics <code>Extractor</code>.
 * 
 *  @author <a href="mailto:bob@werken.com">bob@werken.com</a>
 *
 *  @version $Id: ExprExtractor.java,v 1.6 2002-08-19 04:30:49 bob Exp $
 */
public class ExprExtractor extends Expr implements ConfigurableExtractor
{
    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct, partially.
     *
     *  @see Expr#configure
     */
    public ExprExtractor()
    {
        // intentionally left blank
    }

    /** Construct.
     *
     *  @param expr The expression.
     *  @param availDecls The available declarations.
     *
     *  @throws ConfigurationException If an error occurs while
     *          attempting to perform configuration.
     */
    public ExprExtractor(String expr,
                         Declaration[] availDecls) throws ConfigurationException
    {
        super( expr,
               availDecls );
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 
    //     org.drools.spi.Extractor
    // - - - - - - - - - - - - - - - - - - - - - - - - - - - - - - 

    /** Extract a new fact from the incoming <code>Tuple</code>
     *
     *  @param tuple The source data tuple.
     *
     *  @return The newly extract fact object.
     *
     *  @throws ExtractionException if an error occurs during
     *          fact extraction activities.
     */
    public Object extractFact(Tuple tuple) throws ExtractionException
    {
        try
        {
            return evaluate( tuple );
        }
        catch (Exception e)
        {
            throw new ExtractionException( e );
        }
    }
}
