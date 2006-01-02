using System;
using NUnit.Framework;
using org.drools.rule;
using org.drools.spi;
using org.drools.smf;
using org.drools.@event;
using org.drools.reteoo;
using org.drools.semantics.@base;

namespace org.drools.semantics.dotnet.tests
{
	[TestFixture]
	public class DotNetConditionFactoryTests
	{
		[Test]
		public void TestSimpleCondition()
		{
            RuleBaseContext context = new RuleBaseContext();
            DefaultConfiguration config = new DefaultConfiguration("test config");
			RuleBaseBuilder ruleBaseBuilder = new RuleBaseBuilder(context);
			Importer importer = new DotNetImporter();

			RuleSet ruleSet = new RuleSet("test RuleSet");
			Rule rule = new Rule("Test Rule 1", ruleSet);
			rule.setImporter(importer);

			importer.addImport(new BaseImportEntry("System"));
			importer.addImport(new BaseImportEntry("System.Threading"));

			//Create parameters
			ObjectTypeFactory objectFactory = new DotNetObjectTypeFactory();
			config.setText("Int32");
			ObjectType o = objectFactory.newObjectType(rule, context, config);
			Declaration parm1 = rule.addParameterDeclaration("number1", o);
			
			//Create condition
			DotNetConditionFactory factory = new DotNetConditionFactory();
			//config.setText("number1 > 10");
			config.setText("Thread.CurrentThread.IsAlive");
			Condition[] condition = factory.newCondition(rule, context, config);
			rule.addCondition(condition[0]);
			
			//Create consequence
			config.setText("Thread.Sleep(1);Console.WriteLine(\"Done\");");
			DotNetBlockConsequenceFactory consequenceFactory = new DotNetBlockConsequenceFactory();
			Consequence consequence = consequenceFactory.newConsequence(rule, context, config);
			rule.setConsequence(consequence);

			ruleSet.addRule(rule);
			ruleBaseBuilder.addRuleSet(ruleSet);
			RuleBase ruleBase = ruleBaseBuilder.build();
			Dumper dumper = new Dumper(ruleBase);
			dumper.dumpRete(java.lang.System.@out);
			WorkingMemory workingMemory = ruleBase.newWorkingMemory();
			workingMemory.addEventListener(new DebugWorkingMemoryEventListener());
			int number = 32;
			workingMemory.assertObject(number);
			workingMemory.fireAllRules();
		}
	}
}
