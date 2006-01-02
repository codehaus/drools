using System;
using System.IO;
using NUnit.Framework;
using org.drools.@event;

namespace org.drools.semantics.dotnet.tests.examples.state
{
	[TestFixture]
	public class StateTests
	{
		[Test]
		public void TestStateExample()
		{
			RuleBase ruleBase = DotNetRuleBaseLoader.Load(
				new FileInfo("./examples/state/state.dotnet.drl.xml"));

			WorkingMemory workingMemory = ruleBase.newWorkingMemory();
			workingMemory.addEventListener(new DebugWorkingMemoryEventListener());
			State a = new State("A");
			State b = new State("B");
			State c = new State("C");
			State d = new State("D");

			bool dynamic = true;

			workingMemory.assertObject(a, dynamic);
			workingMemory.assertObject(b, dynamic);
			workingMemory.assertObject(c, dynamic);
			workingMemory.assertObject(d, dynamic);

			workingMemory.fireAllRules();

			Assert.AreEqual(Status.Finished, a.Status);
			Assert.AreEqual(Status.Finished, b.Status);
			Assert.AreEqual(Status.Finished, c.Status);
			Assert.AreEqual(Status.Finished, d.Status);
		}
	}
}
