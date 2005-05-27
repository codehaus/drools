package org.drools.jsr94.rules.admin;

/*
 * $Id: RuleImpl.java,v 1.11 2004-11-27 00:59:54 dbarnett Exp $
 *
 * Copyright 2002-2004 (C) The Werken Company. All Rights Reserved.
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

import java.util.HashMap;
import java.util.Map;

import javax.rules.admin.Rule;

/**
 * The Drools implementation of the <code>Rule</code> interface which provides
 * access to simple metadata for a rule. Related <code>Rule</code>
 * instances are assembled into <code>RuleExecutionSet</code>s, which in
 * turn, can be executed by a rules engine via the <code>RuleSession</code>
 * interface.
 *
 * @see Rule
 *
 * @author N. Alex Rupp (n_alex <at>codehaus.org)
 * @author <a href="mailto:thomas.diesler@softcon-itec.de">thomas diesler </a>
 */
public class RuleImpl implements Rule
{
    /** The name of this rule. */
    private String name;

    /** A description of the rule or null if no description is specified. */
    private String description;

    /** A <code>Map</code> of user-defined and Drools-defined properties. */
    private Map properties = new HashMap( );

    /**
     * The <code>org.drools.rule.Rule</code> that lies at the core of
     * this <code>javax.rules.admin.Rule</code> object.
     */
    private org.drools.rule.Rule rule;

    /**
     * Creates a <code>RuleImpl</code> object by wrapping an
     * <code>org.drools.rule.Rule</code> object.
     *
     * @param rule the <code>org.drools.rule.Rule</code> object to be wrapped.
     */
    RuleImpl( org.drools.rule.Rule rule )
    {
        this.rule = rule;
        this.name = rule.getName( );
        this.description = rule.getDocumentation( );
    }

    /**
     * Returns the <code>org.drools.rule.Rule</code> that lies at the core of
     * this <code>javax.rules.admin.Rule</code> object. This method is package
     * private.
     *
     * @return <code>org.drools.rule.Rule</code> at the core of this object.
     */
    org.drools.rule.Rule getRule( )
    {
        return this.rule;
    }

    /* Rule interface methods */

    /**
     * Get the name of this rule.
     *
     * @return The name of this rule.
     */
    public String getName( )
    {
        return this.name;
    }

    /**
     * Get a description of the rule.
     *
     * @return A description of the rule or null of no description is specified.
     */
    public String getDescription( )
    {
        return this.description;
    }

    /**
     * Get a user-defined or Drools-defined property.
     *
     * @param key the key to use to retrieve the property
     *
     * @return the value bound to the key or <code>null</code>
     */
    public Object getProperty( Object key )
    {
        // TODO certain keys should reference internal rule accessor methods
        return this.properties.get( key );
    }

    /**
     * Set a user-defined or Drools-defined property.
     *
     * @param key the key for the property value
     * @param value the value to associate with the key
     */
    public void setProperty( Object key, Object value )
    {
        // TODO certain keys should alter internal rule accessor methods
        this.properties.put( key, value );
    }
}
