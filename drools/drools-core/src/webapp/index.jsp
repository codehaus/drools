<%@ page import="javax.rules.RuleServiceProvider,
                 javax.rules.admin.RuleAdministrator,
                 javax.naming.InitialContext,
                 javax.rules.admin.LocalRuleExecutionSetProvider,
                 java.io.Reader,
                 java.io.InputStreamReader,
                 javax.rules.admin.RuleExecutionSet,
                 javax.rules.RuleRuntime,
                 javax.rules.StatelessRuleSession,
                 java.util.List,
                 java.util.ArrayList,
                 sisters.Person,
                 java.io.FileInputStream"%>
<html>
<head>
<link rel="stylesheet" href="styles.css">
<title>Drools JSR-94 Compliant Rule Engine</title>
</head>
<body>

<img src="drools.gif">

<h3>JSR-94 Compliant Rule Engine Integration using JCA</h3>
<p>

<%
   String SISTERS_RULES_URI = "http://drools.org/rules/example/sisters";

   InitialContext iniCtx = new InitialContext();
   RuleServiceProvider ruleServiceProvider = (RuleServiceProvider)iniCtx.lookup("java:/jsr94/drools");

   RuleAdministrator ruleAdministrator = ruleServiceProvider.getRuleAdministrator();

   // Load the rules
   LocalRuleExecutionSetProvider ruleSetProvider = ruleAdministrator.getLocalRuleExecutionSetProvider(null);
   Reader ruleReader = new InputStreamReader(new FileInputStream(application.getRealPath("sisters.drl")));
   RuleExecutionSet ruleSet = ruleSetProvider.createRuleExecutionSet(ruleReader, null);

   // register the rule set
   ruleAdministrator.registerRuleExecutionSet(SISTERS_RULES_URI, ruleSet, null);

   // obtain the stateless rule session
   RuleRuntime ruleRuntime = ruleServiceProvider.getRuleRuntime();
   StatelessRuleSession ruleSession = (StatelessRuleSession)ruleRuntime.createRuleSession(SISTERS_RULES_URI, null, RuleRuntime.STATELESS_SESSION_TYPE);

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

   ruleSession.release();

   out.println("<code>");
   for (int i = 0; i < outList.size(); i++) {
      Object obj = outList.get(i);
      out.println("[" + obj.getClass().getName() + "] " + obj + "<br>");
   }
   out.println("</code>");

%>

</body>
</html>
