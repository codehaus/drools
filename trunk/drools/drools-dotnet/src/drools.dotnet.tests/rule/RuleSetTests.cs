using System;
using NUnit.Framework;
using org.drools.dotnet.rule;

namespace org.drools.dotnet.tests.rule
{
	[TestFixture]
	public class RuleSetTests
	{
		public void TestRules()
		{
			RuleSet ruleset = new RuleSet("Test RuleSet");
			Rule rule = new Rule("Test Rule");
			ruleset.Rules.Add(rule);
			Assert.AreEqual(1, ruleset.Rules.Count);
		}

		[Test]
		public void TestFunctions()
		{
			RuleSet ruleset = new RuleSet("Test RuleSet");
			ruleset.Functions.Add("int GetValue(){ return 1; }");
			Rule rule = new Rule("Test Rule", ruleset);
			rule.ParameterDeclarations.Add(new ParameterDeclaration("result", typeof(Result)));
			rule.Conditions.Add("result.Value == 0");
			rule.Consequence = "result.Value = GetValue();";

			RuleBaseBuilder builder = new RuleBaseBuilder();
			builder.RuleSets.Add(ruleset);
			RuleBase rulebase = builder.Build();
			WorkingMemory wm = rulebase.GetNewWorkingMemory();
			Result r = new Result(0);
			wm.AssertObject(r);
			wm.FireAllRules();
			Assert.AreEqual(1, r.Value);
		}

		[Test]
		public void TestImports()
		{
			RuleSet ruleset = new RuleSet("Test RuleSet");
			ruleset.Imports.Add("System.Threading");
			Rule rule = new Rule("Test Rule", ruleset);
			rule.ParameterDeclarations.Add(new ParameterDeclaration("result", typeof(Result)));
			rule.Conditions.Add("result.Value == 0");
			rule.Consequence = "Thread.Sleep(1); result.Value = 1;";

			RuleBaseBuilder builder = new RuleBaseBuilder();
			builder.RuleSets.Add(ruleset);
			RuleBase rulebase = builder.Build();
			WorkingMemory wm = rulebase.GetNewWorkingMemory();
			Result r = new Result(0);
			wm.AssertObject(r);
			wm.FireAllRules();
			Assert.AreEqual(1, r.Value);
		}

		[Test]
		[Ignore("Ignored until .NET Semantics support application data.")]
		public void TestApplicationData()
		{
			RuleSet ruleset = new RuleSet("Test RuleSet");
			ruleset.ApplicationDataDeclarations.Add(new ApplicationDataDeclaration("appDataValue",
				typeof(Int32)));
			Rule rule = new Rule("Test Rule", ruleset);
			rule.ParameterDeclarations.Add(new ParameterDeclaration("result", typeof(Result)));
			rule.Conditions.Add("result.Value == 0");
			rule.Consequence = "result.Value = appDataValue;";

			RuleBaseBuilder builder = new RuleBaseBuilder();
			builder.RuleSets.Add(ruleset);
			RuleBase rulebase = builder.Build();
			WorkingMemory wm = rulebase.GetNewWorkingMemory();
			wm.SetApplicationData("appDataValue", 1);
			Result r = new Result(0);
			wm.AssertObject(r);
			wm.FireAllRules();
			Assert.AreEqual(1, r.Value);
		}

		public class Result
		{
			public int Value;
			public Result(int value)
			{
				Value = value;
			}
		}
	}
}
