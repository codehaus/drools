<%@ page import="javax.naming.InitialContext,
                 java.io.Reader,
                 java.io.InputStreamReader,
                 java.util.List,
                 java.util.ArrayList,
                 sisters.Person,
                 java.io.FileInputStream,
                 org.drools.jsr94.rules.RuleServiceProviderImpl,
                 javax.naming.NameNotFoundException,
                 java.io.IOException,
                 javax.rules.admin.*,
                 javax.rules.*,
                 java.util.HashMap"%>
<html>
<head>
<link rel="stylesheet" href="../styles.css">
<title>Drools JSR-94 Compliant Rule Engine</title>
</head>
<body>

<a href="http://www.drools.org"><img src="http://images.werken.com/drools.gif" border="0"></a>

<h3>Example Sisters</h3>
<p>

<%
    String SISTERS_RULES_URI = "http://drools.org/rules/example/sisters";

    RuleServiceProvider ruleServiceProvider = null;
    try {
        InitialContext iniCtx = new InitialContext();
        ruleServiceProvider = (RuleServiceProvider)iniCtx.lookup("java:/jsr94/drools");
    }
    catch (NameNotFoundException ex) {
        System.err.println(ex);
        ruleServiceProvider = new RuleServiceProviderImpl();
    }

    StatelessRuleSession ruleSession = null;
    try
    {
        RuleAdministrator ruleAdministrator = ruleServiceProvider.getRuleAdministrator();

        // Load the rules
        RuleExecutionSetProvider ruleSetProvider = ruleAdministrator.getRuleExecutionSetProvider(null);
        String ruleUri = Person.class.getResource("sisters.drl").toExternalForm();
        RuleExecutionSet ruleSet = ruleSetProvider.createRuleExecutionSet(ruleUri, null);

        // register the rule set
        ruleAdministrator.registerRuleExecutionSet(SISTERS_RULES_URI, ruleSet, null);

        HashMap appData = new HashMap();
        appData.put("message", "This came from the appData HashMap");

        // obtain the stateless rule session
        RuleRuntime ruleRuntime = ruleServiceProvider.getRuleRuntime();
        ruleSession = (StatelessRuleSession)ruleRuntime.createRuleSession(SISTERS_RULES_URI, appData, RuleRuntime.STATELESS_SESSION_TYPE);

        List inObjects = new ArrayList();
        Person person = new Person("bob");
        inObjects.add(person);

        person = new Person("rebecca");
        person.addSister("jeannie");
        inObjects.add(person);

        person = new Person("jeannie");
        person.addSister("rebecca");
        inObjects.add(person);

        // execute the rules
        List outList = ruleSession.executeRules(inObjects);

        out.println("<code>");
        for (int i = 0; i < outList.size(); i++) {
            Object obj = outList.get(i);
            out.println("[" + obj.getClass().getName() + "] " + obj + "<br>");
        }
        out.println("</code>");
    }
    catch (Exception ex)
    {
        ex.printStackTrace();
        throw ex;
    }
    finally
    {
        if (ruleSession != null)
        {
            ruleSession.release();
        }
    }
%>

</body>
</html>
