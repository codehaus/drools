package org.drools.jsr94.rules.admin;

/*
 * $Id: LocalRuleExecutionSetProviderTestCase.java,v 1.8 2004/07/21 15:55:20 bob
 * Exp $
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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

import javax.rules.admin.LocalRuleExecutionSetProvider;
import javax.rules.admin.RuleAdministrator;
import javax.rules.admin.RuleExecutionSet;

import org.drools.io.RuleSetReader;
import org.drools.jsr94.rules.RuleEngineTestBase;
import org.drools.rule.RuleSet;

/**
 * Test the LocalRuleExecutionSetProvider implementation.
 * 
 * @author N. Alex Rupp (n_alex <at>codehaus.org)
 * @author <a href="mailto:thomas.diesler@softcon-itec.de">thomas diesler </a>
 */
public class LocalRuleExecutionSetProviderTestCase extends RuleEngineTestBase
{

    private RuleAdministrator             ruleAdministrator;

    private LocalRuleExecutionSetProvider ruleSetProvider;

    protected void setUp() throws Exception
    {
        super.setUp( );
        ruleAdministrator = ruleServiceProvider.getRuleAdministrator( );
        ruleSetProvider = ruleAdministrator
                                           .getLocalRuleExecutionSetProvider( null );
    }

    public void testCreateFromInputStream() throws Exception
    {
        InputStream rulesStream = org.drools.jsr94.rules.RuleEngineTestBase.class
                                                                                 .getResourceAsStream( bindUri );
        RuleExecutionSet ruleSet = ruleSetProvider
                                                  .createRuleExecutionSet(
                                                                           rulesStream,
                                                                           null );
        assertEquals( "rule set name", "Sisters Rules", ruleSet.getName( ) );
        assertEquals( "number of rules", 1, ruleSet.getRules( ).size( ) );
    }

    public void testCreateFromObject() throws Exception
    {
        InputStream inputStream = null;
        try
        {
            inputStream = org.drools.jsr94.rules.RuleEngineTestBase.class
                                                                         .getResourceAsStream( bindUri );
            Reader in = new InputStreamReader( inputStream );
            RuleSet ruleSet = new RuleSetReader( ).read( in );
            RuleExecutionSet ruleExecutionSet = ruleSetProvider
                                                               .createRuleExecutionSet(
                                                                                        ruleSet,
                                                                                        null );
            assertEquals( "rule set name", "Sisters Rules",
                          ruleExecutionSet.getName( ) );
            assertEquals( "number of rules", 1, ruleExecutionSet.getRules( )
                                                                .size( ) );
        }
        catch ( IOException e )
        {
            fail( "Couldn't create the RuleExecutionSet. Test threw an IOException." );
        }
        finally
        {
            if ( inputStream != null )
            {
                try
                {
                    inputStream.close( );
                }
                catch ( IOException e )
                {
                    e.printStackTrace( );
                }
            }
        }
    }

    /**
     * Test createRuleExecutionSet from Reader.
     */
    public void testCreateFromReader() throws Exception
    {
        Reader ruleReader = new InputStreamReader(
                                                   org.drools.jsr94.rules.RuleEngineTestBase.class
                                                                                                  .getResourceAsStream( bindUri ) );
        RuleExecutionSet ruleSet = ruleSetProvider
                                                  .createRuleExecutionSet(
                                                                           ruleReader,
                                                                           null );
        assertEquals( "rule set name", "Sisters Rules", ruleSet.getName( ) );
        assertEquals( "number of rules", 1, ruleSet.getRules( ).size( ) );
    }
}