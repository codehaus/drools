package org.drools.jsr94.rules.admin;

/*
 $Id: RuleExecutionSetImpl.java,v 1.10 2004-06-29 15:44:22 n_alex Exp $

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

import org.drools.RuleBase;
import org.drools.WorkingMemory;
import org.drools.RuleIntegrationException;
import org.drools.jsr94.rules.Constants;
import org.drools.rule.Rule;
import org.drools.rule.RuleSet;

import javax.rules.ObjectFilter;
import javax.rules.admin.RuleExecutionSet;
import java.util.*;

/**
 * A named set of executable <code>Rule</code> instances.
 * A <code>RuleExecutionSet</code> can be executed by a rules engine via the <code>RuleSession</code> interface.
 *
 * @see RuleExecutionSet
 *
 * @author N. Alex Rupp (n_alex <at> codehaus.org)
 * @author <a href="mailto:thomas.diesler@softcon-itec.de">thomas diesler</a>
 */
public class RuleExecutionSetImpl implements RuleExecutionSet
{
    private String description;
    private String defaultObjectFilterClassName;
    private Map properties;
    private RuleBase ruleBase;
    private RuleSet ruleSet;
    private ObjectFilter objectFilter;

    /**
     * <p>Instances of this class should be obtained from the
     * <code>LocalRuleExecutionSetProviderImpl</code>. Each
     * <code>RuleExecutionSetImpl</code> corresponds with an
     * <code>org.drools.RuleSet</code> object.
     */
    RuleExecutionSetImpl(
            RuleSet ruleSet,
            Map properties)
    {
        this.properties = properties;
        this.ruleSet = ruleSet;

        if(properties != null && properties.containsKey(Constants.RES_DESCRIPTION)) {
            this.description = (String)properties.get(Constants.RES_DESCRIPTION);
        }

        org.drools.RuleBaseBuilder builder = new org.drools.RuleBaseBuilder();
        try {
            builder.addRuleSet( ruleSet );
            this.ruleBase = builder.build();
        } catch (RuleIntegrationException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get an instance of the default filter, or null.
     */
    public synchronized ObjectFilter getObjectFilter()
    {
        if ( this.objectFilter == null )
        {
            if ( this.defaultObjectFilterClassName != null )
            {
                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                
                if ( cl == null )
                {
                    cl = RuleExecutionSetImpl.class.getClassLoader();
                }
                
                try
                {
                    Class filterClass = cl.loadClass( defaultObjectFilterClassName );
                    this.objectFilter = (ObjectFilter) filterClass.newInstance();
                }
                catch (Exception e)
                {
                    throw new RuntimeException( e.toString() );
                }
            }
        }
        
        return this.objectFilter;
    }

    /**
     * Returns a new WorkingMemory object
     * @return
     */
    public WorkingMemory newWorkingMemory()
    {
        return this.ruleBase.newWorkingMemory();
    }

    // JSR94 interface methods start here -------------------------------------
    public String getName()
    {
        return this.ruleSet.getName();
    }

    public String getDescription()
    {
        return description;
    }

    public Object getProperty( Object key )
    {
        return properties.get( key );
    }

    public void setProperty( Object key, Object val )
    {
        properties.put( key, val );
    }

    public void setDefaultObjectFilter( String objectFilterClassname )
    {
        this.defaultObjectFilterClassName = objectFilterClassname;
    }

    public String getDefaultObjectFilter()
    {
        return defaultObjectFilterClassName;
    }

    /**
     * Returns a list of RuleImpl objects.
     * @return
     */
    public List getRules()
    {
        List jsr94Rules = new ArrayList();
        {
            int i;
            Rule[] rules= (ruleSet.getRules());
            for( i=0; i < rules.length; i++ ) {
                jsr94Rules.add(new RuleImpl(rules[i]));
            }
        }
        return jsr94Rules;
    }
}