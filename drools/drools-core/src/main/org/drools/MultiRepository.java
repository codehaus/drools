package org.drools;

/*
 $Id: MultiRepository.java,v 1.3 2002-08-02 19:43:11 bob Exp $

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

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;

/** Aggregating <code>RuleBaseRepository</code> that searches
 *  multiple repositories, in order.
 *
 *  @author <a href="mailto:bob@eng.werken.com">bob mcwhirter</a>
 */
public class MultiRepository implements RuleBaseRepository
{
    // ------------------------------------------------------------
    //     Instance members
    // ------------------------------------------------------------

    /** Ordered list of repositories. */
    private List repositories;

    // ------------------------------------------------------------
    //     Constructors
    // ------------------------------------------------------------

    /** Construct.
     */
    public MultiRepository()
    {
        this.repositories = new ArrayList();
    }

    // ------------------------------------------------------------
    //     Instance methods
    // ------------------------------------------------------------

    /** Add a repository to the end of the search list.
     *
     *  @param repository The repository to add.
     */
    public void addRepository(RuleBaseRepository repository)
    {
        this.repositories.add( repository );
    }

    /** Retrieve a <code>RuleBase</code> by URI.
     *
     *  @param uri The identifying URI of the <code>RuleBase</code>.
     *
     *  @return The located <code>RuleBase</code>.
     *
     *  @throws NoSuchRuleBaseException If no <code>RuleBase</code>
     *          can be located.
     */
    public RuleBase lookupRuleBase(String uri) throws NoSuchRuleBaseException
    {
        Iterator           repoIter = this.repositories.iterator();
        RuleBaseRepository eachRepo = null;

        RuleBase ruleBase = null;

        while ( repoIter.hasNext() )
        {
            eachRepo = (RuleBaseRepository) repoIter.next();

            try
            {
                ruleBase = eachRepo.lookupRuleBase( uri );
                return ruleBase;
            }
            catch (NoSuchRuleBaseException e)
            {
                // ignore
            }
        }

        throw new NoSuchRuleBaseException( uri );
    }

    /** Register a <code>RuleBase</code> by URI.
     *
     *  @param uri The uri.
     *  @param ruleBase The rule base.
     */
    public void registerRuleBase(String uri,
                                 RuleBase ruleBase)
    {
        throw new UnsupportedOperationException( "MultiRepository.regusterRuleBase(..)" );
    }
}
