package org.drools.jsr94.rules;

// Here's a short list of the classes you'll need:

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