package org.drools.jsr94.rules;

/*
 $Id: ExampleRuleEngineFacade.java,v 1.3 2004-06-11 23:31:27 mproctor Exp $

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

import javax.rules.RuleRuntime;
import javax.rules.RuleServiceProvider;
import javax.rules.RuleServiceProviderManager;
import javax.rules.StatelessRuleSession;
import javax.rules.StatefulRuleSession;
import javax.rules.admin.LocalRuleExecutionSetProvider;
import javax.rules.admin.RuleAdministrator;
import javax.rules.admin.RuleExecutionSet;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;

/**
 * Builds up the JSR94 object structure.  It'll simplify the task of building a
 * <code>RuleExecutionSet</code> and associated <code>RuntimeSession</code>
 * objects from a given <code>InputStream</code>.
 *
 * @author N. Alex Rupp (n_alex <at> codehaus.org)
 */
public class ExampleRuleEngineFacade {

    public static final String RULE_SERVICE_PROVIDER = "http://drools.org/";

    private RuleAdministrator ruleAdministrator;
    private RuleServiceProvider ruleServiceProvider;
    private LocalRuleExecutionSetProvider ruleSetProvider;
    private RuleRuntime ruleRuntime;

    // configuration parameters
    String ruleFilesDirectory;
    String ruleFilesIncludes;

    public ExampleRuleEngineFacade() throws Exception {
        RuleServiceProviderManager
                .registerRuleServiceProvider(RULE_SERVICE_PROVIDER,
                        RuleServiceProviderImpl.class);

        ruleServiceProvider =
                RuleServiceProviderManager
                .getRuleServiceProvider(RULE_SERVICE_PROVIDER);

        ruleAdministrator = ruleServiceProvider
                .getRuleAdministrator();

        ruleSetProvider = ruleAdministrator
                .getLocalRuleExecutionSetProvider(null);
    }

    public void addRuleExecutionSet(String bindUri, InputStream resourceAsStream)
            throws Exception {

        Reader ruleReader =
                new InputStreamReader(resourceAsStream);
        
        RuleExecutionSet ruleExecutionSet =
                ruleSetProvider
                .createRuleExecutionSet(ruleReader,
                        null);

        ruleAdministrator
                .registerRuleExecutionSet(bindUri,
                        ruleExecutionSet,
                        null);
    }

    /**
     * Returns a named <code>StatelessRuleSession</code>.
     *
     * @param key
     * @return StatelessRuleSession
     * @throws Exception
     */
    public StatelessRuleSession getStatelessRuleSession(String key)
            throws Exception {

        ruleRuntime = ruleServiceProvider
                .getRuleRuntime();

        return (StatelessRuleSession) ruleRuntime
                .createRuleSession(key,
                        null,
                        RuleRuntime.STATELESS_SESSION_TYPE);
    }

    public StatefulRuleSession getStatefulRuleSession(String key)
            throws Exception {

        ruleRuntime = ruleServiceProvider
                        .getRuleRuntime();

        return (StatefulRuleSession) ruleRuntime
                .createRuleSession(
                        key,
                        null,
                        RuleRuntime.STATEFUL_SESSION_TYPE);
    }

    public RuleServiceProvider getRuleServiceProvider()
    {
        return this.ruleServiceProvider;
    }
}