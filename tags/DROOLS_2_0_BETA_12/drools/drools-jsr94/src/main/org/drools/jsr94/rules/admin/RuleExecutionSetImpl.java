package org.drools.jsr94.rules.admin;

/*
 $Id: RuleExecutionSetImpl.java,v 1.6 2003-11-30 03:28:51 bob Exp $

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
import org.drools.RuleIntegrationException;
import org.drools.rule.Rule;

import javax.rules.ObjectFilter;
import javax.rules.admin.RuleExecutionSet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A named set of executable <code>Rule</code> instances.
 * A <code>RuleExecutionSet</code> can be executed by a rules engine via the <code>RuleSession</code> interface.
 *
 * @see RuleExecutionSet
 *
 * @author <a href="mailto:thomas.diesler@softcon-itec.de">thomas diesler</a>
 */
public class RuleExecutionSetImpl implements RuleExecutionSet
{

    private String name;
    private String description;
    private String filterName;
    private Map props;

    private RuleBase ruleBase;
    private List ruleList;

    private ObjectFilter objectFilter;

    /**
     * Hide the constructor.
     */
    RuleExecutionSetImpl()
    {
        this.props = new HashMap();
        //this.ruleBase = new RuleBase();
        this.ruleList = new ArrayList();
    }

    /**
     * Get the drool <code>RuleBase</code> associated with this <code>RuleExecutionSet</code>.
     */
    public RuleBase getRuleBase()
    {
        return ruleBase;
    }

    /**
     * Sets the name for this rule set.
     */
    void setName( String name )
    {
        this.name = name;
    }

    /**
     * Sets the description for this rule set.
     */
    void setDescription( String description )
    {
        this.description = description;
    }

    void addRules(Rule[] rules)
    {
        // FIXME
    }

    /**
     * Get an instance of the default filter, or null.
     */
    public synchronized ObjectFilter getObjectFilter()
    {
        if ( this.objectFilter == null )
        {
            if ( this.filterName != null )
            {
                ClassLoader cl = Thread.currentThread().getContextClassLoader();
                
                if ( cl == null )
                {
                    cl = RuleExecutionSetImpl.class.getClassLoader();
                }
                
                try
                {
                    Class filterClass = cl.loadClass( filterName );
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

    // JSR94 interface methods start here ------------------------------------------------------------------------------
    public String getName()
    {
        return name;
    }

    public String getDescription()
    {
        return description;
    }

    public Object getProperty( Object key )
    {
        return props.get( key );
    }

    public void setProperty( Object key, Object val )
    {
        props.put( key, val );
    }

    public void setDefaultObjectFilter( String objectFilterClassname )
    {
        this.filterName = objectFilterClassname;
    }

    public String getDefaultObjectFilter()
    {
        return filterName;
    }

    public List getRules()
    {
        return ruleList;
    }
    // JSR94 interface methods end here --------------------------------------------------------------------------------


}
