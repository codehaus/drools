package org.drools.jsr94.rules.admin;

/*
 * $Id: RuleExecutionSetImpl.java,v 1.18 2004-11-28 20:01:13 mproctor Exp $
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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.rules.ObjectFilter;
import javax.rules.admin.RuleExecutionSet;

import org.drools.RuleBase;
import org.drools.RuleIntegrationException;
import org.drools.RuleSetIntegrationException;
import org.drools.WorkingMemory;
import org.drools.jsr94.rules.Jsr94FactHandleFactory;
import org.drools.rule.Rule;
import org.drools.rule.RuleSet;

/**
 * The Drools implementation of the <code>RuleExecutionSet</code> interface
 * which defines a named set of executable <code>Rule</code> instances. A
 * <code>RuleExecutionSet</code> can be executed by a rules engine via the
 * <code>RuleSession</code> interface.
 *
 * @see RuleExecutionSet
 *
 * @author N. Alex Rupp (n_alex <at>codehaus.org)
 * @author <a href="mailto:thomas.diesler@softcon-itec.de">thomas diesler </a>
 */
public class RuleExecutionSetImpl implements RuleExecutionSet
{
    /**
     * A description of this rule execution set or null if no
     * description is specified.
     */
    private String description;

    /**
     * The default ObjectFilter class name
     * associated with this rule execution set.
     */
    private String defaultObjectFilterClassName;

    /** A <code>Map</code> of user-defined and Drools-defined properties. */
    private Map properties;

    /**
     * The <code>RuleBase</code> associated with this
     * <code>RuleExecutionSet</code>.
     */
    private RuleBase ruleBase;

    /**
     * The <code>RuleSet</code> associated with this
     * <code>RuleExecutionSet</code>.
     */
    private RuleSet ruleSet;

    /**
     * The default ObjectFilter class name
     * associated with this rule execution set.
     */
    private ObjectFilter objectFilter;

    /**
     * Instances of this class should be obtained from the
     * <code>LocalRuleExecutionSetProviderImpl</code>. Each
     * <code>RuleExecutionSetImpl</code> corresponds with an
     * <code>org.drools.RuleSet</code> object.
     *
     * @param ruleSet The <code>RuleSet</code> to associate with this
     *        <code>RuleExecutionSet</code>.
     * @param properties A <code>Map</code> of user-defined and
     *        Drools-defined properties.
     */
    RuleExecutionSetImpl( RuleSet ruleSet, Map properties )
    {
        this.properties = properties;
        this.ruleSet = ruleSet;
        this.description = ruleSet.getDocumentation( );

        org.drools.RuleBaseBuilder builder = new org.drools.RuleBaseBuilder( );
        builder.setFactHandleFactory( Jsr94FactHandleFactory.getInstance( ) );
        try
        {
            builder.addRuleSet( ruleSet );
            this.ruleBase = builder.build( );
        }
        catch ( RuleIntegrationException e )
        {
            e.printStackTrace( );
        }
        catch ( RuleSetIntegrationException e )
        {
            e.printStackTrace( );
        }        
    }

    /**
     * Get an instance of the default filter, or null.
     *
     * @return An instance of the default filter, or null.
     */
    public synchronized ObjectFilter getObjectFilter( )
    {
        if ( this.objectFilter == null )
        {
            if ( this.defaultObjectFilterClassName != null )
            {
                ClassLoader cl =
                    Thread.currentThread( ).getContextClassLoader( );

                if ( cl == null )
                {
                    cl = RuleExecutionSetImpl.class.getClassLoader( );
                }

                try
                {
                    Class filterClass =
                        cl.loadClass( this.defaultObjectFilterClassName );
                    this.objectFilter =
                        ( ObjectFilter ) filterClass.newInstance( );
                }
                catch ( Exception e )
                {
                    throw new RuntimeException( e.toString( ) );
                }
            }
        }

        return this.objectFilter;
    }

    /**
     * Returns a new WorkingMemory object.
     *
     * @return A new WorkingMemory object.
     */
    public WorkingMemory newWorkingMemory( )
    {
        return this.ruleBase.newWorkingMemory( );
    }

    // JSR94 interface methods start here -------------------------------------

    /**
     * Get the name of this rule execution set.
     *
     * @return The name of this rule execution set.
     */
    public String getName( )
    {
        return this.ruleSet.getName( );
    }

    /**
     * Get a description of this rule execution set.
     *
     * @return A description of this rule execution set or null of no
     *         description is specified.
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
     * @return the value bound to the key or null
     */
    public Object getProperty( Object key )
    {
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
        this.properties.put( key, value );
    }

    /**
     * Set the default <code>ObjectFilter</code> class. This class is
     * instantiated at runtime and used to filter result objects unless
     * another filter is specified using the available APIs in the runtime
     * view of a rule engine.
     * <p/>
     * Setting the class name to null removes the default
     * <code>ObjectFilter</code>.
     *
     * @param objectFilterClassname the default <code>ObjectFilter</code> class
     */
    public void setDefaultObjectFilter( String objectFilterClassname )
    {
        this.defaultObjectFilterClassName = objectFilterClassname;
    }

    /**
     * Returns the default ObjectFilter class name
     * associated with this rule execution set.
     *
     * @return the default ObjectFilter class name
     */
    public String getDefaultObjectFilter( )
    {
        return this.defaultObjectFilterClassName;
    }

    /**
     * Return a list of all <code>Rule</code>s that are part of the
     * <code>RuleExecutionSet</code>.
     *
     * @return a list of all <code>Rule</code>s that are part of the
     *         <code>RuleExecutionSet</code>.
     */
    public List getRules( )
    {
        List jsr94Rules = new ArrayList( );

        Rule[] rules = ( this.ruleSet.getRules( ) );
        for ( int i = 0; i < rules.length; i++ )
        {
            jsr94Rules.add( new RuleImpl( rules[i] ) );
        }

        return jsr94Rules;
    }
}
