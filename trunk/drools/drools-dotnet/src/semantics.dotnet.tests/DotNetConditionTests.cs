using System;
using NUnit.Framework;
using org.drools.rule;
using org.drools.spi;
using org.drools.smf;
using NMock;

namespace org.drools.semantics.dotnet.tests
{
	[TestFixture]
	public class DotNetConditionTests
	{
		[Test]
		public void TestTrueCondition()
		{
			Rule rule = CreateRule();
			Int64 startTime = DateTime.Now.Ticks;
			Condition c = new DotNetCondition(rule, 1, "param1.Length == 4;");
			Mock tupleMock = new DynamicMock(typeof(Tuple));
			tupleMock.ExpectAndReturn("get", "test", rule.getParameterDeclaration("param1"));
			tupleMock.ExpectAndReturn("get", 1, rule.getParameterDeclaration("param2"));
			bool result = c.isAllowed((Tuple)tupleMock.MockInstance);
			tupleMock.Verify();
			Assert.IsTrue(result);
		}

		[Test]
		public void TestFalseCondition()
		{
			Rule rule = CreateRule();
			Int64 startTime = DateTime.Now.Ticks;
			Condition c = new DotNetCondition(rule, 1, "param1.Equals(param2);");
			Mock tupleMock = new DynamicMock(typeof(Tuple));
			tupleMock.ExpectAndReturn("get", "test", rule.getParameterDeclaration("param1"));
			tupleMock.ExpectAndReturn("get", 1, rule.getParameterDeclaration("param2"));
			bool result = c.isAllowed((Tuple)tupleMock.MockInstance);
			tupleMock.Verify();
			Assert.IsFalse(result);
		}

		[Test]
		[ExpectedException(typeof(CodeCompilationException))]
		public void TestCompilationError()
		{
			Rule rule = CreateRule();
			Condition c = new DotNetCondition(rule, 1, "foobar...");
		}

		[Test]
		[ExpectedException(typeof(ConditionException))]
		public void TestRuntimeError()
		{
			Rule rule = CreateRule();
			Condition c = new DotNetCondition(rule, 1, "param1.Equals(String.Empty);");
			Mock tupleMock = new DynamicMock(typeof(Tuple));
			tupleMock.ExpectAndReturn("get", null, rule.getParameterDeclaration("param1"));
			tupleMock.ExpectAndReturn("get", 0, rule.getParameterDeclaration("param2"));
			c.isAllowed((Tuple)tupleMock.MockInstance);
		}

		[Test]
		public void TestFactory()
		{
			Mock configMock = new DynamicMock(typeof(Configuration));
			Configuration config = (Configuration)configMock.MockInstance;
			configMock.ExpectAndReturn("getText", "true", null);

			Mock rbcMock = new DynamicMock(typeof(RuleBaseContext));
			RuleBaseContext rbc = (RuleBaseContext)rbcMock.MockInstance;
			rbcMock.ExpectAndReturn("get", null, "dotnet-condition-id");
			rbcMock.Expect("put", new object[] { "dotnet-condition-id", 1 });

			Rule rule = CreateRule();
			ConditionFactory factory = new DotNetConditionFactory();
			Condition[] conditions = factory.newCondition(rule, rbc, config);
			configMock.Verify();
			rbcMock.Verify();

			Assert.IsNotNull(conditions);
		}

		private Rule CreateRule()
		{
			RuleSet ruleset = new RuleSet("test ruleset");
			Rule rule = new Rule("test rule", ruleset);
			rule.setImporter(new DotNetImporter());
			rule.addParameterDeclaration("param1", new DotNetObjectType(typeof(String)));
			rule.addParameterDeclaration("param2", new DotNetObjectType(typeof(Int32)));
			return rule;
		}
	}
}
