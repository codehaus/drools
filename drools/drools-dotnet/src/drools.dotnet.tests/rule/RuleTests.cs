using System;
using NUnit.Framework;
using org.drools.dotnet.rule;
using System.Threading;

namespace org.drools.dotnet.tests.rule
{
	[TestFixture]
	public class RuleTests
	{
		[Test]
		public void TestConditionsAndConsequence()
		{
			RuleSet ruleset = new RuleSet("Test RuleSet");
			Rule rule = new Rule("Test Rule", ruleset);
			Assert.AreEqual(1, ruleset.Rules.Count);

			rule.ParameterDeclarations.Add(new ParameterDeclaration("result", typeof(Result)));
			rule.Conditions.Add("result.Value < 5");
			rule.Conditions.Add("result.Value > 0");
			rule.Consequence = "result.Value = 10";
			RuleBaseBuilder builder = new RuleBaseBuilder();
			builder.RuleSets.Add(ruleset);
			RuleBase rulebase = builder.Build();
			
			//Test state where condition is not met
			WorkingMemory wm = rulebase.GetNewWorkingMemory();
			Result r = new Result(0);
			wm.AssertObject(r);
			wm.FireAllRules();
			Assert.AreEqual(0, r.Value);

			//Test state where condition is met
			wm = rulebase.GetNewWorkingMemory();
			r.Value = 1;
			wm.AssertObject(r);
			wm.FireAllRules();
			Assert.AreEqual(10, r.Value);
		}

		[Test]
		public void TestSalience()
		{
			RuleSet ruleset = new RuleSet("Test RuleSet");
			Rule rule1 = new Rule("Test Rule 1", ruleset);
			rule1.ParameterDeclarations.Add(new ParameterDeclaration("result", typeof(Result)));
			rule1.Conditions.Add("result.Value < 1000");
			rule1.Consequence = "if(result.Value == 0) result.Value = 100";
			rule1.Salience = 100;

			Rule rule2 = new Rule("Test Rule 2", ruleset);
			rule2.ParameterDeclarations.Add(new ParameterDeclaration("result", typeof(Result)));
			rule2.Conditions.Add("result.Value < 1000");
			rule2.Consequence = "if(result.Value == 100) result.Value = 1000";
			rule2.Salience = 10;

			RuleBaseBuilder builder = new RuleBaseBuilder();
			builder.RuleSets.Add(ruleset);
			RuleBase rulebase = builder.Build();

			WorkingMemory wm = rulebase.GetNewWorkingMemory();
			Result r = new Result(0);
			wm.AssertObject(r);
			wm.FireAllRules();
			Assert.AreEqual(1000, r.Value);
		}

		[Test]
		public void TestDuration()
		{
			RuleSet ruleset = new RuleSet("Test RuleSet");
			Rule rule = new Rule("Test Rule", ruleset);
			rule.ParameterDeclarations.Add(new ParameterDeclaration("result", typeof(Result)));
			rule.Conditions.Add("result.Value == 0");
			rule.Consequence = "result.Value = 1";
			rule.Duration = 2;

			RuleBaseBuilder builder = new RuleBaseBuilder();
			builder.RuleSets.Add(ruleset);
			RuleBase rulebase = builder.Build();
			WorkingMemory wm = rulebase.GetNewWorkingMemory();
			Result r = new Result(0);
			wm.AssertObject(r);
			wm.FireAllRules();
			Assert.AreEqual(0, r.Value);
			Thread.Sleep(3000);
			Assert.AreEqual(1, r.Value);
		}

		[Test]
		public void TestNoLoop()
		{
			//Test NoLoop as true
			RuleSet ruleset = new RuleSet("Test RuleSet");
			Rule rule = new Rule("Test Rule", ruleset);
			rule.ParameterDeclarations.Add(new ParameterDeclaration("result", typeof(Result)));
			rule.Conditions.Add("result.Value < 5");
			rule.Consequence = "result.Value++; drools.modifyObject(result);";
			rule.IsNoLoop = true;
			RuleBaseBuilder builder = new RuleBaseBuilder();
			builder.RuleSets.Add(ruleset);
			RuleBase rulebase = builder.Build();

			WorkingMemory wm = rulebase.GetNewWorkingMemory();
			Result r = new Result(0);
			wm.AssertObject(r);
			wm.FireAllRules();
			Thread.Sleep(1000);
			Assert.AreEqual(1, r.Value);

			//Test NoLoop as false
			rule.IsNoLoop = false;
			rulebase = builder.Build();

			wm = rulebase.GetNewWorkingMemory();
			r = new Result(0);
			wm.AssertObject(r);
			wm.FireAllRules();
			Thread.Sleep(1000);
			Assert.AreEqual(5, r.Value);
		}

		[Test]
		[Ignore("Need to write XorGroup unit test.")]
		public void TestXorGroup()
		{
			throw new NotImplementedException();
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
