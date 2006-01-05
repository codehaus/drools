using System;
using NUnit.Framework;
using org.drools.spi;
using org.drools.rule;
using NMock;
using org.drools.smf;

namespace org.drools.semantics.dotnet.tests
{
	[TestFixture]
	public class DotNetBlockConsequenceTests
	{
		[Test]
		public void TestBlockConsequence()
		{
			Rule rule = CreateRule();
			Int64 startTime = DateTime.Now.Ticks;
			Consequence c = new DotNetBlockConsequence(rule, 1, "System.Threading.Thread.Sleep(1000);");
			Mock tupleMock = new DynamicMock(typeof(Tuple));
			tupleMock.ExpectAndReturn("get", "test", rule.getParameterDeclaration("param1"));
			tupleMock.ExpectAndReturn("get", 1, rule.getParameterDeclaration("param2"));
			c.invoke((Tuple)tupleMock.MockInstance);
			tupleMock.Verify();

			Int64 endTime = DateTime.Now.Ticks;
			TimeSpan span = new TimeSpan(endTime - startTime);
			Assert.IsTrue(span.Seconds >= 1);
		}

		[Test]
		[ExpectedException(typeof(CodeCompilationException))]
		public void TestCompilationError()
		{
			Rule rule = CreateRule();
			Consequence c = new DotNetBlockConsequence(rule, 1, "foobar...");
		}

		[Test]
		[ExpectedException(typeof(ConsequenceException))]
		public void TestRuntimeError()
		{
			Rule rule = CreateRule();
			Consequence c = new DotNetBlockConsequence(rule, 1, "Console.WriteLine(param1.ToString());");
			Mock tupleMock = new DynamicMock(typeof(Tuple));
			tupleMock.ExpectAndReturn("get", null, rule.getParameterDeclaration("param1"));
			tupleMock.ExpectAndReturn("get", 0, rule.getParameterDeclaration("param2"));
			c.invoke((Tuple)tupleMock.MockInstance);
		}

		[Test]
		public void TestFactory()
		{
			Mock configMock = new DynamicMock(typeof(Configuration));
			Configuration config = (Configuration)configMock.MockInstance;
			configMock.ExpectAndReturn("getText", "", null);

			Mock rbcMock = new DynamicMock(typeof(RuleBaseContext));
			RuleBaseContext rbc = (RuleBaseContext)rbcMock.MockInstance;
			rbcMock.ExpectAndReturn("get", null, "dotnet-consequence-id");
			rbcMock.Expect("put", new object[] { "dotnet-consequence-id", 1 });

			Rule rule = CreateRule();
			ConsequenceFactory factory = new DotNetBlockConsequenceFactory();
			Consequence consequence = factory.newConsequence(rule, rbc, config);
			configMock.Verify();
			rbcMock.Verify();

			Assert.IsNotNull(consequence);
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
