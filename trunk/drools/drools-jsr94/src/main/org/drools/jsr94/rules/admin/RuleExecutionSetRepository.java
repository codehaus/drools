package org.drools.jsr94.rules.admin;

/*
 * $Id: RuleExecutionSetRepository.java,v 1.6 2004-09-17 00:29:40 mproctor Exp $
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.rules.admin.RuleExecutionSet;

/**
 * Stores the registered <code>RuleExecutionSet</code> objects.
 * 
 * @author <a href="mailto:thomas.diesler@softcon-itec.de">thomas diesler </a>
 */
public class RuleExecutionSetRepository
{
    // holds the registered <code>RuleExecutionSet</code> objects.
    private Map                               map = new HashMap( );

    private static RuleExecutionSetRepository repository;

    /** Hide the constructor. */
    private RuleExecutionSetRepository()
    {
    }

    /** Get the class instance of a <code>RuleExecutionSetRepository</code>. */
    public static RuleExecutionSetRepository getInstance()
    {
        if ( repository != null ) return repository;
        return repository = new RuleExecutionSetRepository( );
    }

    /**
     * Retrieves a List of the URIs that currently have
     * <code>RuleExecutionSets</code> associated with them.
     * 
     * An empty list is returned is there are no associations.
     */
    public List getRegistrations()
    {
        List list = new ArrayList( );
        list.addAll( map.keySet( ) );
        return list;
    }

    /**
     * Get the <code>RuleExecutionSet</code> bound to this URI, or return
     * null.
     */
    public RuleExecutionSet getRuleExecutionSet(String bindUri)
    {
        return ( RuleExecutionSet ) map.get( bindUri );
    }

    /**
     * Register a <code>RuleExecutionSet</code> under the given URI.
     */
    public void registerRuleExecutionSet(String bindUri,
                                         RuleExecutionSet ruleSet)
    {
        if ( bindUri == null ) throw new NullPointerException(
                                                               "bindUri cannot be null" );
        if ( ruleSet == null ) throw new NullPointerException(
                                                               "ruleSet cannot be null" );
        map.put( bindUri, ruleSet );
    }

    /**
     * Unregister a <code>RuleExecutionSet</code> from the given URI.
     */
    public void unregisterRuleExecutionSet(String bindUri)
    {
        if ( bindUri == null ) throw new NullPointerException(
                                                               "bindUri cannot be null" );
        map.remove( bindUri );
    }

}