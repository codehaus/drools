package org.drools.jsr94.rules.admin;

/*
 $Id: LocalRuleExecutionSetProviderImpl.java,v 1.7 2004-03-27 15:52:25 n_alex Exp $

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

import org.drools.io.RuleSetReader;
import org.drools.io.RuleBaseBuilder;
import org.drools.rule.Rule;
import org.drools.rule.RuleSet;
import org.drools.RuleBase;

import javax.rules.admin.LocalRuleExecutionSetProvider;
import javax.rules.admin.RuleExecutionSet;
import javax.rules.admin.RuleExecutionSetCreateException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * The <code>LocalRuleExecutionSetProvider</code> interface defines <code>RuleExecutionSet</code> 
 * creation methods for defining <code>RuleExecutionSets</code> from local (non-serializable) resources.
 *
 * @see LocalRuleExecutionSetProvider
 *
 * @author <a href="mailto:thomas.diesler@softcon-itec.de">thomas diesler</a>
 * @author N. Alex Rupp (n_alex <at> codehaus.org)
 */
public class LocalRuleExecutionSetProviderImpl implements LocalRuleExecutionSetProvider
{

    private RuleExecutionSetImpl ruleExecutionSet = new RuleExecutionSetImpl();

    /**
     * Creates a <code>RuleExecutionSet</code> implementation using a supplied input stream and additional
     * vendor-specific properties.
     *
     * @see LocalRuleExecutionSetProvider#createRuleExecutionSet(InputStream,Map)
     */
    public RuleExecutionSet createRuleExecutionSet(InputStream ruleExecutionSetStream,
                                                   Map properties)
        throws IOException, RuleExecutionSetCreateException
    {
        Reader reader = new InputStreamReader( ruleExecutionSetStream );
        return createRuleExecutionSet( reader, properties );
    }

    /**
     * Creates a <code>RuleExecutionSet</code> implementation from a vendor specific AST representation
     * and vendor-specific properties.
     * <p>
     * This method accepts <code>org.drools.rule.Rule</code> and <code>org.drools.rule.RuleSet</code> objects or
     * a <code>List</code> of these objects.
     *
     * @see LocalRuleExecutionSetProvider#createRuleExecutionSet(Object,Map)
     */
    public RuleExecutionSet createRuleExecutionSet(Object astObject,
                                                   Map properties)
        throws RuleExecutionSetCreateException
    {
        try
        {
            if ( astObject instanceof List )
            {
                Iterator itDroolRules = ( (List) astObject ).iterator();
                while ( itDroolRules.hasNext() )
                {
                    Object object = itDroolRules.next();
                    createRuleExecutionSet( object, properties );
                }
            }
            else if ( astObject instanceof RuleSet )
            {
                RuleSet ruleSet = (RuleSet) astObject;

                // the first drool rule set inits the ruleExecutionSet
                if ( ruleExecutionSet.getName() == null )
                {
                    ruleExecutionSet.setName( ruleSet.getName() );
                    ruleExecutionSet.setDescription( null );
                }

                // recursivly add the rules
                Rule[] rules = ruleSet.getRules();

                for ( int i = 0 ; i < rules.length ; i++ )
                {
                    createRuleExecutionSet( rules[ i ],
                                            properties );
                }
            }
            else if ( astObject instanceof Rule )
            {
                Rule rule = (Rule) astObject;

                // the first drool rule inits the ruleExecutionSet
                if ( ruleExecutionSet.getName() == null )
                {
                    ruleExecutionSet.setName( rule.getName() );
                    ruleExecutionSet.setDescription( null );
                }


                ruleExecutionSet.addRules( new Rule[] { rule } );
            }
            else
            {
                throw new RuleExecutionSetCreateException( "invalid object type: " + astObject.getClass().getName() );
            }

        }
        catch ( Exception ex )
        {
            throw new RuleExecutionSetCreateException( "cannot create rule set", ex );
        }

        return ruleExecutionSet;
    }

    /**
     * Creates a <code>RuleExecutionSet</code> implementation using a supplied character stream Reader
     * and vendor-specific properties..
     *
     * @see LocalRuleExecutionSetProvider#createRuleExecutionSet(Reader,Map)
     */
    public RuleExecutionSet createRuleExecutionSet(Reader ruleReader,
                                                   Map properties)
            throws RuleExecutionSetCreateException, IOException
    {

        try
        {

            RuleBase ruleBase = RuleBaseBuilder.buildFromReader(ruleReader);
            RuleSet[] ruleSet = ruleBase.getRuleSets();

            // add the ruleBase to the execution set
            ruleExecutionSet.setRuleBase(ruleBase);
            createRuleExecutionSet( ruleSet[0], properties );

        }
        catch ( IOException ex )
        {
            throw ex;
        }
        catch ( Exception ex )
        {
            throw new RuleExecutionSetCreateException( "cannot create rule set", ex );
        }

        if ( ruleExecutionSet.getRules().size() == 0 )
        {
            throw new RuleExecutionSetCreateException( "no rules found" );
        }

        return ruleExecutionSet;
    }
}
